package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.VortechFactions;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AirdropListener implements Listener {

    @EventHandler
    private void e(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            if(e.getClickedBlock().getType().equals(Material.BEACON)){
                if(VortechFactions.airdropManager.isActive()){
                    e.setCancelled(true);
                    VortechFactions.airdropManager.click(e.getPlayer());
                }
            }
        }
    }
}
