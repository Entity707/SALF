package com.game.lwjgl;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

class RenderData {
  final ObjectArrayList<Source2D> extra2D = new ObjectArrayList<>();
  volatile LongArrayList UIX = new LongArrayList();
  volatile LongArrayList UIY = new LongArrayList();
  volatile LongArrayList UIData = new LongArrayList();
  volatile LongArrayList UIData2 = new LongArrayList();
  volatile IntArrayList UIType = new IntArrayList();
  volatile IntArrayList UIColor = new IntArrayList();
  volatile LongArrayList entityX = new LongArrayList();
  volatile LongArrayList entityLastX = new LongArrayList();
  volatile LongArrayList entityY = new LongArrayList();
  volatile LongArrayList entityLastY = new LongArrayList();
  volatile LongArrayList entityRadius = new LongArrayList();
  volatile IntArrayList activeIndex = new IntArrayList();
  
  volatile float alpha;
  volatile Long2ObjectOpenHashMap<String> textMap = new Long2ObjectOpenHashMap<>();
  
  volatile LongArrayList entityZ;
  volatile LongArrayList entityLastZ;
  volatile LongArrayList entityDirection;
  volatile LongArrayList entityLastDirection;
  volatile LongArrayList hp;
  
  volatile IntArrayList activeSelectedIndex;
  volatile LongArrayList entityModel;
  
}
