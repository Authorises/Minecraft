package dev.authorises.cavelet.newshop;

import org.bukkit.Material;

public enum ShopCategory {
    COMBAT("&c", "Combat", Material.DIAMOND_SWORD),
    BASE("&b", "Base", Material.WATER_BUCKET),
    SPAWNER("&d", "Spawner", Material.SPAWNER),
    FARMING("&a", "Farming", Material.WHEAT),
    TOOLS("&8", "Tools", Material.IRON_AXE),
    MISC("&f", "Misc", Material.FISHING_ROD),
    ALL("&b", "All", Material.CAULDRON);

    public String color;
    public String name;
    public Material item;

    ShopCategory(String color, String name, Material item){
        this.color = color;
        this.name = name;
        this.item = item;
    }
}