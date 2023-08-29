package dev.authorises.cavelet.utils;

import dev.authorises.cavelet.shop.ItemStackShop;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class InventoryUtil {

    public static int freeSlots(Player player){
        int slots = 0;
        for(ItemStack stack : player.getInventory()){
            if(stack == null) {
                slots+=1;
            }
        }
        return slots-5;
    }

    public static EntityType getEntityType(final ItemStack is) throws IllegalArgumentException {
        final BlockStateMeta bsm = (BlockStateMeta) is.getItemMeta();
        final CreatureSpawner bs = (CreatureSpawner) bsm.getBlockState();
        return bs.getSpawnedType();
    }
}
