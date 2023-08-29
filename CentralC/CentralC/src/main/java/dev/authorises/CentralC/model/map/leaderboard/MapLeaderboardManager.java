package dev.authorises.CentralC.model.map.leaderboard;

import java.util.HashMap;
import java.util.Timer;
import java.util.UUID;

public class MapLeaderboardManager {

    public final HashMap<UUID, MapLeaderboard> leaderboards;

    public MapLeaderboardManager(){
        leaderboards = new HashMap<>();

        new Timer().schedule(new MapLeaderboardCalculationTask(), 0L, 10000L);

    }

}
