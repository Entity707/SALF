package com.game.entity.unit;

/**
 * @param attackSpeed in ticks
 */
public record UnitStats(int speed, int turnSpeed, int damage, int attackSpeed) {
}
