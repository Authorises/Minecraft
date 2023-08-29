package dev.authorises.cavelet.customenchants;

public enum CustomEnchantTier {
    BASIC("basic", "Basic", "<#05acff>", 300D),
    ADVANCED("advanced", "Advanced", "<#b2f55b>", 900D),
    ANGELIC("angelic", "Angelic", "<#d694eb>", 2300D),
    FORGE("forge", "Forge", "<#b02a23>", -1D);

    public String id;
    public String displayName;
    public String color;
    public Double darkSoulsCost;

    CustomEnchantTier(String id, String displayName, String color, Double darkSoulsCost){
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.darkSoulsCost = darkSoulsCost;
    }

    public String getColorDisplayName(){
        return this.color+this.displayName;
    }
}
