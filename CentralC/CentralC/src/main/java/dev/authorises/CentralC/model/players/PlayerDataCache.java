package dev.authorises.CentralC.model.players;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataCache {

    public HashMap<UUID, PlayerData> cachedPlayerData;

    public PlayerDataCache(){
        this.cachedPlayerData = new HashMap<UUID, PlayerData>();
    }


}
