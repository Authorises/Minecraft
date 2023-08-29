package dev.authorises.cavelet.runnables;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.shop.ItemStackShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

public class FiveMinuteTick {
    public FiveMinuteTick(Plugin p){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(p, new Runnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    mp.save(p);
                }

                Cavelet.updateBalanceLeaderboard();
                Cavelet.updateKillsLeaderboard();
                Cavelet.lastLeaderboardUpdate = System.currentTimeMillis();

            }
        },0L, 6000L);
    }
}
