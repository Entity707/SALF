package com.game.lwjgl;

public value class AudioCommand {
  private AudioCommandTypes commandType;
  private float audioChange;
  
  //for audio
  public AudioCommand(AudioCommandTypes commandType, float audioChange) {
    this.commandType = commandType;
    this.audioChange = audioChange;
  }
  
  //for normal
  public AudioCommand(AudioCommandTypes commandType) {
    this.commandType = commandType;
    this.audioChange = 0;
  }
  
  
  public AudioCommandTypes getCommandType() {
    return commandType;
  }
  
  public float getAudioChange() {
    return audioChange;
  }
}
