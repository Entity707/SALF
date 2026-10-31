package com.game.entity.building.vanguard;

import com.game.Init;
import com.game.entity.Entity;
import com.game.entity.building.Building;
import com.game.input.InputType;
import com.game.screens.LoadingScreen;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@Init(stage = 2)
public class VanguardCommandCenter extends Building {

//
//  public VanguardCommandCenter(int x, int y, PlayerColor player/*, ModelInstance modelInstance*/) {
//    super(player/*, modelInstance*/, new EntityPosition(x, y, 0, 0));
//    hp = toScaled((float) (Math.random() * 2000));
//    effects = new ObjectArrayList<>();
//  }
  
  public static void init(LoadingScreen loadingScreen) {
    Entity.validCommandTypes = new ObjectArrayList<>();
    Entity.validCommandTypes.add(InputType.MOUSE_RELEASE);
    
//    tags = EnumSet.of(Tags.SUPER_HEAVY_ARMORED, Tags.MECHANICAL, Tags.UNMOVABLE);
    
//    model = Models.vanguardCommandCenter;
//    entityDimension = new EntityDimension(100, 100, 50);
//    entityStats = new EntityStats(toScaled(2000), toScaled(3));
    loadingScreen.increment();
  }
}
