package dev.authorises.vortechfactions.rng.listeners;

import dev.authorises.vortechfactions.rng.RNGChanceType;
import dev.authorises.vortechfactions.rng.RNGManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class FarmingListeners implements Listener {

    @EventHandler
    private void blockBreak(BlockBreakEvent e) throws InstantiationException, IllegalAccessException {
        if(e.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)){
            RNGManager.rngCheck(e.getPlayer(), RNGChanceType.FARMING);
        }
    }
}
