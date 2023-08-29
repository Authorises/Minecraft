package dev.authorises.vortechfactions.kits;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class KitsListener implements Listener {

    @EventHandler
    private void e(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getClickedBlock().getType().equals(Material.ENDER_PORTAL_FRAME)){
                e.setCancelled(true);
                new KitGUI(e.getPlayer()).open();
            }
        }
    }
}
