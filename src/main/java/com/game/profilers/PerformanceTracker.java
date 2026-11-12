package com.game.profilers;

import com.game.Init;
import com.game.screens.LoadingScreen;
import com.game.utils.LogType;
import com.game.utils.LoggerUtil;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.jctools.queues.MpscUnboundedArrayQueue;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@Init(stage = 0)
public class PerformanceTracker {
  private static final long ONE_SECOND_NS = 1_000_000_000L;
  
  private static final long CALCULATION_INTERVAL_NS = 200_000_000L;//200ms
  
  public static final long LOG_INTERVAL_NS = 2_000_000_000L;//2s
  
  private static final long TAIL_INTERVAL_NS = 500_000_000L;//500ms
  
  // history
  // if is older than 20 secs, remove
  // if more than 20k entries:
  // remove the oldest entry until either there is 20k entries or
  // the oldest entry is younger than 10 seconds
  private static final long HARD_AGE_CAP_NS = 20_000_000_000L;
  private static final long SOFT_AGE_CAP_NS = 10_000_000_000L;
  private static final int MAX_ENTRIES = 20_000;
  
  // channels of the tick series
  private static final int TICK = 0;
  private static final int PHYSICS = 1;
  private static final int SPATIAL_PARTITION = 2;
  private static final int ENTITY = 3;
  private static final int INPUT = 4;
  private static final int CHANNEL_COUNT = 5;
  
