package dev.authorises.vortechfactions.itemlisteners;

import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.items.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StandardKit implements ItemHandler {
    @Override
    public String itemName() {
        return "standardkit";
    }

    @Override
    public Boolean requiresPermission() {
        return false;
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String permissionMessage() {
        return null;
    }

    @Override
    public void rightClickAir(PlayerInteractEvent e) throws Exception {
        Player p = e.getPlayer();
        if(p.getItemInHand().getAmount()>=2){
            p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
        }else {
            p.setItemInHand(null);
        }
        for(ItemStack t : Items.kits.get("standard")){
            p.getInventory().addItem(t);
        }
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void rightClickPlayer(PlayerInteractAtEntityEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickAir(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickPlayer(EntityDamageByEntityEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void place(BlockPlaceEvent e) throws Exception {
        e.setCancelled(true);
    }
}
