package dev.authorises.vortechfactions.settings;

import org.bukkit.Material;

public enum SettingsCategory {
    ALL("&f", "All", Material.COMMAND),
    CHAT("&b", "Chat", Material.PAPER),
    ACTIONBAR("&b", "Action Bar", Material.IRON_PLATE);

    public String color;
    public String name;
    public Material displayMaterial;

    SettingsCategory(String color, String name, Material displayMaterial){
        this.color = color;
        this.name = name;
        this.displayMaterial = displayMaterial;
    }
}
