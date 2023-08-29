package dev.authorises.cavelet.customitems;

import java.util.HashMap;
import java.util.Random;

public class LootContainer {

    private final WeightedRandomBag<CItemBlueprint<?>> loot;

    public LootContainer(WeightedRandomBag<CItemBlueprint<?>> loot){
        this.loot = loot;
    }

    public CItemBlueprint<?> generateItem(){
        return this.loot.getRandom();
    }
}
