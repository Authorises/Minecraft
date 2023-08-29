package dev.authorises.cavelet.utils;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import dev.authorises.cavelet.Cavelet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DisableSpawnerMobAI {

    public DisableSpawnerMobAI(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), () -> {
            for(Entity entity: Bukkit.getWorlds().get(0).getEntities()) {
                if((entity.getType()==EntityType.PIG || entity.getType()==EntityType.SHEEP||entity.getType()==EntityType.SKELETON||entity.getType()==EntityType.VINDICATOR)){
                    ((LivingEntity) entity).removePotionEffect(PotionEffectType.SLOW);
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, false, false));
                }
            }
        }, 0L, 20L);
    }

}
