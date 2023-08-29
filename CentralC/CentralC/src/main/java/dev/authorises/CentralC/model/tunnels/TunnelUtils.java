package dev.authorises.CentralC.model.tunnels;

import com.google.gson.JsonObject;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import dev.authorises.CentralC.model.server.Server;

public class TunnelUtils {

    public static void dispatch(OnlinePlayer player, Server server){
        JsonObject data = new JsonObject();
        data.addProperty("uuid", player.uuid.toString());
        data.addProperty("ip", server.ip);
        data.addProperty("port", server.port);

        JsonObject message = new JsonObject();
        message.addProperty("message", "player-transfer");

        message.add("data", data);

        player.currentProxy.webSocket.send(message.toString());
    }

}