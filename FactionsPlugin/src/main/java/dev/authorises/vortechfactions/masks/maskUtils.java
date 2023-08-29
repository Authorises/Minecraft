package dev.authorises.vortechfactions.masks;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.masks.masks.DebugMask;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.textureUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class maskUtils {
    public static ItemStack getMaskItem(Mask mask){
        ItemStack t = textureUtils.createCustomSkull(1, ColorUtils.format(mask.getRarity().getColor()+mask.getDisplayName()+" Mask"), mask.getLore(), mask.getValue());
        ItemMeta tm = t.getItemMeta();
        tm.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
        t.setItemMeta(tm);
        NBTItem nbt = new NBTItem(t);
        nbt.setString("mask_name", mask.getName());
        return nbt.getItem();
    }

    public static Mask getMask(Player p){
        Mask ma = null;
        try{
            ItemStack helmetItem = p.getInventory().getHelmet();
            NBTItem helmetItemNBT = new NBTItem(helmetItem);
            if (helmetItemNBT.hasKey("mask_name")) {
                for (Mask m : VortechFactions.masks) {
                    if (m.getName().equalsIgnoreCase(helmetItemNBT.getString("mask_name"))) {
                        ma=m;
                    }
                }
            }
        }catch (Exception e){

        }
        if(ma==null) {
            try{
                ItemStack heldItem = p.getItemInHand();
                NBTItem heldItemNBT = new NBTItem(heldItem);
                if (heldItemNBT.hasKey("mask_name")) {
                    for (Mask m : VortechFactions.masks) {
                        if (m.getName().equalsIgnoreCase(heldItemNBT.getString("mask_name"))) {
                            ma=m;
                        }
                    }
                }
            }catch (Exception e){

            }
        }
        return ma;
    }
}
