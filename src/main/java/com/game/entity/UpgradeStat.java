package com.game.entity;

public value class UpgradeStat {
  private int woodCost;
  private int stoneCost;
  private int lowGradeCost;
  private int highGradeCost;
  private int energyCost;
  private int foodCost;
  
  private int upgradeWoodCost;
  private int upgradeStoneCost;
  private int upgradeLowGradeCost;
  private int upgradeHighGradeCost;
  private int upgradeEnergyCost;
  private int upgradeFoodCost;
  
  private int upgradeTime;
  
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
  private int damage;
  public UpgradeStat(int maxHp, int hpRegeneration, int armor, int speed, int acceleration, int turnSpeed,
                     int turnAcceleration, int energy, int energyRegeneration, int attackSpeed, int damage, int upgradeTime,
                     int woodCost, int stoneCost, int lowGradeCost, int highGradeCost, int energyCost, int foodCost,
                     int upgradeWoodCost, int upgradeStoneCost, int upgradeLowGradeCost, int upgradeHighGradeCost,
                     int upgradeEnergyCost, int upgradeFoodCost) {
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
    this.damage = damage;
    
    this.upgradeTime = upgradeTime;
    
    this.woodCost = woodCost;
    this.stoneCost = stoneCost;
    this.lowGradeCost = lowGradeCost;
    this.highGradeCost = highGradeCost;
    this.energyCost = energyCost;
    this.foodCost = foodCost;
    
    this.upgradeWoodCost = upgradeWoodCost;
    this.upgradeStoneCost = upgradeStoneCost;
    this.upgradeLowGradeCost = upgradeLowGradeCost;
    this.upgradeHighGradeCost = upgradeHighGradeCost;
    this.upgradeEnergyCost = upgradeEnergyCost;
    this.upgradeFoodCost = upgradeFoodCost;
  }
  static class Builder{
    private int woodCost = 0;
    private int stoneCost = 0;
    private int lowGradeCost = 0;
    private int highGradeCost = 0;
    private int energyCost = 0;
    private int foodCost = 0;
    
    private int upgradeWoodCost = 0;
    private int upgradeStoneCost = 0;
    private int upgradeLowGradeCost = 0;
    private int upgradeHighGradeCost = 0;
    private int upgradeEnergyCost = 0;
    private int upgradeFoodCost = 0;
    
    
    private final int maxHp;
    private final int hpRegeneration;
    private final int armor;
    private final int speed;
    private final int acceleration;
    private final int turnSpeed;
    private final int turnAcceleration;
    private final int energy;
    private final int energyRegeneration;
    private final int attackSpeed;
    private final int damage;
    private final int upgradeTime;
    
    public Builder(int maxHp, int hpRegeneration, int armor, int speed, int acceleration, int turnSpeed,
                   int turnAcceleration,  int energy, int energyRegeneration, int attackSpeed, int damage, int upgradeTime) {
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
      this.damage = damage;
      this.upgradeTime = upgradeTime;
    }
    public UpgradeStat build(){
      return new UpgradeStat(maxHp, hpRegeneration, armor, speed, acceleration, turnSpeed, turnAcceleration,
          energy, energyRegeneration, attackSpeed, damage, upgradeTime, woodCost, stoneCost, lowGradeCost, highGradeCost,
          energyCost, foodCost, upgradeWoodCost, upgradeStoneCost, upgradeLowGradeCost, upgradeHighGradeCost,
          upgradeEnergyCost, upgradeFoodCost);
    }
    
    public Builder setEnergyCost(int energyCost) {
      this.energyCost = energyCost;
      return this;
    }
    
    public Builder setFoodCost(int foodCost) {
      this.foodCost = foodCost;
      return this;
    }
    
    public Builder setHighGradeCost(int highGradeCost) {
      this.highGradeCost = highGradeCost;
      return this;
    }
    
    public Builder setLowGradeCost(int lowGradeCost) {
      this.lowGradeCost = lowGradeCost;
      return this;
    }
    
    public Builder setStoneCost(int stoneCost) {
      this.stoneCost = stoneCost;
      return this;
    }
    
    public Builder setWoodCost(int woodCost) {
      this.woodCost = woodCost;
      return this;
    }
    
    public Builder setUpgradeEnergyCost(int upgradeEnergyCost) {
      this.upgradeEnergyCost = upgradeEnergyCost;
      return this;
    }
    
    public Builder setUpgradeFoodCost(int upgradeFoodCost) {
      this.upgradeFoodCost = upgradeFoodCost;
      return this;
    }
    
    public Builder setUpgradeHighGradeCost(int upgradeHighGradeCost) {
      this.upgradeHighGradeCost = upgradeHighGradeCost;
      return this;
    }
    
    public Builder setUpgradeLowGradeCost(int upgradeLowGradeCost) {
      this.upgradeLowGradeCost = upgradeLowGradeCost;
      return this;
    }
    
    public Builder setUpgradeStoneCost(int upgradeStoneCost) {
      this.upgradeStoneCost = upgradeStoneCost;
      return this;
    }
    
    public Builder setUpgradeWoodCost(int upgradeWoodCost) {
      this.upgradeWoodCost = upgradeWoodCost;
      return this;
    }
  }
  
  public int getAcceleration() {
    return acceleration;
  }
  
  public int getArmor() {
    return armor;
  }
  
  public int getAttackSpeed() {
    return attackSpeed;
  }
  
  public int getDamage() {
    return damage;
  }
  
  public int getEnergy() {
    return energy;
  }
  
  public int getEnergyCost() {
    return energyCost;
  }
  
  public int getEnergyRegeneration() {
    return energyRegeneration;
  }
  
  public int getFoodCost() {
    return foodCost;
  }
  
  public int getHighGradeCost() {
    return highGradeCost;
  }
  
  public int getHpRegeneration() {
    return hpRegeneration;
  }
  
  public int getLowGradeCost() {
    return lowGradeCost;
  }
  
  public int getMaxHp() {
    return maxHp;
  }
  
  public int getSpeed() {
    return speed;
  }
  
  public int getStoneCost() {
    return stoneCost;
  }
  
  public int getTurnAcceleration() {
    return turnAcceleration;
  }
  
  public int getTurnSpeed() {
    return turnSpeed;
  }
  
  public int getWoodCost() {
    return woodCost;
  }
  
  public int getUpgradeEnergyCost() {
    return upgradeEnergyCost;
  }
  
  public int getUpgradeFoodCost() {
    return upgradeFoodCost;
  }
  
  public int getUpgradeHighGradeCost() {
    return upgradeHighGradeCost;
  }
  
  public int getUpgradeLowGradeCost() {
    return upgradeLowGradeCost;
  }
  
  public int getUpgradeStoneCost() {
    return upgradeStoneCost;
  }
  
  public int getUpgradeTime() {
    return upgradeTime;
  }
  
  public int getUpgradeWoodCost() {
    return upgradeWoodCost;
  }
}
