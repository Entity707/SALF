package com.game.entity;


import com.game.input.InputType;

public class Command {
  int x;
  int y;
  String button;
  InputType inputType;
  
  public Command(String button) {
    this.button = button;
    inputType = InputType.KEY_RELEASE;
    x = 0;
    y = 0;
  }
  
  public Command(InputType inputType, int x, int y) {
    this.button = "";
    this.x = x;
    this.y = y;
    this.inputType = inputType;
  }
  
  public String getButton() {
    return button;
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
