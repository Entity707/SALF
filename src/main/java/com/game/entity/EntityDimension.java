package com.game.entity;

import com.game.utils.NumUtil;

public value class EntityDimension {
  public final int widthScaled;
  public final int heightScaled;
  public final int halfWidthScaled;
  public final int halfHeightScaled;
  public final int radiusScaled;
  public final int diameterScaled;
  public final float width;
  public final float height;
  public final float halfWidth;
  public final float halfHeight;
  public final float radius;
  public final float diameter;
  
  public EntityDimension(float width, float height, float radius) {
    widthScaled = NumUtil.toScaled(width);
    heightScaled = NumUtil.toScaled(height);
    halfWidthScaled = NumUtil.toScaled(width / 2);
    halfHeightScaled = NumUtil.toScaled(height / 2);
    radiusScaled = NumUtil.toScaled(radius);
    diameterScaled = NumUtil.toScaled(radius * 2);
    this.width = width;
    this.height = height;
    halfWidth = width / 2;
    halfHeight = height;
    this.radius = radius;
    diameter = radius * 2;
    
  }
}
