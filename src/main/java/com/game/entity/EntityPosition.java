package com.game.entity;

public class EntityPosition {
  public int x;
  public int y;
  public int z;
  public int lastX;
  public int lastY;
  public int lastZ;
  public int direction;
  public int lastDirection;
  
  public void tick() {
    lastX = x;
    lastY = y;
    lastZ = z;
    lastDirection = direction;
  }
  
  public EntityPosition(int x, int y, int z, int direction) {
    this.x = x;
    this.lastX = x;
    this.y = y;
    this.lastY = y;
    this.z = z;
    this.lastZ = z;
    this.direction = direction;
    this.lastDirection = direction;
  }
}
