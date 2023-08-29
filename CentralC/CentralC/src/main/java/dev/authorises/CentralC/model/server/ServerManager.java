package dev.authorises.CentralC.model.server;

import com.corundumstudio.socketio.SocketIOClient;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import org.java_websocket.WebSocket;

import java.util.*;

public class ServerManager {
    // String is the global serve name - i.e. lm-0
    public HashMap<String, Server> connectedServers;
    public HashMap<WebSocket, Server> serverSockets;
    public final long startTime;
    private List<String> usedNames;

    public ServerManager(){
        this.startTime = System.currentTimeMillis();
        this.usedNames = new ArrayList<>();
        this.connectedServers = new HashMap<>();
        this.serverSockets = new HashMap<>();
    }

    public void reloadPlayerCounts(){
        for(Server server : connectedServers.values()){
            int online = 0;
            for(OnlinePlayer player : CentralCApplication.onlinePlayerManager.onlinePlayers.values()){
                if(player.currentServer !=null && player.currentServer.equals(server)) {
                    online += 1;
                }
            }
            server.playersOnline = online;
        }
    }

    public void addServer(Server server){
        usedNames.add(server.globalName);
        connectedServers.put(server.globalName, server);
        serverSockets.put(server.webSocket, server);
    }

    public void removeServer(WebSocket client){
        Server s = serverSockets.get(client);
        s.shutdown();
        connectedServers.remove(s.globalName);
        usedNames.remove(s.globalName);
        serverSockets.remove(client);
    }

    public String generateGlobalName(String serverType){
        String gname = serverType+"-"+UUID.randomUUID().toString().substring(0, 4);
        while (usedNames.contains(gname)){
            gname = serverType+"-"+UUID.randomUUID().toString().substring(0, 4);
        }
        return gname;
    }

    public Server getRandomByType(String type){
        List<Server> applicableServers = new ArrayList<>();
        for(Server s : connectedServers.values()){
            if(s.type.equals(type)){
                applicableServers.add(s);
            }
        }
        if(applicableServers.size()>1){
            Random r = new Random();
            return applicableServers.get(r.nextInt(applicableServers.size()));
        }else if(applicableServers.size()==1){
            return applicableServers.get(0);
        }else{
            return null;
        }
    }

    // get newest
    // get with least players
    // get with most players



}
