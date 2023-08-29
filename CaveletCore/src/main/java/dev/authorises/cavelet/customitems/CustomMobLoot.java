package dev.authorises.cavelet.customitems;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CustomMobLoot {
    private final String name;
    private final String displayName;
    private final Double sellValue;
    private final Double buyValue;
    private final Material material;
    private final Boolean stackable;
    private final Boolean glowing;

    public CustomMobLoot(String name, String displayName, Double sellValue, Double buyValue, Material material, Boolean stackable, Boolean glowing) {
        this.name = name;
        this.displayName = displayName;
        this.sellValue = sellValue;
        this.buyValue = buyValue;
        this.material = material;
        this.stackable = stackable;
        this.glowing = glowing;
    }

    public ItemStack getItem(){
        ItemBuilder i = new ItemBuilder(getMaterial())
                .setName(ColorUtils.format(this.displayName));
        if(getGlowing()){
            i=i.addEnchant(Enchantment.MENDING, 1)
                    .addFlags(ItemFlag.HIDE_ENCHANTS);
        }
        NBTItem nbtItem = new NBTItem(i.toItemStack());
        nbtItem.setString("item_id", getName());
        if(!(getStackable())){
            nbtItem.setUUID("mobloot_id", UUID.randomUUID());
        }

        return nbtItem.getItem();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Double getSellValue() {
        return sellValue;
    }

    public Double getBuyValue() {
        return buyValue;
    }

    public Material getMaterial() {
        return material;
    }

    public Boolean getStackable() {
        return stackable;
    }


    public Boolean getGlowing() {
        return glowing;
    }
}
