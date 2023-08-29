package dev.authorises.cavelet.factions;

import com.mongodb.client.FindIterable;
import dev.authorises.cavelet.Cavelet;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MFactionManager {

    public HashMap<UUID, MFaction> loadedFactions;
    public HashMap<UUID, MFaction> playersFactions;
    public List<String> factionNames;
    public FactionTop factionTop;

    public MFaction getFactionByName(String name){
        for(MFaction fac : loadedFactions.values()){
            if(fac.getName().equalsIgnoreCase(name)) return fac;
        }
        return null;
    }

    public MFactionManager(){
        this.loadedFactions = new HashMap<>();
        this.factionNames = new ArrayList<>();
        Bukkit.getLogger().info("Loading factions from Mongo");

        FindIterable<Document> findIterable =  Cavelet.factionsData.find();

        List<Document> factions = StreamSupport.stream(findIterable.spliterator(), false).toList();

        Bukkit.getLogger().info("Found "+factions.size()+" factions");

        this.playersFactions = new HashMap<>();

        factions.forEach(faction -> {

            MFaction fac = new MFaction(faction);
            fac.getMembers().keySet().forEach(k -> {
                playersFactions.put(k, fac);
            });
            factionNames.add(fac.getName());
            this.loadedFactions.put(fac.getId(), fac);

        });

        Bukkit.getLogger().info("Loaded "+loadedFactions.size()+" factions");

        factionTop = new FactionTop();
    }
}
