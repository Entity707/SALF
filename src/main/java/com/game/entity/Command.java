package com.game.entity;


import com.game.input.InputType;
import com.game.input.Keys;


public value class Command {
  int x;
  int y;
  boolean controlPressed;
  boolean shiftPressed;
  boolean altPressed;
  boolean superPressed;
  Keys key;
  InputType inputType;
  
  public Command(Keys key, boolean controlPressed, boolean shiftPressed, boolean altPressed, boolean superPressed) {
    this.key = key;
    inputType = InputType.KEY_RELEASE;
    x = 0;
    y = 0;
    this.controlPressed = controlPressed;
    this.shiftPressed = shiftPressed;
    this.altPressed = altPressed;
    this.superPressed = superPressed;
  }
  
  public Command(InputType inputType, int x, int y, boolean controlPressed, boolean shiftPressed, boolean altPressed, boolean superPressed) {
    this.key = Keys.NONE;
    this.x = x;
    this.y = y;
    this.inputType = inputType;
    this.controlPressed = controlPressed;
    this.shiftPressed = shiftPressed;
    this.altPressed = altPressed;
    this.superPressed = superPressed;
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
  
  public boolean isAltPressed() {
    return altPressed;
  }
  
  public boolean isControlPressed() {
    return controlPressed;
  }
  
  public boolean isShiftPressed() {
    return shiftPressed;
  }
  
  public boolean isSuperPressed() {
    return superPressed;
  }
}
