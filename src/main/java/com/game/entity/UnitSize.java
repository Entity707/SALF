package com.game.entity;

import com.game.utils.NumUtil;

public value class UnitSize {
  private final float width;
  private final float height;
  private final float halfWidth;
  private final float halfHeight;
  private final int scaledWidth;
  private final int scaledHeight;
  private final int scaledHalfWidth;
  private final int scaledHalfHeight;
  private final float boundingRadius;
  private final float boundingDiameter;
  private final int boundingRadiusScaled;
  private final int boundingDiameterScaled;
  
  public UnitSize(float width, float height, float radius) {
    this.width = width;
    halfWidth = width / 2;
    scaledWidth = NumUtil.toScaled(width);
    scaledHalfWidth = NumUtil.toScaled(width / 2);
    this.height = height;
    halfHeight = height / 2;
    scaledHeight = NumUtil.toScaled(height);
    scaledHalfHeight = NumUtil.toScaled(height / 2);
    this.boundingRadius = radius;
    boundingDiameter = radius * 2;
    boundingRadiusScaled = NumUtil.toScaled(radius);
    boundingDiameterScaled = NumUtil.toScaled(radius * 2);
  }
  
  public int getBoundingDiameterScaled() {
    return boundingDiameterScaled;
  }
  
  public int getBoundingRadiusScaled() {
    return boundingRadiusScaled;
  }
  
  public float getBoundingDiameter() {
    return boundingDiameter;
  }
  
  public float getBoundingRadius() {
    return boundingRadius;
  }
  
  public int getScaledHalfHeight() {
    return scaledHalfHeight;
  }
  
  public int getScaledHalfWidth() {
    return scaledHalfWidth;
  }
  
  public int getScaledHeight() {
    return scaledHeight;
  }
  
  public int getScaledWidth() {
    return scaledWidth;
  }
  
  public float getHalfHeight() {
    return halfHeight;
  }
  
  public float getHalfWidth() {
    return halfWidth;
  }
  
  public float getHeight() {
    return height;
  }
  
  public float getWidth() {
    return width;
  }
  
}
