package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.objects.ChunkType;
import dev.authorises.vortechfactions.utilities.chunkUtils;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class CactusListener implements Listener {

    @EventHandler
    private void b(BlockGrowEvent e){
        e.setCancelled(true);
        if(e.getNewState().getType()!= Material.CACTUS){
            e.getBlock().setType(Material.AIR);
        }else{
            Chunk c = e.getBlock().getChunk();
            if(chunkUtils.getType(c).equals(ChunkType.CACTUS)){
                String x = VortechFactions.api.getFactionAt(c).getId();
                if(!(VortechFactions.cactusCache.containsKey(x))){
                    VortechFactions.cactusCache.put(x, 1);
                }else {
                    VortechFactions.cactusCache.put(x, VortechFactions.cactusCache.get(x) + 1);
                }
            }else{
                e.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            if(e.getEntity().getType()== EntityType.PIG_ZOMBIE){
                e.getEntity().getCustomName();
            }

        }
    }

}
