package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.TitleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {

    public void timer(final Player player) {
        (new BukkitRunnable() {
            public void run() {
                player.spigot().respawn();
            }
        }).runTaskLater(VortechFactions.getPlugin(VortechFactions.class), 3L);
    }

    @EventHandler
    public void d(PlayerDeathEvent e){
        timer(e.getEntity());
        TitleUtils.sendTitle(e.getEntity(), 0, 40, 40, ColorUtils.format("&c&lYou died!"), ColorUtils.format("&7You lost all of your items"));
        VortechFactions.combatLogger.clear(e.getEntity());
    }


}
