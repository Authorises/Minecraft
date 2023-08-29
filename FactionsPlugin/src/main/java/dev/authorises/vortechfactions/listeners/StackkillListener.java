package dev.authorises.vortechfactions.listeners;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.rng.RNGChanceType;
import dev.authorises.vortechfactions.rng.RNGManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class StackkillListener implements Listener {

    @EventHandler
    public void entityInteractEvent(PlayerInteractAtEntityEvent e) throws InstantiationException, IllegalAccessException {
        if(e.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD)){
            if(e.getPlayer().hasPermission("*")){
                if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)){
                    if(e.getRightClicked() instanceof LivingEntity) {
                        LivingEntity en = (LivingEntity) e.getRightClicked();
                        StackedEntity sen = WildStackerAPI.getWildStacker().getSystemManager().getStackedEntity(en);
                        sen.setStackAmount(1, true);
                        for (int i = 0; i < sen.getStackAmount(); i++) {
                            RNGManager.rngCheck(e.getPlayer(), RNGChanceType.GRINDING);
                        }
                    }
                }
            }
        }
    }
}
