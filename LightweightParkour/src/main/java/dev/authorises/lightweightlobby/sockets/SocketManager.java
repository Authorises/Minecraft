package dev.authorises.lightweightlobby.sockets;

import com.google.gson.JsonObject;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.game.ParkourSession;
import net.minestom.server.entity.Player;
import org.java_websocket.WebSocket;

import java.net.URISyntaxException;
import java.util.UUID;

public class SocketManager {
    public WebSocket socket;

    public SocketManager() throws URISyntaxException {
        this.socket = connect();
    }

    public WebSocket connect() throws URISyntaxException {
        SocketClient client = new SocketClient("ws://"+Server.CENTRALC_ENDPOINT+":9092");
        client.connect();
        WebSocket socket = client.getConnection();
        return socket;
    }

    public void reconnect() throws URISyntaxException {
        this.socket.close();
        this.socket = connect();
    }

    public void sendParkourRequest(Player player, UUID resource, ParkourSession.SessionAction action){

        player.sendMessage("sending parkour request for: "+resource);

        JsonObject data = new JsonObject();
        data.addProperty("player", player.getUuid().toString());
        data.addProperty("resource", resource.toString());
        data.addProperty("action", action.toString());

        JsonObject message = new JsonObject();
        message.addProperty("message", "player-parkour-request");
        message.add("data", data);

        socket.send(message.toString());
    }


    public void sendToServer(UUID player, String serverType){
        JsonObject data = new JsonObject();
        data.addProperty("type", serverType);
        data.addProperty("uuid", player.toString());

        JsonObject message = new JsonObject();
        message.addProperty("message", "player-transfer-generic");
        message.add("data", data);

        socket.send(message.toString());
    }
}
