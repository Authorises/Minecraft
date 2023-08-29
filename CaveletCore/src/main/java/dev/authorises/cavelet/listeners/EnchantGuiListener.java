package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.gui.EnchantGUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnchantGuiListener implements Listener {

    @EventHandler
    public void rightClickEnchantTable(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)){
                e.setCancelled(true);
                new EnchantGUI(e.getPlayer());
            }
        }
    }

}
