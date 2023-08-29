package dev.authorises.instinctiacore.utilities;

import dev.authorises.instinctiacore.InstinctiaCore;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class UUIDFromString {

    public static UUID fromStringOrName(String s) throws Exception {
        UUID id = null;
        boolean f = true;
        for(ProxiedPlayer pr : InstinctiaCore.proxy.getPlayers()){
            if(pr.getName().equalsIgnoreCase(s)){
                id=pr.getUniqueId();
                f=false;
            }
        }
        if(f){
            try {
                id=UUID.fromString(s);
            }catch (Exception e){
                throw new Exception("No name or uuid was found");
            }
        }
        return id;
    }

}
