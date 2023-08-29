package dev.authorises.vortechfactions.rng.listeners;

import dev.authorises.vortechfactions.rng.RNGChanceType;
import dev.authorises.vortechfactions.rng.RNGManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class GrindingListeners implements Listener {

    @EventHandler
    private void mobKill(EntityDeathEvent e) throws InstantiationException, IllegalAccessException {
        if(e.getEntity().getKiller() instanceof Player){
            Player player = e.getEntity().getKiller();
            if(e.getEntity().getType().equals(EntityType.GUARDIAN)){
                RNGManager.rngCheck(player, RNGChanceType.GRINDING);
            }
        }
    }
}
