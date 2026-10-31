package com.game.entity.unit.vanguard;

import com.game.Init;
import com.game.entity.EntityUpgrades;
import com.game.entity.UpgradeTree;
import com.game.entity.unit.Unit;
import com.game.input.InputType;
import com.game.screens.LoadingScreen;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@Init(stage = 2)
public class VanguardMarine extends Unit {


  
//  public VanguardMarine(int x, int y, PlayerColor player) {
//    super(player/*, Models.getModelInstance(Models.vanguardMarine)*/, new EntityPosition(x, y, 0, 0));
////    tags = EnumSet.of(Tags.LIGHT_ARMORED, Tags.BIOLOGICAL, Tags.RANGED);
//    hp = toScaled(50);
//    ticksUntilAttack = 4;
//    effects = new ObjectArrayList<>();
//    unitState = UnitState.IDLE;
//  }


  public static void init(LoadingScreen loadingScreen) {
    validCommandTypes = new ObjectArrayList<>();
    validCommandTypes.add(InputType.MOUSE_RELEASE);
//    upgrade = new Upgrade();
//    entityDimension = new EntityDimension(16, 10, 8);
////    unitStats = new UnitStats(toScaled(10), toScaled(100), toScaled(0.5f), toScaled(4));
//    entityStats = new EntityStats(toScaled(50), toScaled(1));
    loadingScreen.increment();
  }

}
