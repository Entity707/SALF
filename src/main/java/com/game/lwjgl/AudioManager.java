package com.game.lwjgl;

import com.game.Init;
import com.game.screens.LoadingScreen;
import com.game.utils.LoggerUtil;
import com.game.utils.RandomUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jctools.queues.MpscArrayQueue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Init(stage=1)
public class AudioManager {
  private static AudioManager instance;
  private AudioData audioData;
  private MpscArrayQueue<AudioCommand> commandQueue;
  private Thread audioThread;
  
  private volatile boolean shutdown = false;
  public static AudioManager getInstance() {
    return instance;
  }
  
  public static void init(LoadingScreen loadingScreen) {
    instance = new AudioManager();
    loadingScreen.increment();
  }
  private AudioManager() {
    audioData = new AudioData();
    commandQueue = new MpscArrayQueue<>(1024);//no idea if this is too big or too small
  }
  
  public void shutdown(){
    shutdown = true;
    try {
      audioThread.join();
    } catch (InterruptedException e) {
      LoggerUtil.error(e);
    }
  }
  
  public void submitCommand(AudioCommand command){
    commandQueue.offer(command);
  }
  
  public void startAudioThread(){
    if (audioThread != null) return;
    //TODO:setup openAL
    getBGMS(audioData);
    pickRandomBGM(audioData);
    audioThread = new Thread(this::audioThread);
    audioThread.start();
  }
  
  private void audioThread(){
    while(!shutdown){//TODO:choose a bgm and start streaming it
      int drained = commandQueue.drain(this::processCommand);
      if (drained == 0) {
        Thread.onSpinWait();
      }
    }
  }
  
  private void processCommand(AudioCommand command){
    switch (command.getCommandType()){
      case CHANGE_BGM -> {
        pickRandomBGM(audioData);
      }
      case PLAY_SOUND -> {
      
      }
      case CHANGE_MASTER_VOLUME ->  {
        audioData.masterVolume+=command.getAudioChange();
      }
      case CHANGE_SFX_VOLUME -> {
        audioData.sfxVolume+=command.getAudioChange();
      }
      case CHANGE_BGM_VOLUME -> {
        audioData.bgmVolume+=command.getAudioChange();
      }
      case SET_MASTER_VOLUME ->  {
        audioData.masterVolume=command.getAudioChange();
      }
      case SET_SFX_VOLUME -> {
        audioData.sfxVolume=command.getAudioChange();
      }
      case SET_BGM_VOLUME ->  {
        audioData.bgmVolume=command.getAudioChange();
      }
    }
  }
  
  private void getBGMS(AudioData audioData){
    ObjectArrayList<String> bgms = new ObjectArrayList<>();
    try {
      Files.walk(Paths.get("resources/sounds/bgm/")).forEach(path->{
        bgms.add(path.toString());
      });
    } catch (IOException e) {
      LoggerUtil.error(e);
    }
    audioData.bgms = bgms;
  }
  private void pickRandomBGM(AudioData audioData) {
    audioData.bgm = RandomUtil.getInt(0, audioData.bgms.size());
  }
}
