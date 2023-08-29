package dev.authorises.instinctiacore.sockets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.auth.TokenManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
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

        JsonObject message = new JsonObject();
        message.addProperty("message", "connect-proxy");
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
                Component message = MiniMessage.miniMessage().deserialize(jsonObject.get("message").getAsString());
                ProxyServer.getInstance().getPlayer(uuid).sendMessage(BungeeComponentSerializer.get().serialize(message));
                break;
            }
            case "player-transfer-direct":{
                jsonObject = jsonObject.getAsJsonObject("data");
                UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
                String ip = jsonObject.get("ip").getAsString();
                Integer port = jsonObject.get("port").getAsInt();

                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
                p.connect(ProxyServer.getInstance().constructServerInfo(
                        UUID.randomUUID().toString(),
                        InetSocketAddress.createUnresolved(ip, port),
                        UUID.randomUUID().toString(),
                        false
                ));
            }
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        try {
            InstinctiaCore.socket.reconnect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
