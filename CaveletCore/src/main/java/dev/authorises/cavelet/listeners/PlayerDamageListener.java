package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void damage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            Cavelet.cachedMPlayers.get(player.getUniqueId()).combatLogLeft=20;
            Cavelet.cachedMPlayers.get(damager.getUniqueId()).combatLogLeft=20;
            Cavelet.lastDamager.put(player, damager);
            Cavelet.lastDamager.put(damager, player);
        }
    }

}
