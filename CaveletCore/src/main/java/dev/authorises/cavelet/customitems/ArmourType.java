package dev.authorises.cavelet.customitems;

import dev.authorises.cavelet.customenchants.CustomEnchantType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public enum ArmourType {
    BOOTS(EquipmentSlot.FEET, List.of(CustomEnchantType.DEPTH_STRIDER)),
    LEGGINGS(EquipmentSlot.LEGS, List.of()),
    CHESTPLATE(EquipmentSlot.CHEST, List.of()),
    HELMET(EquipmentSlot.HEAD, List.of(CustomEnchantType.AQUA_AFFINITY, CustomEnchantType.RESPIRATION));

    public EquipmentSlot slotType;
    public List<CustomEnchantType> allowedEnchantments;

    ArmourType(EquipmentSlot slotType, List<CustomEnchantType> allowedEnchantments){
        this.slotType = slotType;
        this.allowedEnchantments = allowedEnchantments;
    }
}
