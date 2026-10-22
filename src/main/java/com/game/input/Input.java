package com.game.input;


public class Input {
  private final InputType inputType;
  private final Actions action;
  private final Keys key;
  
  private final long x;
  private final long y;
  
  private final boolean shiftPressed;
  private final boolean controlPressed;
  private final boolean altPressed;
  private final boolean superPressed;
  
  
  public Input(int key, int action, long x, long y, boolean isMouse, boolean shiftPressed, boolean controlPressed, boolean altPressed, boolean superPressed) {
    this.key = Keys.getKey(key);
    this.action = Actions.getAction(this.key);
    
    this.x = x;
    this.y = y;
    
    this.inputType = InputType.getInputType(action, isMouse);
    
    this.shiftPressed = shiftPressed;
    this.controlPressed = controlPressed;
    this.altPressed = altPressed;
    this.superPressed = superPressed;
  }
  
  public Input(InputType inputType, long x, long y, boolean shiftPressed, boolean controlPressed, boolean altPressed, boolean superPressed) {
    this.key = Keys.NONE;
    this.action = Actions.NONE;
    
    this.x = x;
    this.y = y;
    
    this.inputType = inputType;
    
    this.shiftPressed = shiftPressed;
    this.controlPressed = controlPressed;
    this.altPressed = altPressed;
    this.superPressed = superPressed;
  }
  
  public Keys getKey() {
    return key;
  }
  
  public Actions getAction() {
    return action;
  }
  
  public InputType getInputType() {
    return inputType;
  }
  
  public long getX() {
    return x;
  }
  
  public long getY() {
    return y;
  }
  
  public boolean isShiftPressed() {
    return shiftPressed;
  }
  
  public boolean isControlPressed() {
    return controlPressed;
  }
  
  public boolean isAltPressed() {
    return altPressed;
  }
  
  public boolean isSuperPressed() {
    return superPressed;
  }
}
