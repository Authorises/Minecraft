package dev.authorises.vortechfactions.listeners;

import com.comphenix.protocol.PacketType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class echestListener implements Listener {

    @EventHandler
    private void e(PlayerInteractEvent e){
        try {
            if (e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                Player p = e.getPlayer();
                if (!(p.isSneaking())) {
                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        p.performCommand("echest");
                        e.setCancelled(true);
                    }
                }
            }
        }catch (Exception ex){

        }
    }
}
