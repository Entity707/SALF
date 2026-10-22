package com.game.input;

import org.lwjgl.glfw.GLFW;

public enum InputType {
  MOUSE_PRESS(),
  MOUSE_RELEASE(),
  MOVE(),
  SCROLL(),
  KEY_PRESS(),
  KEY_RELEASE(),
  KEY_REPEAT();
  public static InputType getInputType(int action, boolean isMouse) {
    if (isMouse) {
      if (action == GLFW.GLFW_PRESS) {
        return MOUSE_PRESS;
      } else {
        return MOUSE_RELEASE;
      }
    } else {
      if (action == GLFW.GLFW_PRESS) {
        return KEY_PRESS;
      } else if (action == GLFW.GLFW_RELEASE) {
        return KEY_RELEASE;
      } else {
        return KEY_REPEAT;
      }
    }
  }
}
