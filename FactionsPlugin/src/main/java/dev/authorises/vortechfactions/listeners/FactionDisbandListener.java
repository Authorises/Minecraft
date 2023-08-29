package dev.authorises.vortechfactions.listeners;

import cc.javajobs.factionsbridge.bridge.events.FactionDisbandEvent;
import dev.authorises.vortechfactions.utilities.factionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class FactionDisbandListener implements Listener {

    @EventHandler
    private void f(FactionDisbandEvent e) throws IOException {
        factionUtils.factionDisband(e.getFPlayer().getPlayer(), e.getFaction());
    }
}
