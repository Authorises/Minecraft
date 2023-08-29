package dev.authorises.vortechfactions.shop;

import org.bukkit.Material;

public enum ShopCategory {
    RAID("&c", "Raid", Material.TNT),
    BASE("&b", "Base", Material.WATER_BUCKET),
    SPAWNER("&d", "Spawner", Material.MOB_SPAWNER),
    FARMING("&a", "Farming", Material.SUGAR_CANE),
    MISC("&f", "Misc", Material.FISHING_ROD),
    ALL("&b", "All", Material.CAULDRON_ITEM);

    public String color;
    public String name;
    public Material item;

    ShopCategory(String color, String name, Material item){
        this.color = color;
        this.name = name;
        this.item = item;
    }
}
