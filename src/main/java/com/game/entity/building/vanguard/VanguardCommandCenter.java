package com.game.entity.building.vanguard;

import com.game.Init;
import com.game.entity.Entity;
import com.game.entity.EntityDimension;
import com.game.entity.EntityStats;
import com.game.entity.building.Building;
import com.game.entity.unit.UnitStats;
import com.game.input.InputType;
import com.game.screens.LoadingScreen;

import java.util.ArrayList;

import static com.game.utils.NumUtil.toScaled;

@Init(stage = 2)
public class VanguardCommandCenter extends Building {
//  private static Models model;
  private static EntityDimension entityDimension;
//  private static EnumSet<Tags> tags;
  private static UnitStats unitStats;
  private static EntityStats entityStats;
  
  
//  @Override
//  public Models getModel() {
//    return model;
//  }
  
  @Override
  protected EntityStats getEntityStats() {
    return null;
  }
  
  @Override
  protected EntityDimension getEntityDimension() {
    return null;
  }
  
//  @Override
//  public EnumSet<Tags> getTags() {
//    return null;
//  }
//
//  public VanguardCommandCenter(int x, int y, PlayerColor player/*, ModelInstance modelInstance*/) {
//    super(player/*, modelInstance*/, new EntityPosition(x, y, 0, 0));
//    hp = toScaled((float) (Math.random() * 2000));
//    effects = new ArrayList<>();
//  }
  
  public static void init(LoadingScreen loadingScreen) {
    Entity.hasCollision = true;
    Entity.validCommandTypes = new ArrayList<>();
    Entity.validCommandTypes.add(InputType.MOUSE_RELEASE);
    
//    tags = EnumSet.of(Tags.SUPER_HEAVY_ARMORED, Tags.MECHANICAL, Tags.UNMOVABLE);
    
//    model = Models.vanguardCommandCenter;
    entityDimension = new EntityDimension(100, 100, 50);
    entityStats = new EntityStats(toScaled(2000), toScaled(3));
    loadingScreen.increment();
  }
}
