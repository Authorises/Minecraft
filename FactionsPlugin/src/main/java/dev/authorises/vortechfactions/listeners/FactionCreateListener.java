package dev.authorises.vortechfactions.listeners;

import cc.javajobs.factionsbridge.bridge.events.FactionCreateEvent;
import dev.authorises.vortechfactions.utilities.factionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class FactionCreateListener implements Listener {

    @EventHandler
    private void f(FactionCreateEvent e) throws IOException {
        factionUtils.factionCreate(e.getPlayer(), e.getFaction());
    }
}
