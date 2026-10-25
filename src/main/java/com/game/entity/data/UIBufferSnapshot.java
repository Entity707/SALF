package com.game.entity.data;

import com.game.Init;
import com.game.screens.LoadingScreen;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.Object2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@Init(stage = 2)
public class UIBufferSnapshot {
  static Char2IntOpenHashMap charBits;//TODO:generate this on startup
  static Object2ObjectOpenHashMap<String, int[]> stringHashMap;
  static Object2ObjectFunction<String, int[]> intsGenerator = (string->{
    return new int[3];
  });//TODO: make sure this works and make it actaully computes the ints
  static int[] getLongs(String string){
    return stringHashMap.computeIfAbsent(string, intsGenerator);
  }
  public static void init(LoadingScreen loadingScreen){
    //generate charBits here;
  }
  //43 letters 6 bits per letter
  IntArrayList UIX;//13 bits of x when text
  IntArrayList UIY;//same for y
  IntArrayList UIData;
  IntArrayList UIData2;
  IntArrayList UIType;//last 4 bits is aligment type
  //could maybe store some data in here for strings but its fine.
  
  UIBufferSnapshot(){
    UIX = new IntArrayList();
    UIY = new IntArrayList();
    UIData = new IntArrayList();
    UIData2 = new IntArrayList();
    UIType = new IntArrayList();
  }
  //rect
  void addRectangle(int x, int y, int width, int height){
    UIX.add(x);
    UIY.add(y);
    UIData.add(width);
    UIData2.add(height);
    UIType.add(0);
  }
  
  //circle
  void addCicle(int x, int y, int radius){
    UIX.add(x);
    UIY.add(y);
    UIData.add(radius);
    UIData2.add(-1);
    UIType.add(1);
  }
  
  //string
  void addText(int x, int y, String string){
    UIX.add(x);
    UIY.add(y);
    int[] value = getLongs(string);
    UIData.add(value[0]);
    UIData2.add(value[1]);
    UIType.add((int)value[2]);//TODO: or the last 4 aligment bits onto the int
  }
}
