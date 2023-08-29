package dev.authorises.vortechfactions.listeners;


import dev.authorises.vortechfactions.VortechFactions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void playerQuit(PlayerQuitEvent e) throws IOException {
        Player p = e.getPlayer();
        VortechFactions.combatLogger.logOff(p);
        VortechFactions.scoreboard.playerLeave(p);

    }

}
