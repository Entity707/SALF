package com.game.entity;


import com.game.input.InputType;

import java.util.ArrayList;

public abstract class Entity {

  protected static boolean hasCollision;
  protected static ArrayList<InputType> validCommandTypes = new ArrayList<>();
  
  protected abstract EntityStats getEntityStats();
  
  protected abstract EntityDimension getEntityDimension();
  
  
//  public void drawHeathBar(boolean isSnapshot1) {
////    if (getTags().contains(Tags.INVULNERABLE)) {
////      return;
////    }
//
//    int maxHp = getEntityStats().maxHp();
//    float scale = getEntityDimension().diameter / (NumUtil.toUnscaledFloat(maxHp) / 100f); //the width of each tick
////        System.out.println(getCollisionDiameter() + ":" + getMaxHp() + ":" + scale + ":" + ((float)getCollisionDiameter())/((float)getMaxHp()));
//
//    int color;
//    if (hp > maxHp * 0.8) {
//      color = 0x00FF00FF;
//    } else if (hp > maxHp * 0.6) {
//      color = 0x80FF00FF;
//    } else if (hp > maxHp * 0.4) {
//      color = 0xFFFF00FF;
//    } else if (hp > maxHp * 0.2) {
//      color = 0xFF8000FF;
//    } else {
//      color = 0xFF0000FF;
//    }
//    float tempX;
//    float tempY;
//    float tempLastX;
//    float tempLastY;
//    float tempHp;
//    if (isSnapshot1) {
//      tempX = NumUtil.toUnscaledFloat(snapshot1.x);
//      tempY = NumUtil.toUnscaledFloat(snapshot1.y);
//      tempLastX = NumUtil.toUnscaledFloat(snapshot1.lastX);
//      tempLastY = NumUtil.toUnscaledFloat(snapshot1.lastY);
//      tempHp = NumUtil.toUnscaledFloat(snapshot1.hp);
//    } else {
//      tempX = NumUtil.toUnscaledFloat(snapshot2.x);
//      tempY = NumUtil.toUnscaledFloat(snapshot2.y);
//      tempLastX = NumUtil.toUnscaledFloat(snapshot2.lastX);
//      tempLastY = NumUtil.toUnscaledFloat(snapshot2.lastY);
//      tempHp = NumUtil.toUnscaledFloat(snapshot2.hp);
//    }
//    float radius = getEntityDimension().radius;
//    DrawManager.Game.fillRect(tempX - radius, tempLastX - radius, tempY - radius - 7, tempLastY - radius - 7, (tempHp / 100f) * scale, 6, color);
//    DrawManager.Game.strokeRect(tempX - radius, tempLastX - radius, tempY - radius - 7, tempLastY - radius - 7, getEntityDimension().diameter, 6, 0x000000FF, 1);
//    if (scale > getEntityDimension().diameter) {
//      for (float i = scale / 4; i < getEntityDimension().diameter; i += scale / 4) {
//        DrawManager.Game.fillLineDotted(tempX + i - radius, tempLastX + i - radius, tempY - radius - 7, tempLastY - radius - 7, tempX + i - radius, tempLastX + i - radius, tempY - radius - 1, tempLastY - radius - 1, 0x000000FF, 1, 2, 2);
//      }
//    } else {
//      for (float i = scale; i < getEntityDimension().diameter; i += scale) {
//        DrawManager.Game.fillLine(tempX + i - radius, tempLastX + i - radius, tempY - radius - 7, tempLastY - radius - 7, tempX + i - radius, tempLastX + i - radius, tempY - radius - 1, tempLastY - radius - 1, 0x000000FF, 1);
//      }
//    }
//
//
//  }
}


