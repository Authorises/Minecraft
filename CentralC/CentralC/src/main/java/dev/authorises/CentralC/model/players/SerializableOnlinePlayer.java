package dev.authorises.CentralC.model.players;

import dev.authorises.CentralC.model.proxy.Proxy;
import dev.authorises.CentralC.model.server.Server;

import java.util.UUID;

public class SerializableOnlinePlayer {
    public String currentProxy;
    public String currentServer;
    public UUID uuid;
    public String lastUsername;
    public long lastUpdate;

    public SerializableOnlinePlayer(OnlinePlayer onlinePlayer){
        if(onlinePlayer.currentServer != null){
            this.currentServer = onlinePlayer.currentServer.globalName;
        }else{
            this.currentServer = "Error";
        }

        if(onlinePlayer.currentProxy != null){
            this.currentProxy = onlinePlayer.currentProxy.globalName;
        }else{
            this.currentProxy = "Error";
        }

        this.uuid = onlinePlayer.uuid;
        this.lastUsername = onlinePlayer.lastUsername;
        this.lastUpdate = onlinePlayer.lastUpdate;
    }

}
