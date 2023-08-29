package dev.authorises.cavelet.customenchants;

import org.bukkit.enchantments.Enchantment;

public enum CustomEnchantType {
    PROTECTION(5, "protection", "Protection", CustomEnchantTier.BASIC, "Reduces environmental damage", Enchantment.PROTECTION_ENVIRONMENTAL),
    FIRE_PROTECTION(5, "fireprotection", "Fire Protection", CustomEnchantTier.BASIC, "Reduces fire damage",  Enchantment.PROTECTION_FIRE),
    DEPTH_STRIDER(4, "depthstrider", "Depth Strider", CustomEnchantTier.BASIC, "Move faster in water", Enchantment.DEPTH_STRIDER),
    MENDING(1, "mending", "Mending", CustomEnchantTier.BASIC, "Uses experience gained from skills to repair your tools", Enchantment.MENDING),
    BLAST_PROTECTION(5, "blastprotection", "Blast Protection", CustomEnchantTier.BASIC, "Reduces explosions damage", Enchantment.PROTECTION_EXPLOSIONS),
    SHARPNESS(6, "sharpness", "Sharpness", CustomEnchantTier.BASIC, "Increases damage", Enchantment.DAMAGE_ALL),
    SKELESHARPNESS(6, "skelesharpness", "Skele-Sharpness", CustomEnchantTier.BASIC, "Increases damage against skeletons", Enchantment.DAMAGE_UNDEAD),
    FIRE_ASPECT(3, "fireaspect", "Fire Aspect", CustomEnchantTier.BASIC, "When hitting an enemy, inflict them with fire", Enchantment.FIRE_ASPECT),
    EFFICIENCY(6, "efficiency", "Efficiency", CustomEnchantTier.BASIC, "Increases dig speed", Enchantment.DIG_SPEED),
    RESPIRATION(4, "respiration", "Respiration", CustomEnchantTier.BASIC, "Increases underwater breathing time",  Enchantment.OXYGEN),
    AQUA_AFFINITY(1, "aquaaffinity", "Aqua Affinity", CustomEnchantTier.BASIC, "Increases underwater mining speed", Enchantment.WATER_WORKER),
    EXPERIBOOST(1, "experiboost", "Expiri-Boost",  CustomEnchantTier.ADVANCED,"Doubles experience gained from mining when using this tool"),
    SILKTOUCH(1, "silktouch", "Silk Touch", CustomEnchantTier.ADVANCED, "Allows you to mine spawners", Enchantment.SILK_TOUCH)
    ;

    public int maxLevel;
    public String id;
    public CustomEnchantTier tier;
    public String displayName;
    public Enchantment[] vanillaCounterparts;
    public String description;

    CustomEnchantType(int maxLevel, String id, String displayName, CustomEnchantTier tier,String description, Enchantment... vanillaCounterparts){
        this.maxLevel = maxLevel;
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.vanillaCounterparts = vanillaCounterparts;
        this.description = description;
    }

    public String loreSafeDisplayName(){
        return displayName.replace(" ", "%space%");
    }
}
