package com.game.entity;


import com.game.input.InputType;
import com.game.input.Keys;


public value class Command {
  int x;
  int y;
  Keys key;
  InputType inputType;
  
  public Command(Keys key) {
    this.key = key;
    inputType = InputType.KEY_RELEASE;
    x = 0;
    y = 0;
  }
  
  public Command(InputType inputType, int x, int y) {
    this.key = Keys.NONE;
    this.x = x;
    this.y = y;
    this.inputType = inputType;
  }
  
  public Keys getKey() {
    return key;
  }
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
  
  public InputType getInputType() {
    return inputType;
  }
}
