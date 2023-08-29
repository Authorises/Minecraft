package dev.authorises.vortechfactions.utilities;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Claim;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.objects.ChunkType;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Locale;

public class factionUtils {

    public static String getPath(Faction f){
        return "f-"+f.getId();
    }

    public static Faction getFaction(String path){
        return VortechFactions.api.getFactionById(path.replace("f-", ""));
    }

    public static void factionCreate(Player p, Faction f) throws IOException {
        p.sendMessage(ColorUtils.format("Faction Created!"));
        for(ChunkType type : ChunkType.values()){
            VortechFactions.dataFileConfig.set(getPath(f)+".claims."+type.getName().toLowerCase(Locale.ROOT), 0);
        }
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    public static void factionDisband(Player p, Faction f) throws IOException {
        p.sendMessage(ColorUtils.format("Faction Disbanded!"));
        for(Claim c : f.getAllClaims()){
           chunkUtils.claimChunk(p, c);
        }
        VortechFactions.dataFileConfig.set(getPath(f), null);
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    public static boolean canAddChunk(Faction f, ChunkType type){
        return VortechFactions.dataFileConfig.getInt(getPath(f)+".claims."+type.getName().toLowerCase(Locale.ROOT)) < type.getMaxNum();
    }

    public static void addChunk(Faction f, ChunkType type) throws IOException {
        Integer x = VortechFactions.dataFileConfig.getInt(getPath(f)+".claims."+type.getName().toLowerCase(Locale.ROOT));
        VortechFactions.dataFileConfig.set(getPath(f)+".claims."+type.getName().toLowerCase(Locale.ROOT), x+1);
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    public static void removeChunk(Faction f, ChunkType type) throws IOException {
        Integer x = VortechFactions.dataFileConfig.getInt(getPath(f)+".claims."+type.getName().toLowerCase(Locale.ROOT));
        VortechFactions.dataFileConfig.set(getPath(f)+".claims."+type.getName().toLowerCase(Locale.ROOT), x-1);
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    public static Integer chunksLeft(Faction f, ChunkType type){
        return type.getMaxNum() - VortechFactions.dataFileConfig.getInt(getPath(f)+".claims."+type.getName().toLowerCase(Locale.ROOT));
    }
}
