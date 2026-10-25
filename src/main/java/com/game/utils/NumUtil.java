package com.game.utils;

import com.game.Init;
import com.game.screens.LoadingScreen;


@Init(stage = 0)
public class NumUtil {
  private NumUtil() {}
  //22.10 system
  public static final int SHIFT = 10;
  public static final int ONE = 1 << SHIFT; // 1024
  public static final int SIN_MASK = ONE-1;
  public static final int QUARTER = ONE >> 2;
  public static final int HALF = ONE >> 1;  // 512 (used for rounding)
  private static final int[] SIN_TABLE = new int[ONE];
  private static final int[] ATAN_TABLE = new int[513];
  
  public static void init(LoadingScreen loadingScreen) {
    for (int i = 0; i < ONE; i++) {
      double angle = (i * 2.0 * StrictMath.PI) / ONE;
      SIN_TABLE[i] = (int) StrictMath.round(Math.sin(angle) * ONE);
    }
    for (int i = 0; i <= 512; i++) {
      double ratio = i / 512.0;
      double rad = StrictMath.atan(ratio);
      ATAN_TABLE[i] = (int) StrictMath.round((rad / (2.0 * StrictMath.PI)) * ONE);
    }
    loadingScreen.increment();
  }
  
  
  public static int toScaled(double value) {
    return (int)StrictMath.round(value * ONE);
  }
  
  public static int toScaled(float value) {
    return StrictMath.round(value * ONE);
  }
  
  public static int toScaled(int value) {
    return value << SHIFT;
  }
  
  public static float toUnscaledFloat(int value) {
    return (float) value / ONE;
  }
  
  public static int toUnscaledIntRound(int value) {
    return (value + HALF) >> SHIFT;
  }
  
  public static int toUnscaledIntFloor(int value) {
    return value >> SHIFT;
  }
  
  public static int toFloor(int value) {
    return value & ~SIN_MASK;
  }
  
  public static int toCeil(int value) {
    return (value + SIN_MASK) & ~SIN_MASK;
  }
  
  public static int multiply(int a, int b) {
    long product = (long) a * b;
    return (int) ((product + HALF) >> SHIFT);
  }
  
  public static int divide(int value, int divisor) {
    long scaled = ((long) value << SHIFT);
    long halfDenominator = divisor >> 1;
    return (int) ((scaled + (scaled >= 0 ? halfDenominator : -halfDenominator)) / divisor);
  }
  
  public static int sqrt(int value) {
    if (value <= 0) return 0;
    return (int) (Math.sqrt((double) value * ONE) + 0.5);
  }
  
  public static int sin(int angle) {
    return SIN_TABLE[angle & SIN_MASK];
  }
  
  public static int cos(int angle) {
    return SIN_TABLE[(angle + QUARTER) & SIN_MASK];
  }
  
  public static int atan2(int y, int x) {
    if (x == 0 && y == 0) return 0;
    
    int absX = x < 0 ? -x : x;
    int absY = y < 0 ? -y : y;
    
    boolean flip = absY > absX;
    int num = flip ? absX : absY;
    int den = flip ? absY : absX;
    
    int lutIndex = (int) ((((long) num << 9) + (den >> 1)) / den);
    int angle = ATAN_TABLE[lutIndex];
    
    if (flip) angle = QUARTER - angle;
    
    if (x < 0) {
      if (y >= 0) angle = HALF - angle;
      else angle = HALF + angle;
    } else if (y < 0) {
      angle = ONE - angle;
    }
    
    return angle & SIN_MASK;
  }
  
  /**
   * <b>DO NOT USE FOR ANGLES, USE lerpAngle FOR ANGLES</b>
   */
  public static int lerp(int start, int end, float alpha) {
    return start + multiply(end - start, toScaled(alpha));
  }
  
  /**
   * <b>DO NOT USE FOR NON ANGLES, USE lerp FOR NON ANGLES</b>
   */
  public static int lerpAngle(int startAngle, int endAngle, float alpha) {
    int diff = (endAngle - startAngle) & SIN_MASK;
    if (diff > HALF) diff -= ONE;
    return (startAngle + multiply(diff, toScaled(alpha))) & SIN_MASK;
  }
  
  /**
   * <b>DO NOT USE FOR ANGLES, USE lerpAngle FOR ANGLES</b>
   */
  public static float Unscaledlerp(float start, float end, float alpha) {
    return start + (end - start)* alpha;
  }
  
  /**
   * <b>DO NOT USE FOR NON ANGLES, USE lerp FOR NON ANGLES</b>
   */
  public static float UnscaledLerpAngle(int start, int end, float alpha) {
    float diff = (end - start) % 360.0f;
    
    if (diff > 180.0f) {
      diff -= 360.0f;
    } else if (diff < -180.0f) {
      diff += 360.0f;
    }
    
    return start + diff * alpha;
  }
}
