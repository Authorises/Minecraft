package dev.authorises.vortechfactions.listeners;

import cc.javajobs.factionsbridge.bridge.events.FactionClaimEvent;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.chunkUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class ClaimListener implements Listener {

    @EventHandler
    private void f(FactionClaimEvent e) throws IOException {
        Player p = e.getFPlayer().getPlayer();
        chunkUtils.claimChunk(p, e.getClaim());
    }
}
