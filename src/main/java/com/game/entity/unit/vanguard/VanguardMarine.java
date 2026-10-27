package com.game.entity.unit.vanguard;

import com.game.Init;
import com.game.entity.EntityDimension;
import com.game.entity.EntityStats;
import com.game.entity.unit.Unit;
import com.game.entity.unit.UnitStats;
import com.game.input.InputType;
import com.game.screens.LoadingScreen;

import java.util.ArrayList;

import static com.game.utils.NumUtil.toScaled;

@Init(stage = 2)
public class VanguardMarine extends Unit {
//  private static Models model;
  private static EntityDimension entityDimension;
//  private static EnumSet<Tags> tags;
  private static UnitStats unitStats;
  private static EntityStats entityStats;
  
  @Override
  protected UnitStats getUnitStats() {
    return unitStats;
  }
  
  @Override
  protected EntityStats getEntityStats() {
    return entityStats;
  }
  
//  @Override
//  public EnumSet<Tags> getTags() {
//    return tags;
//  }
  
  
  @Override
  protected EntityDimension getEntityDimension() {
    return entityDimension;
  }
  
//  @Override
//  public Models getModel() {
//    return model;
//  }
  
//  public VanguardMarine(int x, int y, PlayerColor player) {
//    super(player/*, Models.getModelInstance(Models.vanguardMarine)*/, new EntityPosition(x, y, 0, 0));
////    tags = EnumSet.of(Tags.LIGHT_ARMORED, Tags.BIOLOGICAL, Tags.RANGED);
//    hp = toScaled(50);
//    ticksUntilAttack = 4;
//    effects = new ArrayList<>();
//    unitState = UnitState.IDLE;
//  }
  
  
  public static void init(LoadingScreen loadingScreen) {
    hasCollision = true;
    validCommandTypes = new ArrayList<>();
    validCommandTypes.add(InputType.MOUSE_RELEASE);
    
//    model = Models.vanguardMarine;
    entityDimension = new EntityDimension(16, 10, 8);
//    unitStats = new UnitStats(toScaled(10), toScaled(100), toScaled(0.5f), toScaled(4));
    entityStats = new EntityStats(toScaled(50), toScaled(1));
    loadingScreen.increment();
  }
  
}
