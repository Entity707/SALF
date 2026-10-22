package com.game.input;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class TickInputs {
  final int mouseMoveInputsSize = 450;
  final int inputsSize = 20;
  boolean snapshot1 = true;
  
  int inputs1Size = 0;
  int inputs2Size = 0;
  
  int mouseMoveInputs1Size = 0;
  int mouseMoveInputs2Size = 0;
  
  ObjectArrayList<Input> mouseMoveInputs1 = new ObjectArrayList<>(mouseMoveInputsSize);
  ObjectArrayList<Input> inputs1 = new ObjectArrayList<>(inputsSize);
  
  ObjectArrayList<Input> mouseMoveInputs2 = new ObjectArrayList<>(mouseMoveInputsSize);
  ObjectArrayList<Input> inputs2 = new ObjectArrayList<>(inputsSize);
  
  public void tick(){
    snapshot1 = !snapshot1;
    if (snapshot1){
      mouseMoveInputs1.clear();
      inputs1.clear();
      inputs1Size = 0;
      mouseMoveInputs1Size = 0;
    } else {
      mouseMoveInputs2.clear();
      inputs2.clear();
      inputs2Size = 0;
      mouseMoveInputs2Size = 0;
    }
  }
  
  public void addInput(Input input) {
    if (snapshot1){
      inputs2.set(inputs2Size, input);
      inputs2Size++;
    } else {
      inputs1.set(inputs1Size, input);
      inputs1Size++;
    }
  }
  
  public ObjectArrayList<Input> getInputs(){
    if (snapshot1){
      return inputs1;
    } else {
      return inputs2;
    }
  }
  
  public ObjectArrayList<Input> getMouseMoveInputs(){
    if (snapshot1){
      return mouseMoveInputs1;
    } else {
      return mouseMoveInputs2;
    }
  }
  
}
