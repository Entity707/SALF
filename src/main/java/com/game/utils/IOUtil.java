package com.game.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.File;

public class IOUtil {
  private IOUtil() {}
  
  public static ObjectArrayList<File> getFiles(String path){
    File[] file = new File(path).listFiles();
    if (file == null) return new ObjectArrayList<>();
    return new ObjectArrayList<>(file);
  }
}
