package dev.authorises.cavelet.playerdata;

import com.mongodb.client.FindIterable;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.exceptions.PlayerNotFoundException;
import org.bson.Document;

import java.util.UUID;

public class MProfileManager {

    public static MProfile getPlayerById(UUID uuid) throws Exception {
        if(Cavelet.cachedMPlayers.containsKey(uuid)){
            return Cavelet.cachedMPlayers.get(uuid);
        }else{
            return new MProfile(uuid);
        }
    }

    public static MProfile getPlayerByName(String name) throws Exception {
        for(MProfile mp : Cavelet.cachedMPlayers.values()){
            if(mp.getLastUsername().equalsIgnoreCase(name)) return mp;
        }

        FindIterable<Document> d = Cavelet.playerData.find(new Document("last-username", new Document("$regex",name).append("$options","i")));

        if(d.first()==null) throw new PlayerNotFoundException("Player with name "+name+" could not be found");
        else return new MProfile(d.first());

    }

}
