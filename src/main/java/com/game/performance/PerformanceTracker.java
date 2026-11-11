package com.game.performance;

import com.game.Init;
import com.game.screens.LoadingScreen;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.jctools.queues.MpscUnboundedArrayQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Init(stage = 2)
public class PerformanceTracker {
  private static PerformanceTracker instance;
  ExecutorService executor;
  private int targetFps;
  private int targetTps;
  private LongArrayList frameStarts;//last second
  private LongArrayList frameTime;
  private LongArrayList frameEnds;
  
  private LongArrayList tickStarts;//last second
  private LongArrayList tickTime;
  private LongArrayList tickEnds;
  
  private LongArrayList physicsTime;
  private LongArrayList physicsTimeHistory;
  
  private LongArrayList spacialPartitionTime;
  private LongArrayList spacialPartitionTimeHistory;
  
  private LongArrayList entityTime;
  private LongArrayList entityTimeHistory;
  
  private LongArrayList inputTime;
  private LongArrayList inputTimeHistory;
  
  private LongArrayList renderTimeHistory;
  private LongArrayList tickTimeHistory;
  
  private Metrics metrics1;
  private Metrics metrics2;
  
  private volatile boolean isMetric1;
  
  private MpscUnboundedArrayQueue<FrameData> frameQueue;
  public PerformanceTracker() {
    frameStarts = new LongArrayList();
    frameTime = new LongArrayList();
    frameEnds = new LongArrayList();
    tickStarts = new LongArrayList();
    tickTime = new LongArrayList();
    tickEnds = new LongArrayList();
    physicsTime = new LongArrayList();
    spacialPartitionTime = new LongArrayList();
    spacialPartitionTimeHistory = new LongArrayList();
    entityTime = new LongArrayList();
    entityTimeHistory = new LongArrayList();
    inputTime = new LongArrayList();
    inputTimeHistory = new LongArrayList();
    renderTimeHistory = new LongArrayList();
    tickTimeHistory = new LongArrayList();
    physicsTimeHistory = new LongArrayList();
    spacialPartitionTimeHistory = new LongArrayList();
    frameQueue = new MpscUnboundedArrayQueue<>(200);
    executor = Executors.newSingleThreadExecutor();
    isMetric1 = true;
  }
  public void submitFrameData(FrameData frameData) {
    frameQueue.add(frameData);
  }
  public void submitTickData(TickData tickData) {
    executor.execute(() -> calculateMetrics(tickData));
  }
  private void calculateMetrics(TickData tickData) {
    //do stuff here
    isMetric1 = !isMetric1;
  }
  
  public static void init(LoadingScreen loadingScreen){
    instance = new PerformanceTracker();
    loadingScreen.increment();
  }
  public value record FrameData(long frameStart, long frameEnd){ }
  public value record TickData(long tickStart, long tickEnd, long physicsTime, long spatialPartitionTime, long entityTime, long inputTime){}
  public value record Metrics(BugetedMetric frame, BugetedMetric tick, Metric physics, Metric spatialPartition,
                              Metric entity, Metric input){}
  public value record BugetedMetric(double metric, double metric1, double metric01,
                                    double metric95, double metric99,
                                    double metricTimeBudget, double metricTimeBudget1, double metricTimeBudget01,
                                    double metricTimeBudget95, double metricTimeBudget99){}
  
  public value record Metric(double metric, double metric1, double metric01, double metric95, double metric99){}
}
