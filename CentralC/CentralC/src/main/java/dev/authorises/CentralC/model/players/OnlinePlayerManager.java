package dev.authorises.CentralC.model.players;

import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.proxy.Proxy;
import dev.authorises.CentralC.model.server.Server;
import net.kyori.adventure.text.Component;
import org.springframework.context.expression.CachedExpressionEvaluator;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OnlinePlayerManager {
    public HashMap<UUID, OnlinePlayer> onlinePlayers;

    public OnlinePlayerManager(){
        this.onlinePlayers = new HashMap<>();
        Timer timer = new Timer();
        timer.schedule(new OnlinePlayerTask(this.onlinePlayers), 0, 1000);
    };

    public void update(UUID uuid, String username, Server server){
        if(onlinePlayers.containsKey(uuid)){
            onlinePlayers.get(uuid).update(server);
        }else{
            onlinePlayers.put(uuid, new OnlinePlayer(
                    uuid,
                    username
            ));
            onlinePlayers.get(uuid).update(server);
        }
    }

    public void update(UUID uuid, String username, Proxy proxy){
        if(onlinePlayers.containsKey(uuid)){
            onlinePlayers.get(uuid).update(proxy);
        }else{
            onlinePlayers.put(uuid, new OnlinePlayer(
                    uuid,
                    username
            ));
            onlinePlayers.get(uuid).update(proxy);
        }
    }

    
}
