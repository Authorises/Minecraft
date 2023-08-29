package dev.authorises.vortechcore.sockets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.vortechcore.VortechCore;
import dev.authorises.vortechcore.auth.TokenManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

public class SocketClient extends WebSocketClient {

    public SocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public SocketClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        JsonObject data = new JsonObject();
        data.addProperty("accessToken", TokenManager.token);
        data.addProperty("connectionIp", VortechCore.instance.config.getString("connectionIp"));
        data.addProperty("connectionPort", VortechCore.instance.config.getString("connectionPort"));
        data.addProperty("type", VortechCore.instance.config.getString("serverType"));

        JsonObject message = new JsonObject();
        message.addProperty("message", "connect-server");
        message.add("data", data);

        send(message.toString());
    }

    @Override
    public void onMessage(String string) {
        JsonObject jsonObject = new JsonParser().parse(string).getAsJsonObject();
        switch (jsonObject.get("message").getAsString()){
            case "player-message":{
                jsonObject = jsonObject.getAsJsonObject("data");
                UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
                Component message = GsonComponentSerializer.gson().deserialize(jsonObject.get("message").getAsString());
                Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(message);
                break;
            }
            case "connect-response":{
                jsonObject = jsonObject.getAsJsonObject("data");
                System.out.println(jsonObject);
                VortechCore.name = new JsonParser().parse(jsonObject.get("server").getAsString()).getAsJsonObject().get("globalName").getAsString();
            }
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        VortechCore.name="Reconnecting";
        try {
            VortechCore.socketManager.reconnect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
