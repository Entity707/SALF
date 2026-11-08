package com.game.utils;

import com.game.Init;
import com.game.screens.LoadingScreen;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

@Init(stage = 0)
public class RandomUtil {
  static RandomGenerator rng;
  
  public static void init(LoadingScreen loadingScreen) {
    rng = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create();
    loadingScreen.increment();
  }
  
  public static int getInt(int min, int max){
    return rng.nextInt(min, max);
  }
  public static long getLong(long min, long max){
    return rng.nextLong(min, max);
  }
  public static float getFloat(float min, float max){
    return rng.nextFloat(min, max);
  }
  public static double getDouble(double min, double max){
    return rng.nextDouble(min, max);
  }
  public static boolean getBoolean(){
    return rng.nextBoolean();
  }
}
