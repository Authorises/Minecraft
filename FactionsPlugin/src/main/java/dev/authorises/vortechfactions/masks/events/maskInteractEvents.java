package dev.authorises.vortechfactions.masks.events;

import dev.authorises.vortechfactions.masks.Mask;
import dev.authorises.vortechfactions.masks.maskUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.EventListener;

public class maskInteractEvents implements Listener {

    @EventHandler
    public void e1(PlayerInteractEvent e){
        if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            Mask m = maskUtils.getMask(e.getPlayer());
            if(m!=null){
                m.leftClickBlock(e);
            }
        }
    }

    @EventHandler
    public void e2(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            Mask m = maskUtils.getMask(e.getPlayer());
            if(m!=null){
                m.rightClickBlock(e);
            }
        }
    }

    @EventHandler
    public void e3(PlayerInteractEvent e){
        if(e.getAction().equals(Action.LEFT_CLICK_AIR)){
            Mask m = maskUtils.getMask(e.getPlayer());
            if(m!=null){
                m.leftClickAir(e);
            }
        }
    }

    @EventHandler
    public void e(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            Mask m = maskUtils.getMask(e.getPlayer());
            if(m!=null){
                m.rightClickAir(e);
            }
        }
    }
}
