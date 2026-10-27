package com.game.entity;

public value class Stat {
  private int woodCost;
  private int stoneCost;
  private int lowGradeCost;
  private int highGradeCost;
  private int energyCost;
  private int foodCost;
  private int fuelCost;
  
  private int supplyCost;
  private int weightCost;
  private int spaceCost;
  
  
  private int maxHp;
  private int hpRegeneration;
  private int armor;
  private int speed;
  private int acceleration;
  private int turnSpeed;
  private int turnAcceleration;
  private int energy;
  private int energyRegeneration;
  private int attackSpeed;
  private int width;
  private int height;
  private int radius;
  
  
  public Stat(int maxHp, int hpRegeneration, int armor, int speed, int acceleration, int turnSpeed, int turnAcceleration,
              int energy, int energyRegeneration, int attackSpeed, int width, int height, int radius,
              int woodCost, int stoneCost, int lowGradeCost, int highGradeCost, int energyCost,
              int foodCost, int fuelCost, int supplyCost, int weightCost, int spaceCost) {
    
    this.maxHp = maxHp;
    this.hpRegeneration = hpRegeneration;
    this.armor = armor;
    this.speed = speed;
    this.acceleration = acceleration;
    this.turnSpeed = turnSpeed;
    this.turnAcceleration = turnAcceleration;
    this.energy = energy;
    this.energyRegeneration = energyRegeneration;
    this.attackSpeed = attackSpeed;
    this.width = width;
    this.height = height;
    this.radius = radius;
    
    this.woodCost = woodCost;
    this.stoneCost = stoneCost;
    this.lowGradeCost = lowGradeCost;
    this.highGradeCost = highGradeCost;
    this.energyCost = energyCost;
    this.foodCost = foodCost;
    this.fuelCost = fuelCost;
    
    this.supplyCost = supplyCost;
    this.weightCost = weightCost;
    this.spaceCost = spaceCost;
  }
}
