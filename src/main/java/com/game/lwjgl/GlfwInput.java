package com.game.lwjgl;

import org.lwjgl.glfw.GLFW;

class GlfwInput {
  private GlfwInput(){}
  private static long x;
  private static long y;
  
  private static boolean shiftPressed = false;
  private static boolean controlPressed = false;
  private static boolean altPressed = false;
  private static boolean superPressed = false;
  
  static void setInputCallbacks(LwjglData lwjglData){
    GLFW.glfwSetKeyCallback(lwjglData.windowHandle, GlfwInput::handleKey);
    GLFW.glfwSetCursorPosCallback(lwjglData.windowHandle, GlfwInput::handleMouseMove);
    GLFW.glfwSetMouseButtonCallback(lwjglData.windowHandle, GlfwInput::handleMouseClick);
    GLFW.glfwSetScrollCallback(lwjglData.windowHandle, GlfwInput::handleMouseScroll);
  }
  
  private static void setModifiers(int mods){
    shiftPressed = (mods >> 0x0001) == 1;
    controlPressed = (mods >> 0x0002) == 1;
    altPressed = (mods >> 0x0004) == 1;
    superPressed = (mods >> 0x0008) == 1;
  }
  
  private static void handleKey(long windowHandle, int key, int scancode, int action, int mods){
    setModifiers(mods);
  }
  
  private static void handleMouseMove(long windowHandle, double x, double y){
  
  }
  
  private static void handleMouseClick(long windowHandle, int button, int action, int mods){
    setModifiers(mods);
  
  }
  
  private static void handleMouseScroll(long windowHandle, double x, double y){
  
  }
  
}
