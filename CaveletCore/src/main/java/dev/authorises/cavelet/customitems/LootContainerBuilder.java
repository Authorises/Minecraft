package dev.authorises.cavelet.customitems;

public class LootContainerBuilder {
    private WeightedRandomBag<CItemBlueprint<?>> loot;

    public LootContainerBuilder(){
        this.loot = new WeightedRandomBag<>();
    }

    public LootContainerBuilder addItem(CItemBlueprint<?> blueprint, Double weight){
        this.loot.addEntry(blueprint, weight);
        return this;
    }

    public LootContainer build(){
        return new LootContainer(this.loot);
    }
}
