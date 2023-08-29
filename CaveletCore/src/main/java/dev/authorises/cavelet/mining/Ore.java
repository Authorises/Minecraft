package dev.authorises.cavelet.mining;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.economy.Sellable;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Ore extends Sellable {
    private final OreType type;
    private final OreRarity rarity;
    private final OreSpawnRarity spawnRarity;
    private final Material itemMaterial;
    private final Material blockMaterial;
    private final String oreId;
    private final String name;
    private final String displayName;

    public Ore(OreType type, OreRarity rarity, Integer sellPrice, OreSpawnRarity spawnRarity, Material itemMaterial, Material blockMaterial, String oreId, String name, String displayName) {
        this.type = type;
        this.rarity = rarity;
        this.spawnRarity = spawnRarity;
        this.itemMaterial = itemMaterial;
        this.blockMaterial = blockMaterial;
        this.oreId = oreId;
        this.name = name;
        if(!(this.rarity.equals(OreRarity.DEFAULT))) this.displayName = this.rarity.displayName+" "+displayName;
        else{
            this.displayName = displayName;
        }
        this.setSellPrice(sellPrice);
    }

    public ItemStack getItemStack(){
        ItemStack i = new ItemBuilder(this.itemMaterial)
                .setName(ColorUtils.format(this.displayName))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_DESTROYS,ItemFlag.HIDE_DYE,ItemFlag.HIDE_PLACED_ON,ItemFlag.HIDE_POTION_EFFECTS,ItemFlag.HIDE_UNBREAKABLE)
                .toItemStack();
        if(this.getRarity().equals(OreRarity.PURE)){
            ItemMeta m = i.getItemMeta();
            i.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            i.setItemMeta(m);
        }
        NBTItem ni = new NBTItem(i);
        ni.setString("ore_id", this.oreId);
        return ni.getItem();
    }


    public Material getItemMaterial() {
        return itemMaterial;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public OreRarity getRarity() {
        return rarity;
    }

    public OreType getType() {
        return type;
    }

    public OreSpawnRarity getSpawnRarity() {
        return spawnRarity;
    }

    public String getOreId() {
        return oreId;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
