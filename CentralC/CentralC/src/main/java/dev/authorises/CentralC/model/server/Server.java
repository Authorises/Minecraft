package dev.authorises.CentralC.model.server;

import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import org.java_websocket.WebSocket;

import java.util.UUID;

public class Server {
    public final String globalName;
    public final String ip;
    public final Integer port;
    public final String type;
    public final long timeAdded;
    public transient WebSocket webSocket;
    public Integer playersOnline;


    public Server(String globalName, String ip, Integer port, String type) {
        this.globalName = globalName;
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.timeAdded = System.currentTimeMillis();
    }

    public void shutdown(){
        /**
        for(OnlinePlayer p : CentralCApplication.onlinePlayerManager.onlinePlayers.values()){
            if(p.currentServer!=null) {
                if (p.currentServer.equals(this)) {
                    p.currentServer = null;
                }
            }
        }
         */
    }

}
