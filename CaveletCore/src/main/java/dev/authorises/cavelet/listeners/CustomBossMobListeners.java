package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.custommobs.CustomBossMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class CustomBossMobListeners implements Listener {

    @EventHandler
    public void entityDies(EntityDeathEvent e){
        if(Cavelet.customBossMobs.containsKey(e.getEntity().getUniqueId())){
            e.getDrops().clear();
            e.setDroppedExp(0);
            CustomBossMob m = Cavelet.customBossMobs.get(e.getEntity().getUniqueId());
            if(e.getEntity().getKiller() != null){
                m.kill(e.getEntity().getKiller(), e);
            }else{
                m.death();
            }
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            if(Cavelet.customBossMobs.containsKey(e.getEntity().getUniqueId())){
                e.setCancelled(true);
                CustomBossMob b = Cavelet.customBossMobs.get(e.getEntity().getUniqueId());
                LivingEntity entity = (LivingEntity)b.getE();
                if(entity.getHealth()-e.getFinalDamage()<=0){
                    e.getEntity().remove();
                    b.kill((Player) e.getDamager(), new EntityDeathEvent(entity, List.of()));
                }else{
                    entity.setHealth(entity.getHealth()-e.getFinalDamage());
                    Player p = (Player) e.getDamager();
                    p.damage(b.getPlayerDamage(), e.getEntity());
                    p.sendActionBar(Cavelet.miniMessage.deserialize(b.getHealthMessage()));
                }

            }
        }
    }



}
