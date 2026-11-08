package com.game.utils;

import org.tinylog.Logger;

public class LoggerUtil {
  private LoggerUtil() {}
  
  public static void trace(String message){
    Logger.trace(message);
  }
  
  public static void debug(String message){
    Logger.debug(message);
  }
  
  public static void info(String message){
    Logger.info(message);
  }
  
  public static void info(String message, Object... args){
    Logger.info(message, args);
  }
  
  public static void warn(String message){
    Logger.warn(message);
  }
  public static void warn(String message, Object... args){
    Logger.warn(message, args);
  }
  
  public static void error(String message){
    Logger.error(message);
    throwError(new RuntimeException(message));
  }
  public static void error(Exception e){
    Logger.error(e);
    throwError(e);
  }
  
  public static void error(String message, Exception e){
    Logger.error(message, e);
    throwError(e);
  }
  public static void throwError(Exception e) {
    LoggerUtil.error(e);
  }
}
