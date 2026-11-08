package com.game.entity.upgrade;

public value class UpgradeTree {
  //array of upgrades, each one has index of children + parents
  int currentUpgrade;
  private Upgrade[] upgrade;
  public Upgrade getUpgrade(){
    return upgrade[currentUpgrade];
  }
  public UpgradeTree(String json){
    currentUpgrade = 0;
    upgrade = new Upgrade[0];
  }
}
