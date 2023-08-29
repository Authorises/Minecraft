package dev.authorises.vortechfactions.masks.events;

import dev.authorises.vortechfactions.masks.Mask;
import dev.authorises.vortechfactions.masks.maskUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class maskBlockEvents implements Listener {

    @EventHandler
    public void e1(BlockPlaceEvent e){
        Mask m = maskUtils.getMask(e.getPlayer());
        if(m!=null){
            m.placeBlock(e);
        }
    }

    @EventHandler
    public void e2(BlockBreakEvent e){
        Mask m = maskUtils.getMask(e.getPlayer());
        if(m!=null){
            m.breakBlock(e);
        }
    }
}
