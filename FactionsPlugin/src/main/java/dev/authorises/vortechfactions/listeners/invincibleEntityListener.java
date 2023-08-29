package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.VortechFactions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class invincibleEntityListener implements Listener {

    @EventHandler
    public void p(EntityDamageEvent e){
        if(VortechFactions.invincibleEntities.contains(e.getEntity().getUniqueId())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void e(PlayerArmorStandManipulateEvent e){
        if(VortechFactions.invincibleEntities.contains(e.getRightClicked().getUniqueId())){
            e.setCancelled(true);
        }
    }
}
