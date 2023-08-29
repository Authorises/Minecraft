package dev.authorises.cavelet.intelligentblocks;

import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface IntelligentBlockFunctions {
    void shiftRightClick(PlayerInteractEvent event);
    void rightClick(PlayerInteractEvent event);
    void shiftLeftClick(PlayerInteractEvent event);
    void leftClick(PlayerInteractEvent event);
    void broken(BlockBreakEvent event);
    void shiftBroken(BlockBreakEvent event);
    void placed(IntelligentBlockPlacedEvent event) throws InterruptedException;
}
