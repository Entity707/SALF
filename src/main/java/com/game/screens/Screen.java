package com.game.screens;

public abstract class Screen {
  private boolean exit = false;
  private boolean loaded = false;
  protected void load(){
    loaded = true;
  }
  protected void exit(){
    exit = true;
  }
  public boolean isExit(){
    return exit;
  }
  public boolean isLoaded(){
    return loaded;
  }
  public abstract void draw();
  public abstract void tick();
  
}
