package dev.authorises.cavelet.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class DisplayMessage {
    public abstract void showTo(Player var1);

    public void showToAll() {
        this.showTo(Bukkit.getServer().getOnlinePlayers());
    }

    public void showTo(Collection<? extends Player> collection) {
        Iterator var3 = collection.iterator();

        while(var3.hasNext()) {
            Player player = (Player)var3.next();
            this.showTo(player);
        }

    }

    public void showTo(Player... players) {
        this.showTo((Collection)Arrays.asList(players));
    }
}
