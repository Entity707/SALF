package com.game.entity;

import com.game.utils.NumUtil;

public class Snapshot {
  public int x;
  public int y;
  public int z;
  public int lastX;
  public int lastY;
  public int lastZ;
  public int direction;
  public int lastDirection;
  public boolean isSelected = false;
  public int hp;
  public int lerpY;
  
  public void set(Entity entity) {
    x = entity.entityPosition.x;
    y = entity.entityPosition.y;
    z = entity.entityPosition.z;
    lastX = entity.entityPosition.lastX;
    lastY = entity.entityPosition.lastY;
    lastZ = entity.entityPosition.lastZ;
    direction = entity.entityPosition.direction;
    lastDirection = entity.entityPosition.lastDirection;
    isSelected = entity.isSelected;
    hp = entity.hp;
  }
  
  public int getYLerp() {
    return lerpY;
  }
  
  public void setYLerp(float factor) {
    lerpY = NumUtil.lerp(y, lastY, factor);
  }
}
