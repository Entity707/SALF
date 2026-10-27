package com.game.lwjgl;

import com.game.input.Input;
import com.game.input.InputType;
import com.game.utils.NumUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.lwjgl.glfw.GLFW;

class GlfwInput {
  private GlfwInput(){}
  
  private static int x;
  private static int y;
  
  private static boolean shiftPressed = false;
  private static boolean controlPressed = false;
  private static boolean altPressed = false;
  private static boolean superPressed = false;
  
  private static boolean isMouseIn = true;
  
  static void setInputCallbacks(LwjglData lwjglData){
    GLFW.glfwSetKeyCallback(lwjglData.windowHandle, GlfwInput::handleKey);
    GLFW.glfwSetCursorPosCallback(lwjglData.windowHandle, GlfwInput::handleMouseMove);
    GLFW.glfwSetMouseButtonCallback(lwjglData.windowHandle, GlfwInput::handleMouseClick);
    GLFW.glfwSetScrollCallback(lwjglData.windowHandle, GlfwInput::handleMouseScroll);
    GLFW.glfwSetCursorEnterCallback(lwjglData.windowHandle, GlfwInput::handleMouseEnter);
  }
  
  private static void setModifiers(int mods){
    shiftPressed = (mods >> 0x0001) == 1;
    controlPressed = (mods >> 0x0002) == 1;
    altPressed = (mods >> 0x0004) == 1;
    superPressed = (mods >> 0x0008) == 1;
  }
  
  private static void setPos(double x, double y){
    GlfwInput.x = NumUtil.toScaled(x);
    GlfwInput.y = NumUtil.toScaled(y);
  }
  
  private static void handleKey(long windowHandle, int key, int scancode, int action, int mods){
    if (!isMouseIn) return;
    setModifiers(mods);
    addInput(new Input(key, action, x, y, false, shiftPressed, controlPressed, altPressed, superPressed));
    System.out.println("key");
  }
  
  private static void handleMouseMove(long windowHandle, double x, double y){
    if (!isMouseIn) return;
    setPos(x, y);
    addInput(new Input(InputType.MOVE, GlfwInput.x, GlfwInput.y, shiftPressed, controlPressed, altPressed, superPressed));
    System.out.println("mouse move");
  }
  
  private static void handleMouseClick(long windowHandle, int button, int action, int mods){
    if (!isMouseIn) return;
    setModifiers(mods);
    addInput(new Input(button, action, x, y, true, shiftPressed, controlPressed, altPressed, superPressed));
    System.out.println("mouse click");
  }
  
  private static void handleMouseScroll(long windowHandle, double x, double y){
    if (!isMouseIn) return;
    setPos(x, y);
    addInput(new Input(InputType.SCROLL, GlfwInput.x, GlfwInput.y, shiftPressed, controlPressed, altPressed, superPressed));
    System.out.println("mouse scroll");
  }
  
  private static void handleMouseEnter(long windowHandle, boolean enter){
    isMouseIn = enter;
  }
  
  private static final int mouseMoveInputsSize = 450;
  private static final int inputsSize = 20;
  private static boolean snapshot1 = true;
  
  private static ObjectArrayList<Input> mouseMoveInputs1 = new ObjectArrayList<>(mouseMoveInputsSize);
  private static ObjectArrayList<Input> inputs1 = new ObjectArrayList<>(inputsSize);
  
  private static ObjectArrayList<Input> mouseMoveInputs2 = new ObjectArrayList<>(mouseMoveInputsSize);
  private static ObjectArrayList<Input> inputs2 = new ObjectArrayList<>(inputsSize);
  

  public static void tick(){
    snapshot1 = !snapshot1;
    if (snapshot1){
      mouseMoveInputs1.clear();
      inputs1.clear();
    } else {
      mouseMoveInputs2.clear();
      inputs2.clear();
    }
  }
  
  private static void addInput(Input input) {
    if (snapshot1){
      inputs2.add(input);
    } else {
      inputs1.add(input);
    }
  }
  

  public static ObjectArrayList<Input> getInputs(){
    if (snapshot1){
      return inputs1;
    } else {
      return inputs2;
    }
  }
  
  public static ObjectArrayList<Input> getMouseMoveInputs(){
    if (snapshot1){
      return mouseMoveInputs1;
    } else {
      return mouseMoveInputs2;
    }
  }
  
}
