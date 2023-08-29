package dev.authorises.vortechfactions.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHitPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void p(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){

            Player damager = (Player) e.getDamager();
            Player entity = (Player) e.getEntity();

            if(!Board.getInstance().getFactionAt(new FLocation(FPlayers.getInstance().getByPlayer(entity))).isSafeZone()) {
                VortechFactions.combatLogger.combat(damager);
                VortechFactions.combatLogger.combat(entity);
            }else{
                damager.sendMessage(ColorUtils.format("&cYou can not damage that player here."));

            }
        }
    }


}
