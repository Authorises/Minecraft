package dev.authorises.CentralC.model.players;

import dev.authorises.CentralC.CentralCApplication;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.*;

public class OnlinePlayerTask extends TimerTask {

    private HashMap<UUID, OnlinePlayer> onlinePlayers;

    public OnlinePlayerTask(HashMap<UUID, OnlinePlayer> onlinePlayers){
        this.onlinePlayers = onlinePlayers;
    }

    public void run() {
        ArrayList<OnlinePlayer> online = new ArrayList<>();
        online.addAll(onlinePlayers.values());
        for (int i = 0; i < online.size(); i++) {
            OnlinePlayer p = online.get(i);
            long x = System.currentTimeMillis() - p.lastUpdate;
            if (x > 3000) {
                onlinePlayers.remove(p.uuid);
                System.out.println(p.uuid+" logged off");
            }
        }
        CentralCApplication.serverManager.reloadPlayerCounts();
        CentralCApplication.proxyManager.reloadPlayerCounts();
    }
}
