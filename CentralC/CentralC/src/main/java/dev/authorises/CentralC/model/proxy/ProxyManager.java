package dev.authorises.CentralC.model.proxy;

import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ProxyManager {
    public HashMap<String, Proxy> connectedProxies;
    public HashMap<WebSocket, Proxy> proxySockets;
    public final long startTime;
    private final List<String> usedNames;

    public ProxyManager(){
        this.startTime = System.currentTimeMillis();
        this.usedNames = new ArrayList<>();
        this.connectedProxies = new HashMap<>();
        this.proxySockets = new HashMap<>();
    }

    public void addProxy(Proxy server){
        usedNames.add(server.globalName);
        connectedProxies.put(server.globalName, server);
        proxySockets.put(server.webSocket, server);
    }

    public void removeProxy(WebSocket client){
        Proxy s = proxySockets.get(client);
        s.shutdown();
        connectedProxies.remove(s.globalName);
        usedNames.remove(s.globalName);
        proxySockets.remove(client);
    }

    public void reloadPlayerCounts(){
        for(Proxy proxy : connectedProxies.values()){
            Integer online = 0;
            for(OnlinePlayer player : CentralCApplication.onlinePlayerManager.onlinePlayers.values()){
                if(player.currentProxy != null && player.currentProxy.equals(proxy)){
                    online+=1;
                }
            }
            proxy.playersOnline = online;
        }
    }

    public String generateGlobalName(){
        String gname ="proxy-"+ UUID.randomUUID().toString().substring(0, 4);
        while (usedNames.contains(gname)){
            gname = "proxy-"+UUID.randomUUID().toString().substring(0, 4);
        }
        return gname;
    }


}
