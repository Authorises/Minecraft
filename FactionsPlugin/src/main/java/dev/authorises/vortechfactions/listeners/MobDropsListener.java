package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.items.Items;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDropsListener implements Listener {
    @EventHandler
    private void e(EntityDeathEvent e){
        if(e.getEntityType().equals(EntityType.GUARDIAN)){
            e.setDroppedExp(0);
            e.getDrops().clear();
            e.getDrops().add(Items.items.get("guardiandrop"));
        }
        else if(e.getEntityType().equals(EntityType.RABBIT)){
            e.setDroppedExp(0);
            e.getDrops().clear();
            e.getDrops().add(Items.items.get("rabbitdrop"));
        }else{
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
    }
}
