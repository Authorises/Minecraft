package dev.authorises.cavelet.mining;

public enum OreRarity {
    DEFAULT("&7", ""),
    PROCESSED("&a", "&aProcessed&r"),
    PURE("&dPure", "&dPure&r");

    public String color;
    public String displayName;

    OreRarity(String color, String displayName){
        this.color = color;
        this.displayName = displayName;
    }
}
