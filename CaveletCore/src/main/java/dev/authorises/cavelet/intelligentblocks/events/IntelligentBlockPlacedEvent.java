package dev.authorises.cavelet.intelligentblocks.events;

import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockPlaceEvent;

public class IntelligentBlockPlacedEvent implements Cancellable {

    private boolean cancelled;
    private Player player;
    private IntelligentBlock placedBlock;
    private BlockPlaceEvent placeEvent;

    public IntelligentBlockPlacedEvent(Player player, IntelligentBlock placedBlock, BlockPlaceEvent placeEvent){
        this.cancelled = false;
        this.player = player;
        this.placedBlock = placedBlock;
        this.placeEvent = placeEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public IntelligentBlock getPlacedBlock() {
        return placedBlock;
    }

    public BlockPlaceEvent getPlaceEvent() {
        return placeEvent;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
        this.placeEvent.setCancelled(b);
    }
}
