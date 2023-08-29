package dev.authorises.instinctiacore.events;

import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.objects.PlayerDataManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

import static net.md_5.bungee.event.EventPriority.LOW;

public class PreLogin implements Listener {

    @EventHandler(priority = LOW)
    public void onPreLogin(PreLoginEvent event){
        PlayerDataManager.manageJoin(event.getConnection(), event);
    }
}
