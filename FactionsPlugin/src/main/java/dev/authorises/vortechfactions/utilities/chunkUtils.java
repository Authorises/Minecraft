package dev.authorises.vortechfactions.utilities;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Claim;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.objects.ChunkType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Locale;

public class chunkUtils {

    public static ChunkType chunkTypeFromString(String s){
        for(ChunkType type  : ChunkType.values()){
            if(type.getName().equalsIgnoreCase(s)){
                return type;
            }
        }
        return ChunkType.DEFAULT;
    }

    public static String chunkToString(Chunk c){
        return "c-"+c.getX()+"%"+c.getZ()+"%"+c.getWorld().getName();
    }

    public static Chunk chunkFromString(String s){
        String x = s.replace("c-", "");
        String[] xyw = x.split("%");
        return Bukkit.getWorld(xyw[2]).getChunkAt(Integer.parseInt(xyw[0]), Integer.parseInt(xyw[2]));
    }

    public static ChunkType getType(Chunk c){
        String x = chunkToString(c);
        if(VortechFactions.dataFileConfig.contains(x)){
            try {
                return chunkTypeFromString(VortechFactions.dataFileConfig.getString(chunkToString(c)));
            }catch (Exception e){
                e.printStackTrace();
                return ChunkType.DEFAULT;
            }
        }
        return ChunkType.DEFAULT;
    }

    public static void unclaimChunk(Player p, Claim c) throws IOException {
        System.out.println(c.getFaction().getName());
        String x = chunkToString(c.getChunk());
        if(VortechFactions.dataFileConfig.contains(x)){
            VortechFactions.dataFileConfig.set(x, null);
            VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
            Chunk ch = c.getChunk();
            ChunkType type = chunkTypeFromString(VortechFactions.dataFileConfig.getString(chunkToString(ch)));
            factionUtils.removeChunk(c.getFaction(), type);
            p.sendMessage(ColorUtils.format("&b(!) Chunk Unclaimed: &f"+c.getChunk().getX()+" "+c.getChunk().getZ()));
            p.sendMessage(ColorUtils.format("&f- &bChunk Type: "+type.getColor()+type.getName()));
            p.sendMessage(ColorUtils.format("&f- &bChunks Left of that type: &b"+factionUtils.chunksLeft(c.getFaction(), type)));
        }
    }

    public static void claimChunk(Player p, Claim c) throws IOException {
        Faction f = VortechFactions.api.getFaction(p);
        System.out.println(c.getFaction().getName());
        VortechFactions.dataFileConfig.set(chunkToString(c.getChunk()), ChunkType.DEFAULT.getName().toLowerCase(Locale.ROOT));
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
        factionUtils.addChunk(f, ChunkType.DEFAULT);
        p.sendMessage(ColorUtils.format("&b(!) Chunk Claimed: &f"+c.getChunk().getX()+" "+c.getChunk().getZ()));
        p.sendMessage(ColorUtils.format("&f- &bChunk Type: "+ChunkType.DEFAULT.getColor()+ChunkType.DEFAULT.getName()));
        p.sendMessage(ColorUtils.format("&f- &bChunks Left of that type: &b"+factionUtils.chunksLeft(f, ChunkType.DEFAULT)));
    }
}
