package com.game.lwjgl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.nio.ShortBuffer;

public class AudioData {
  public long bgmFilePointer;
  public int channels;
  public int sampleRate;
  public ShortBuffer bgmFileBuffer;
  public ObjectArrayList<String> bgms;
  public int bgm;
  
  public float masterVolume;
  public float sfxVolume;
  public float bgmVolume;
}
