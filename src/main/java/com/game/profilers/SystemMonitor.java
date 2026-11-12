package com.game.profilers;

import com.game.Init;
import com.game.screens.LoadingScreen;
import com.game.utils.LogType;
import com.game.utils.LoggerUtil;
import com.game.utils.PerformanceType;
import oshi.ffm.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OSProcess;

@Init(stage = 1)
public class SystemMonitor {
  private static Thread thread;
  private volatile static SystemStats systemStats;
  private static SystemInfo si;
  private static HardwareAbstractionLayer hal;
  private static Sensors sensors;
  private static CentralProcessor cpu;
  private static GlobalMemory memory;
  private static OSProcess process;
  private static Runtime runtime;
  public static SystemStats getSystemStats() {
    return systemStats;
  }
  
  public static void init(LoadingScreen loadingScreen) {
    si = new SystemInfo();
    hal = si.getHardware();
    sensors = hal.getSensors();
    cpu = hal.getProcessor();
    memory = hal.getMemory();
    process = si.getOperatingSystem().getProcess(si.getOperatingSystem().getProcessId());
    runtime = Runtime.getRuntime();
    running = true;
    systemStats = new SystemStats(0, 0, 0, 0, 0, 0, 0);
    thread = Thread.ofPlatform()
        .name("SystemMonitor")
        .daemon(true)
        .start(SystemMonitor::pollThread);
    loadingScreen.increment();
  }
  
  private static volatile boolean running;
  
  public static void cleanup() {
    running = false;
  }
  
  private static void pollThread() {
    long totalMemory = memory.getTotal();
    float cpuUsage = 0;
    int logCounter = 0;
    
    String gpuName = hal.getGraphicsCards().isEmpty()
        ? "unknown"
        : hal.getGraphicsCards().getFirst().getName();
    LoggerUtil.info("{} os:{} cpu:{} gpu:{} ram:{}", LogType.EVENT.getString(), si.getOperatingSystem(),
        cpu.getProcessorIdentifier().getName(),  gpuName, toGb(totalMemory));
    
    while (running) {
      try {
        cpuUsage = (float) (cpu.getSystemCpuLoad(1000) * 100);
        long availableMemory = memory.getAvailable();
        process.updateAttributes();
        SystemStats stats = new SystemStats(toGb(totalMemory), toGb(availableMemory), toGb(process.getResidentMemory()),
            toGb(runtime.totalMemory()-runtime.freeMemory()), toGb(totalMemory - availableMemory), cpuUsage,
            (float) sensors.getCpuTemperature());
        systemStats = stats;
        logCounter++;
        if (logCounter >= 5){
          LoggerUtil.info("{} ram total:{} ram unused:{} ram used:{} ram heap:{} ram used total:{} cpu usage:{} cpu temp:{}",
              PerformanceType.HARDWARE.getString(), stats.ramTotal, stats.ramUnused, stats.ramUsed, stats.ramHeap, stats.ramUsedTotal,
              stats.cpuUsage, stats.cpuTemp);
          logCounter = 0;
        }//TODO: log if there is a spike in a metric maybe
      } catch (Exception e) {
        LoggerUtil.error(e);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ex) {
          LoggerUtil.error(ex);
        }
      }
    }
    
  }
  
  public static int getPhysicalThreadCount(){
    return cpu.getPhysicalProcessorCount();
  }
  
  public static int getLogicThreadCount(){
    return cpu.getLogicalProcessorCount();
  }
  
  private static float toGb(long bytes) {
    return (float) (bytes / 1073741824d);
  }
  
  public value record SystemStats(float ramTotal, float ramUnused, float ramUsed, float ramHeap, float ramUsedTotal, float cpuUsage,
                                  float cpuTemp) {
  }
}
