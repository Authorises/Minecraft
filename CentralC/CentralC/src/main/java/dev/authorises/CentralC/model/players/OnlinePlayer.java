package dev.authorises.CentralC.model.players;

import com.google.gson.JsonObject;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.proxy.Proxy;
import dev.authorises.CentralC.model.server.Server;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.HashMap;
import java.util.UUID;

public class OnlinePlayer {
    public Proxy currentProxy;
    public Server currentServer;
    public UUID uuid;
    public String lastUsername;
    public long lastUpdate;

    public OnlinePlayer(UUID uuid, String lastUsername){
        this.uuid = uuid;
        this.lastUsername = lastUsername;
    }

    public void update(Proxy currentProxy){
        this.currentProxy = currentProxy;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void update(Server currentServer){
        this.currentServer = currentServer;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void connect(Server server) throws Exception {

        JsonObject data = new JsonObject();
        data.addProperty("ip", server.ip);
        data.addProperty("port", server.port);
        data.addProperty("uuid", this.uuid.toString());
        data.addProperty("name", server.globalName);

        JsonObject message = new JsonObject();
        message.addProperty("message", "player-transfer-direct");
        message.add("data", data);

        if(this.currentProxy!=null && this.currentProxy.webSocket!=null && this.currentProxy.webSocket.isOpen()){
            this.currentProxy.webSocket.send(message.toString());
        }else{
            throw new Exception("Not currently on a proxy!");
        }
    }

    public void sendMessage(Component sendMessage){
        JsonObject data = new JsonObject();
        data.addProperty("uuid", this.uuid.toString());
        data.addProperty("message", MiniMessage.miniMessage().serialize(sendMessage));

        JsonObject message = new JsonObject();
        message.addProperty("message", "player-message");
        message.add("data", data);

        if(this.currentProxy!=null && this.currentProxy.webSocket!=null && this.currentProxy.webSocket.isOpen()){
            this.currentProxy.webSocket.send(message.toString());
        }else{
            if(this.currentServer!=null && this.currentServer.webSocket!=null && this.currentServer.webSocket.isOpen()){
                this.currentServer.webSocket.send(message.toString());
            }
        }
    }
}
