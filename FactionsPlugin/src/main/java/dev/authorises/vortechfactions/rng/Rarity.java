package dev.authorises.vortechfactions.rng;

import org.bukkit.Sound;

public enum Rarity {
    COMMON("&f&lCOMMON", "&f&l", Sound.ANVIL_USE),
    UNCOMMON("&a&lUNCOMMON", "&a&l", Sound.ANVIL_LAND),
    RARE("&b&lRARE", "&b&l", Sound.EXPLODE),
    LEGENDARY("&d&lLEGENDARY", "&d&l", Sound.LEVEL_UP),
    DIVINE("&d&l&k||| &c&lDIVINE &d&l&k||| ", "&c&l", Sound.ENDERDRAGON_DEATH);

    String name;
    String color;
    Sound sound;

    Rarity(String name, String color, Sound sound){
        this.name = name;
        this.color = color;
        this.sound = sound;
    }

    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }
}
