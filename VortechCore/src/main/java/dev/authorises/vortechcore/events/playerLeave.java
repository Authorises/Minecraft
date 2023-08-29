package dev.authorises.vortechcore.events;

import dev.authorises.vortechcore.VortechCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class playerLeave implements Listener {
    @EventHandler
    public void v(PlayerQuitEvent e){
        if(VortechCore.sb!=null) {
            VortechCore.sb.playerLeave(e.getPlayer());
        }
    }
}
