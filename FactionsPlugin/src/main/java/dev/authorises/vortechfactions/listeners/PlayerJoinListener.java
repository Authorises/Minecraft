package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.FirstJoin;
import dev.authorises.vortechfactions.utilities.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void e(PlayerJoinEvent e) throws Exception {
        e.setJoinMessage("");
        Player p = e.getPlayer();
        Bukkit.getScheduler().runTaskLater(VortechFactions.getPlugin(VortechFactions.class), new Runnable() {
            @Override
            public void run() {
                p.teleport(new Location(Bukkit.getWorlds().get(0), -1, 88, -1));
            }
        },3);
        TitleUtils.sendTitle(p, 0, 40, 20, ColorUtils.format("&b&lInstinctia Factions"), ColorUtils.format("&fMap &b#1"));
        new FirstJoin().run(p);
        VortechFactions.scoreboard.playerJoin(p);
        VortechFactions.combatLogger.logOn(p);
    }
}
