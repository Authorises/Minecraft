package dev.authorises.cavelet.cthuluagent;

import dev.authorises.cavelet.customitems.CItemBlueprint;

public class TimedSale {

    public final Integer stock;
    public final CItemBlueprint<?> itemSold;
    public final Integer amountPerSale;
    public final Integer fragmentsCost;
    public final SaleRarity rarity;

    public TimedSale(Integer stock, CItemBlueprint<?> itemSold, Integer amountPerSale, Integer fragmentsCost, SaleRarity rarity) {
        this.stock = stock;
        this.itemSold = itemSold;
        this.amountPerSale = amountPerSale;
        this.fragmentsCost = fragmentsCost;
        this.rarity = rarity;
    }


}