  private static final MainMetric EMPTY_MAIN = new MainMetric(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  private static final SubsystemMetric EMPTY_SUBSYSTEM = new SubsystemMetric(0, 0, 0, 0, 0, 0);
  private static final Metrics EMPTY_METRICS = new Metrics(EMPTY_MAIN, EMPTY_MAIN, EMPTY_SUBSYSTEM, EMPTY_SUBSYSTEM, EMPTY_SUBSYSTEM, EMPTY_SUBSYSTEM);
  
  private static PerformanceTracker instance;
  
  private final ExecutorService executor;
  private final MpscUnboundedArrayQueue<FrameData> frameQueue = new MpscUnboundedArrayQueue<>(2048);
  private final MpscUnboundedArrayQueue<TickData> tickQueue = new MpscUnboundedArrayQueue<>(256);
  
  private volatile int targetFps = 0; // <= 0 if unlimited
  private final int targetTps = 10;
  
  private long lastLog = 0;
  
  private final AtomicLong lastScheduleNs = new AtomicLong(System.nanoTime() - CALCULATION_INTERVAL_NS);
  
  private volatile Metrics metrics = EMPTY_METRICS;
  

  private final Series frames = new Series(1);
  private final Series ticks = new Series(CHANNEL_COUNT);
  
  private long previousFrameStartNs;
  private boolean hasPreviousFrame;
  
  private long latestTimeNs = Long.MIN_VALUE;
  
  private Tail frameTail = Tail.EMPTY;
  private final Tail[] tickTails = new Tail[CHANNEL_COUNT];
  private long lastTailRefreshNs;
  private boolean hasTails;
  
  private long[] sortBuffer = new long[1024];
  
  public PerformanceTracker() {
    lastLog = System.nanoTime();
    Arrays.fill(tickTails, Tail.EMPTY);
    executor = Executors.newSingleThreadExecutor(runnable -> {
      Thread thread = new Thread(runnable, "performance tracker");
      thread.setDaemon(true);
      return thread;
    });
  }
  
  public static void init(LoadingScreen loadingScreen) {
    instance = new PerformanceTracker();
    loadingScreen.increment();
  }
  
  public static PerformanceTracker getInstance() {
    return instance;
  }
  
  public static void cleanup() {
    if (instance != null) {
      instance.executor.shutdown();
    }
  }
  
  public void setTargetFps(int targetFps) {
    this.targetFps = targetFps;
  }
  
  public Metrics getLatestMetrics() {
    return metrics;
  }
  
  public void submitFrameData(FrameData frameData) {
    frameQueue.offer(frameData);
    scheduleCalculationIfDue();
  }
  
  public void submitTickData(TickData tickData) {
    tickQueue.offer(tickData);
    scheduleCalculationIfDue();
  }
  
  private void scheduleCalculationIfDue() {
    long nowNs = System.nanoTime();
    long lastNs = lastScheduleNs.get();
    if (nowNs - lastNs < CALCULATION_INTERVAL_NS) return;
    if (!lastScheduleNs.compareAndSet(lastNs, nowNs)) return;
    try {
      executor.execute(this::calculateMetrics);
    } catch (RejectedExecutionException ignored) {}
  }
  
  private void calculateMetrics() {
    try {
      calculate();
      logMetricsIfDue();
    } catch (Exception e) {
      LoggerUtil.error("failed to calculate performance metrics", e);
    }
  }
  
  private void logMetricsIfDue() {
    if (System.nanoTime() - lastLog > LOG_INTERVAL_NS) {
      lastLog = System.nanoTime();
      Metrics snapshot = metrics;
      logMainMetric("Frame", "FPS", snapshot.frame());
      logMainMetric("Tick", "TPS", snapshot.tick());
      logSubsystemMetric("Physics", snapshot.physics());
      logSubsystemMetric("Spatial partition", snapshot.spatialPartition());
      logSubsystemMetric("Entity", snapshot.entity());
      logSubsystemMetric("Input", snapshot.input());
    }
  }
  
  private static void logMainMetric(String name, String rateName, MainMetric snapshot) {
    LoggerUtil.info(
        "{} {}: {0.00} {} (1% low:{0.00} 0.1% low:{0.00} 95th percentile:{0.00} 99th percentile:{0.00})"
            + " {0.00} ms (1% low:{0.00} 0.1% low:{0.00} 95th percentile:{0.00} 99th percentile:{0.00})"
            + " {0.00}% of budget (1% low:{0.00}% 0.1% low:{0.00}% 95th percentile:{0.00}% 99th percentile:{0.00}%)",
        LogType.PERFORMANCE.getString(), name,
        snapshot.metric(), rateName, snapshot.metric1(), snapshot.metric01(), snapshot.metric95(), snapshot.metric99(),
        snapshot.metricTime(), snapshot.metricTime1(), snapshot.metricTime01(), snapshot.metricTime95(), snapshot.metricTime99(),
        snapshot.percentBudget(), snapshot.percentBudget1(), snapshot.percentBudget01(), snapshot.percentBudget95(), snapshot.percentBudget99());
  }
  
  private static void logSubsystemMetric(String name, SubsystemMetric snapshot) {
    LoggerUtil.info(
        "{} {}: {0.00} ms ({0.00}% of tick) (1% low:{0.00} 0.1% low:{0.00} 95th percentile:{0.00} 99th percentile:{0.00})",
        LogType.PERFORMANCE.getString(), name,
        snapshot.metric(), snapshot.percentTotal(), snapshot.metric1(), snapshot.metric01(), snapshot.metric95(), snapshot.metric99());
  }
  
  private void calculate() {
    drainQueues();
    if (latestTimeNs == Long.MIN_VALUE) return;
    long referenceTimeNs = latestTimeNs;
    
    frames.evict(referenceTimeNs);
    ticks.evict(referenceTimeNs);
    
    if (!hasTails || referenceTimeNs - lastTailRefreshNs >= TAIL_INTERVAL_NS) {
      frameTail = computeTail(frames.values[0]);
      for (int channel = 0; channel < CHANNEL_COUNT; channel++) {
        tickTails[channel] = computeTail(ticks.values[channel]);
      }
      lastTailRefreshNs = referenceTimeNs;
      hasTails = true;
    }
    
    int frameFrom = frames.recentStart(referenceTimeNs);
    int tickFrom = ticks.recentStart(referenceTimeNs);
    
    MainMetric frameMetric = mainMetric(frames, 0, frameFrom, targetFps, frameTail);
    MainMetric tickMetric = mainMetric(ticks, TICK, tickFrom, targetTps, tickTails[TICK]);
    
    metrics = new Metrics(
        frameMetric,
        tickMetric,
        subsystemMetric(PHYSICS, tickFrom, tickTails[PHYSICS]),
        subsystemMetric(SPATIAL_PARTITION, tickFrom, tickTails[SPATIAL_PARTITION]),
        subsystemMetric(ENTITY, tickFrom, tickTails[ENTITY]),
        subsystemMetric(INPUT, tickFrom, tickTails[INPUT]));
  }
  
  private void drainQueues() {
    FrameData frameData;
    while ((frameData = frameQueue.poll()) != null) {
      if (hasPreviousFrame) {
        frames.ends.add(frameData.frameStart());
        frames.values[0].add(frameData.frameStart() - previousFrameStartNs);
      }
      previousFrameStartNs = frameData.frameStart();
      hasPreviousFrame = true;
      latestTimeNs = Math.max(latestTimeNs, frameData.frameEnd());
    }
    
    TickData tickData;
    while ((tickData = tickQueue.poll()) != null) {
      ticks.ends.add(tickData.tickEnd());
      ticks.values[TICK].add(tickData.tickEnd() - tickData.tickStart());
      ticks.values[PHYSICS].add(tickData.physicsTime());
      ticks.values[SPATIAL_PARTITION].add(tickData.spatialPartitionTime());
      ticks.values[ENTITY].add(tickData.entityTime());
      ticks.values[INPUT].add(tickData.inputTime());
      latestTimeNs = Math.max(latestTimeNs, tickData.tickEnd());
    }
  }
  
  private static MainMetric mainMetric(Series series, int channel, int from, int targetRate, Tail tail) {
    int entryCount = series.ends.size();
    if (entryCount == 0) return EMPTY_MAIN;
    
    LongArrayList channelValues = series.values[channel];
    int recentCount = entryCount - from;
    
    double averageNs = 0;
    for (int index = from; index < entryCount; index++) {
      averageNs += channelValues.getLong(index);
    }
    if (recentCount > 0) averageNs /= recentCount;
    
    long spanNs = recentCount > 1 ? series.ends.getLong(entryCount - 1) - series.ends.getLong(from) : 0;
    double rate = spanNs > 0 ? ((recentCount - 1) * 1_000_000_000d) / spanNs : toPerSecond(averageNs, targetRate);
    double metric = targetRate > 0 ? Math.min(targetRate, rate) : rate;
    
    double averageMs = averageNs / 1_000_000d;
    double low1PercentMs = tail.low1PercentNs() / 1_000_000d;
    double low01PercentMs = tail.low01PercentNs() / 1_000_000d;
    double percentile95Ms = tail.percentile95Ns() / 1_000_000d;
    double percentile99Ms = tail.percentile99Ns() / 1_000_000d;
    
    double budgetMs = targetRate > 0 ? 1000d / targetRate : 0;
    
    return new MainMetric(
        metric,
        toPerSecond(tail.low1PercentNs(), targetRate),
        toPerSecond(tail.low01PercentNs(), targetRate),
        toPerSecond(tail.percentile95Ns(), targetRate),
        toPerSecond(tail.percentile99Ns(), targetRate),
        averageMs, low1PercentMs, low01PercentMs, percentile95Ms, percentile99Ms,
        percentOf(averageMs, budgetMs),
        percentOf(low1PercentMs, budgetMs),
        percentOf(low01PercentMs, budgetMs),
        percentOf(percentile95Ms, budgetMs),
        percentOf(percentile99Ms, budgetMs));
  }
  
  private SubsystemMetric subsystemMetric(int channel, int from, Tail tail) {
    int entryCount = ticks.ends.size();
    if (entryCount == 0) return EMPTY_SUBSYSTEM;
    
    LongArrayList subsystemTimes = ticks.values[channel];
    LongArrayList tickTimes = ticks.values[TICK];
    int recentCount = entryCount - from;
    
    double sumNs = 0;
    double sumFraction = 0;
    for (int index = from; index < entryCount; index++) {
      long subsystemNs = subsystemTimes.getLong(index);
      sumNs += subsystemNs;
      long tickNs = tickTimes.getLong(index);
      if (tickNs > 0) sumFraction += (double) subsystemNs / tickNs;
    }
    
    double averageMs = recentCount > 0 ? sumNs / recentCount / 1_000_000d : 0;
    double percentTotal = recentCount > 0 ? (sumFraction / recentCount) * 100d : 0;
    
    return new SubsystemMetric(
        averageMs,
        tail.low1PercentNs() / 1_000_000d,
        tail.low01PercentNs() / 1_000_000d,
        tail.percentile95Ns() / 1_000_000d,
        tail.percentile99Ns() / 1_000_000d,
        percentTotal);
  }
  
  private Tail computeTail(LongArrayList values) {
    int count = values.size();
    if (count == 0) return Tail.EMPTY;
    if (sortBuffer.length < count) {
      sortBuffer = new long[Math.max(count, sortBuffer.length * 2)];
    }
    values.getElements(0, sortBuffer, 0, count);
    Arrays.sort(sortBuffer, 0, count);
    return new Tail(
        averageTail(sortBuffer, count, 0.99),
        averageTail(sortBuffer, count, 0.999),
        sortBuffer[(int) (count * 0.95)],
        sortBuffer[(int) (count * 0.99)]);
  }
  
  private static double averageTail(long[] sorted, int count, double percentileCutoff) {
    int startIndex = Math.min((int) (count * percentileCutoff), count - 1);
    double sum = 0;
    for (int index = startIndex; index < count; index++) {
      sum += sorted[index];
    }
    return sum / (count - startIndex);
  }
  
  private static double toPerSecond(double durationNs, int targetRate) {
    if (durationNs <= 0) return 0;
    double perSecondRate = 1_000_000_000d / durationNs;
    return targetRate > 0 ? Math.min(targetRate, perSecondRate) : perSecondRate;
  }
  
  private static double percentOf(double valueMs, double budgetMs) {
    return budgetMs > 0 ? (valueMs / budgetMs) * 100d : 0;
  }
  
  private static final class Series {
    final LongArrayList ends = new LongArrayList();
    final LongArrayList[] values;
    
    Series(int channelCount) {
      values = new LongArrayList[channelCount];
      for (int channel = 0; channel < channelCount; channel++) {
        values[channel] = new LongArrayList();
      }
    }
    
    void evict(long referenceTimeNs) {
      int entryCount = ends.size();
      int evictCount = 0;
      while (evictCount < entryCount) {
        long ageNs = referenceTimeNs - ends.getLong(evictCount);
        if (ageNs > HARD_AGE_CAP_NS || (entryCount - evictCount > MAX_ENTRIES && ageNs > SOFT_AGE_CAP_NS)) {
          evictCount++;
        } else {
          break;
        }
      }
      if (evictCount > 0) {
        ends.removeElements(0, evictCount);
        for (LongArrayList channelValues : values) {
          channelValues.removeElements(0, evictCount);
        }
      }
    }
    
    int recentStart(long referenceTimeNs) {
      int index = ends.size();
      while (index > 0 && referenceTimeNs - ends.getLong(index - 1) <= ONE_SECOND_NS) {
        index--;
      }
      return index;
    }
  }
  
  private record Tail(double low1PercentNs, double low01PercentNs, double percentile95Ns, double percentile99Ns) {
    static final Tail EMPTY = new Tail(0, 0, 0, 0);
  }
  
  public value record FrameData(long frameStart, long frameEnd) {
  }
  
  public value record TickData(long tickStart, long tickEnd, long physicsTime, long spatialPartitionTime,
                               long entityTime, long inputTime) {
  }
  
  public value record Metrics(MainMetric frame, MainMetric tick, SubsystemMetric physics,
                              SubsystemMetric spatialPartition, SubsystemMetric entity, SubsystemMetric input) {
  }
  
  public value record MainMetric(double metric, double metric1, double metric01, double metric95, double metric99,
                                 // in per second
                                 double metricTime, double metricTime1, double metricTime01, double metricTime95,
                                 double metricTime99, // in ms
                                 double percentBudget, double percentBudget1, double percentBudget01,
                                 double percentBudget95, double percentBudget99) {
  } // in percent
  
  public value record SubsystemMetric(double metric, double metric1, double metric01, double metric95, double metric99,
                                      double percentTotal) {
  }
}