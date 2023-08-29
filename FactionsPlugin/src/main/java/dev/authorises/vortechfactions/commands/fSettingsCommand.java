package dev.authorises.vortechfactions.commands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class fSettingsCommand implements Listener {

    @EventHandler
    public void h(PlayerCommandPreprocessEvent e){
        String x = e.getMessage().toLowerCase(Locale.ROOT);
        if(x.equalsIgnoreCase("/F SETTINGS")){
            e.setCancelled(true);
            e.getPlayer().performCommand("facsets");
        }
        if(x.equalsIgnoreCase("/F SETTING")){
            e.setCancelled(true);
            e.getPlayer().performCommand("facsets");
        }
    }
}
