package com.game.utils;

import com.sun.management.HotSpotDiagnosticMXBean;

import java.lang.management.ManagementFactory;

public class FlagUtil {
  private FlagUtil(){}
  public static void setZGCIntensity(int intensity){
    if (intensity < 1) return;
    HotSpotDiagnosticMXBean mxBean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
    mxBean.setVMOption("ZGCIntensity", Double.toString(intensity));
  }
}
