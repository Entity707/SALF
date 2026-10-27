package com.game.entity.unit;

import com.game.draw.DrawManager;
import com.game.entity.Abilities;
import com.game.entity.Entity;

import java.util.ArrayList;

public abstract class Unit extends Entity {
  
  protected static ArrayList<Abilities> abilities;
  
  
  abstract protected UnitStats getUnitStats();
  
  protected int ticksUntilAttack;
  
  protected UnitState unitState;
  protected int targetX;
  protected int targetY;
  protected int targetDirection;
  
  public void setFormationIndex(int formationIndex) {
    this.formationIndex = formationIndex;
  }
  
  public void changeFormationIndex(int amount) {
    formationIndex += amount;
  }
  
  protected Entity attackTarget;
  
  public int getFormationIndex() {
    return formationIndex;
  }
  
  protected int formationIndex = 0;
  
  
//  public Unit(PlayerColor color/*, ModelInstance modelInstance*/, EntityPosition entityPosition) {
//    super(color/*, modelInstance*/, entityPosition);
//  }
  
  
  public int getTargetX() {
    return targetX;
  }
  
  public int getTargetY() {
    return targetY;
  }
  
  
  public UnitState getUnitState() {
    return unitState;
  }
  
  public void setUnitState(UnitState unitState) {
    this.unitState = unitState;
  }
  
  
  public void drawTarget() {
    if (unitState != UnitState.MOVING) {
      return;
    }
    DrawManager.Game.fillCircleScaledCull(targetX - 50000, targetX - 50000, targetY - 50000, targetY - 50000, 100000, 0x00FF00FF);
  }
  
//  @Override
  public void tick() {
//    entityPosition.tick();
//    if (!commands.isEmpty()) {
//      for (Command command : commands) {
//        if (command.getInputType() == InputType.MOUSE_RELEASE) {
//          unitState = UnitState.MOVING;
//
//          int ring = (int) StrictMath.sqrt(formationIndex);
//          int angle = (int) (NumUtil.toScaled(formationIndex * 137.507)) % NumUtil.toScaled(360);
//          int spreadRadius = getEntityDimension().radiusScaled * ring;
//          int offsetX = (int) (NumUtil.cos(angle) * spreadRadius);
//          int offsetY = (int) (NumUtil.sin(angle) * spreadRadius);
//
//          targetX = command.getX() - getEntityDimension().halfWidthScaled + offsetX;
//          targetY = command.getY() - getEntityDimension().halfWidthScaled + offsetY;
//        }
//      }
//    } else {
//      unitState = UnitState.IDLE;
//    }
//
//    switch (unitState) {
//      case MOVING:
//        int x = entityPosition.x;
//        int y = entityPosition.y;
//        int direction = entityPosition.direction;
//        targetDirection = NumUtil.atan2(targetY - y, targetX - x);
//        int delta = targetDirection - direction;
//        int scaled180 = NumUtil.toScaled(180);
//        int scaled360 = NumUtil.toScaled(360);
//        while (delta <= -scaled180) delta += scaled360;
//        while (delta > scaled180) delta -= scaled360;
////                System.out.println(delta);
//        if (StrictMath.abs(delta) <= getUnitStats().turnSpeed()) {
//          direction = targetDirection;
//        } else {
//          if (delta > 0) entityPosition.direction += getUnitStats().turnSpeed();
//          else entityPosition.direction -= getUnitStats().turnSpeed();
//          if (direction <= -scaled180) entityPosition.direction += scaled360;
//          if (direction > scaled180) entityPosition.direction -= scaled360;
//        }
//        if (direction == targetDirection) {
//          int xChange = (int) (getUnitStats().speed() * NumUtil.cos(direction));
//          int yChange = (int) (getUnitStats().speed() * NumUtil.sin(direction));
//          if (targetX > x ? x + xChange >= targetX : x + xChange <= targetX) entityPosition.x = targetX;
//          else entityPosition.x += xChange;
//          if (targetY > y ? y + yChange >= targetY : y + yChange <= targetY) entityPosition.y = targetY;
//          else entityPosition.y += yChange;
//          int dx = x - targetX;
//          int dy = y - targetY;
//          int scaledHalfWidth = getEntityDimension().halfWidthScaled;
//          if ((dx * dx) + (dy * dy) <= (scaledHalfWidth * scaledHalfWidth)) {
//            unitState = UnitState.IDLE;
//            removeCommand();
//          }
//        }
//        break;
//    }
  }
}
