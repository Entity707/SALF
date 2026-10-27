package com.game.entity.unit;

/**
 * @param attackSpeed in ticks
 */
public value record UnitStats(int speed, int acceleration, int turnSpeed, int damage, int attackSpeed) {
}
