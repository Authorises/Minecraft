package dev.authorises.CentralC.model.proxy;

import com.corundumstudio.socketio.SocketIOClient;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import org.java_websocket.WebSocket;

import java.util.UUID;

public class Proxy {
    public final UUID id;
    public final String globalName;
    public final long timeAdded;
    public transient WebSocket webSocket;
    public Integer playersOnline;


    public Proxy(String globalName) {
        this.id = UUID.randomUUID();
        this.globalName = globalName;
        this.timeAdded = System.currentTimeMillis();
    }

    public void shutdown(){
        /**
        for(OnlinePlayer p : CentralCApplication.onlinePlayerManager.onlinePlayers.values()){
            if(p.currentProxy.equals(this)){
                p.currentServer = null;
            }
        }
         */
    }

}
