package dev.authorises.cavelet.listeners;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.DamageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDamageListener implements Listener {

    @EventHandler
    public void itemDamaged(PlayerItemDamageEvent e){
        e.setCancelled(true);
        ItemStack i = e.getItem();
        NBTItem nbtItem = new NBTItem(i);

        if(nbtItem.hasKey("damage")){
            Double damage = nbtItem.getDouble("damage");
            if((damage+e.getDamage())>=nbtItem.getDouble("max_durability")){
                e.getPlayer().getInventory().remove(i);
                e.getPlayer().sendMessage(ColorUtils.format("&cYou used an item too much, and it broke! Remember to check the durability of your items."));
            }else{
                e.getPlayer().sendMessage("OLD HEALTH: "+(nbtItem.getDouble("max_durability")-damage));
                nbtItem.setDouble("damage", damage+e.getDamage());
                damage = nbtItem.getDouble("damage");
                try {
                    ItemStack[] armour = e.getPlayer().getInventory().getArmorContents();
                    if (armour[0].equals(e.getItem())) {
                        armour[0] = DamageUtil.updateDamageLore(nbtItem.getItem());
                    }
                    if (armour[1].equals(e.getItem())) {
                        armour[1] = DamageUtil.updateDamageLore(nbtItem.getItem());
                    }
                    if (armour[2].equals(e.getItem())) {
                        armour[2] = DamageUtil.updateDamageLore(nbtItem.getItem());
                    }
                    if (armour[3].equals(e.getItem())) {
                        armour[3] = DamageUtil.updateDamageLore(nbtItem.getItem());
                    }
                    e.getPlayer().getInventory().setArmorContents(armour);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                e.getPlayer().sendMessage("NEW HEALTH: "+(nbtItem.getDouble("max_durability")-damage));
            }
        }
    }

    @EventHandler
    public void itemDamaged(PlayerItemBreakEvent e){
        e.getPlayer().getInventory().addItem(e.getBrokenItem());
    }

}
