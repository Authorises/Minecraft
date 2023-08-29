package dev.authorises.lightweightlobby.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;

import java.util.HashMap;

public class PlayerLeave {

    private static final HashMap<Player, Runnable> data = new HashMap<>();

    public static void add(Player player, Runnable toDo){
        data.put(player, toDo);
    }

    public static void end(Player player){
        if(!data.containsKey(player)) return;
        data.remove(player);
    }

    public static void init(){
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, (event) -> {
            if(!data.containsKey(event.getPlayer())) return;
            data.remove(event.getPlayer()).run();
        });
    }

}
