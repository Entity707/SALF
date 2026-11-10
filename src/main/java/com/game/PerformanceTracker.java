package com.game;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.jctools.queues.MpscUnboundedArrayQueue;

public class PerformanceTracker {
  private static PerformanceTracker instance;
  
  private int targetFps;
  private int targetTps;
  private LongArrayList frameStarts;//last second
  private LongArrayList frameTime;
  private LongArrayList frameEnds;
  
  private LongArrayList tickStarts;//last second
  private LongArrayList tickTime;
  private LongArrayList tickEnds;
  
  private LongArrayList physicsTime;
  private long[] physicsTimeHistory;
  
  private LongArrayList spacialPartitionTime;
  private long[] spacialPartitionTimeHistory;
  
  private LongArrayList entityTime;
  private long[] entityTimeHistory;
  
  private LongArrayList inputTime;
  private long[] inputTimeHistory;
  
  private long[] renderTimeHistory;
  private long[] tickTimeHistory;
  
  private Metrics metrics1;
  private Metrics metrics2;
  
  private volatile boolean isMetric1;
  
  private MpscUnboundedArrayQueue<FrameData> frameQueue;
  private MpscUnboundedArrayQueue<TickData> tickQueue;
  public static value class FrameData{
    public long frameStart;
    public long frameEnd;
    
    public FrameData(long frameStart, long frameEnd) {
      this.frameStart = frameStart;
      this.frameEnd = frameEnd;
    }
  }
  public static value class TickData{
    public long tickStart;
    public long tickEnd;
    public long physicsTime;
    public long spacialPartitionTime;
    public long entityTime;
    public long inputTime;
    
    public TickData(long tickStart, long tickEnd, long physicsTime, long spacialPartitionTime, long entityTime, long inputTime) {
      this.tickStart = tickStart;
      this.tickEnd = tickEnd;
      this.physicsTime = physicsTime;
      this.spacialPartitionTime = spacialPartitionTime;
      this.entityTime = entityTime;
      this.inputTime = inputTime;
    }
  }
  public static value class Metrics{
    public BugetedMetric frame;
    public BugetedMetric tick;
    public Metric physics;
    public Metric spatialPartition;
    public Metric entity;
    public Metric input;
    
    public Metrics(BugetedMetric frame, BugetedMetric tick, Metric physics, Metric spatialPartition, Metric entity, Metric input) {
      this.frame = frame;
      this.tick = tick;
      this.physics = physics;
      this.spatialPartition = spatialPartition;
      this.entity = entity;
      this.input = input;
    }
  }
  public static value class BugetedMetric{
    public double metric;
    public double metric1;
    public double metric01;
    public double metric95;
    public double metric99;
    public double metricTimeBudget;
    public double metricTimeBudget1;
    public double metricTimeBudget01;
    public double metricTimeBudget95;
    public double metricTimeBudget99;
    
    public BugetedMetric(double metric, double metric1, double metric01, double metric95, double metric99, double metricTimeBudget, double metricTimeBudget1, double metricTimeBudget01, double metricTimeBudget95, double metricTimeBudget99) {
      this.metric = metric;
      this.metric1 = metric1;
      this.metric01 = metric01;
      this.metric95 = metric95;
      this.metric99 = metric99;
      this.metricTimeBudget = metricTimeBudget;
      this.metricTimeBudget1 = metricTimeBudget1;
      this.metricTimeBudget01 = metricTimeBudget01;
      this.metricTimeBudget95 = metricTimeBudget95;
      this.metricTimeBudget99 = metricTimeBudget99;
    }
  }
  public static value class Metric{
    public double metric;
    public double metric1;
    public double metric01;
    public double metric95;
    public double metric99;
    
    public Metric(double metric, double metric1, double metric01, double metric95, double metric99) {
      this.metric = metric;
      this.metric1 = metric1;
      this.metric01 = metric01;
      this.metric95 = metric95;
      this.metric99 = metric99;
    }
  }
}