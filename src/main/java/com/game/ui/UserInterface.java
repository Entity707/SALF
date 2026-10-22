package com.game.ui;

public abstract class UserInterface {
  protected UserInterfaceState state;
  public abstract void draw();
  public abstract void tick();
  public UserInterfaceState getState(){
    return state;
  }
  public void setState(UserInterfaceState state){
    this.state = state;
  }
}
