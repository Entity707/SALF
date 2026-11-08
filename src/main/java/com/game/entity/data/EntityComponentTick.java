package com.game.entity.data;

public class EntityComponentTick {
//  switch (unitState) {
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
