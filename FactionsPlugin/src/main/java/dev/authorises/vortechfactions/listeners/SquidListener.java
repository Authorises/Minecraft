package dev.authorises.vortechfactions.listeners;

import com.bgsoftware.wildstacker.api.events.EntityStackEvent;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.objects.ChunkType;
import dev.authorises.vortechfactions.utilities.chunkUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SquidListener implements Listener {

    @EventHandler
    private void e(EntityStackEvent e){
        if(e.getEntity().getType().equals(EntityType.SQUID)){
            if(chunkUtils.getType(e.getEntity().getLocation().getChunk()).equals(ChunkType.SPAWNERS)){
                String x = VortechFactions.api.getFactionAt(e.getEntity().getLocation().getChunk()).getId();
                if(!(VortechFactions.squidCache.containsKey(x))){
                    VortechFactions.squidCache.put(x, 1);
                }else {
                    VortechFactions.squidCache.put(x, VortechFactions.squidCache.get(x) + 1);
                }
            }else{
                e.setCancelled(true);
            }
            e.getEntity().remove();
        }
    }
}
