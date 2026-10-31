package com.game.entity;

public value class UpgradeTree {
  private UpgradeTree parent;
  private UpgradeTree[] children;
  private Stat stat;
  private EntityUpgrades name;
  public UpgradeTree(Stat stat, EntityUpgrades name, UpgradeTree parent, UpgradeTree... children){
    this.stat = stat;
    this.parent = parent;
    this.children = children;
  }
  
  public UpgradeTree[] getChildren() {
    return children;
  }
  
  public EntityUpgrades getName() {
    return name;
  }
  
  public UpgradeTree getParent() {
    return parent;
  }
  
  public Stat getStat() {
    return stat;
  }
}
