package com.game.entity.upgrade;

public value class Upgrades {
  private UpgradeTree tree;
  private Upgrade[] upgrades;
  public Upgrades(UpgradeTree tree, Upgrade[] upgrades) {
    this.tree = tree;
    this.upgrades = upgrades;
  }
}
