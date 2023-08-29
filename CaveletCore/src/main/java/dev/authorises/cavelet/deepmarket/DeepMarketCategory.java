package dev.authorises.cavelet.deepmarket;

import org.bukkit.Material;

public enum DeepMarketCategory {
    MOB_DROPS("MOB_DROPS", "Mob Drops", "<#c22d5c>", Material.BONE),
    BLOCKS("BLOCKS", "Blocks", "<#3b2dcf>", Material.BRICKS)
    ;

    private String id;
    private String displayName;
    private String color;
    private Material item;

    DeepMarketCategory(String id, String displayName, String color, Material item) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.item = item;
    }
}
