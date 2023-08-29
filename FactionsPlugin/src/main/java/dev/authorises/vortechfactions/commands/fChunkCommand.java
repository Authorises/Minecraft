package dev.authorises.vortechfactions.commands;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.objects.ChunkType;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.chunkUtils;
import dev.authorises.vortechfactions.utilities.factionUtils;
import dev.authorises.vortechfactions.utilities.getFactionDisplay;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class fChunkCommand implements Listener {

    @EventHandler
    public void h(PlayerCommandPreprocessEvent e) throws IOException {
        String x = e.getMessage().toLowerCase(Locale.ROOT);
        if(x.startsWith("/f chunk ")){
            Player p = e.getPlayer();
            e.setCancelled(true);
            String xx = e.getMessage().replace("/f chunk ", "");
            String cmd = xx.split(" ")[0];
            if(cmd.equalsIgnoreCase("info")){
                Chunk c = p.getLocation().getChunk();
                ChunkType type = chunkUtils.chunkTypeFromString(VortechFactions.dataFileConfig.getString(chunkUtils.chunkToString(c)));
                p.sendMessage(ColorUtils.format("&b(!) Chunk : &f"+c.getX()+" "+c.getZ()));
                p.sendMessage(ColorUtils.format("&f- &bChunk Type: "+type.getColor()+type.getName()));
                p.sendMessage(ColorUtils.format("&f- &bChunk Owner: "+ getFactionDisplay.get(p, VortechFactions.api.getFactionAt(c))));
            }else if(cmd.equalsIgnoreCase("set")){
                if(xx.split(" ").length!=2){
                    p.sendMessage(ColorUtils.format("&f(&b!&f) Usage: &b/f chunk set normal/cactus/raid/spawners"));
                }else{
                     ChunkType type = chunkUtils.chunkTypeFromString(xx.split(" ")[1]);
                     Chunk c = p.getLocation().getChunk();
                     Faction f = VortechFactions.api.getFaction(p);
                     assert f != null;
                     if(Objects.requireNonNull(VortechFactions.api.getFactionAt(c)).getId().equals(f.getId())){
                         if(factionUtils.canAddChunk(f, type)){
                             ChunkType type2 = chunkUtils.chunkTypeFromString(VortechFactions.dataFileConfig.getString(chunkUtils.chunkToString(c)));
                             factionUtils.removeChunk(f, type2);
                             factionUtils.addChunk(f, type);
                             VortechFactions.dataFileConfig.set(chunkUtils.chunkToString(c), type.getName().toLowerCase());
                             VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
                             p.sendMessage(ColorUtils.format("&b(!) Chunk type changed: &f"+c.getX()+" "+c.getZ()));
                             p.sendMessage(ColorUtils.format("&f- &bPrevious type: "+type2.getColor()+type2.getName()));
                             p.sendMessage(ColorUtils.format("&f- &bNew type: "+type.getColor()+type.getName()));
                             p.sendMessage(ColorUtils.format("&F- "+type.getColor()+type.getName()+" &bclaims left: &f"+factionUtils.chunksLeft(f, type)));
                         }else{
                             p.sendMessage(ColorUtils.format("&cError! You have reached the max limit for that type of chunk."));
                         }
                     }else{
                            p.sendMessage(ColorUtils.format("&cError! You can only set chunk type in your own claims!"));
                     }
                }
            }
        }else if(x.equals("/f chunk")){
            e.getPlayer().sendMessage(ColorUtils.format("&f(&b!&f) Usage: &b/f chunk <info / set>"));
            e.setCancelled(true);
        }
    }

}
