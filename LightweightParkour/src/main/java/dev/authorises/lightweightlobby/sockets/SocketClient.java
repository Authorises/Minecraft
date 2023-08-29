package dev.authorises.lightweightlobby.sockets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.game.ParkourSession;
import net.kyori.adventure.Adventure;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.UUID;

public class SocketClient extends WebSocketClient {

    public SocketClient(String serverURI) throws URISyntaxException {
        super(new URI(serverURI));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        JsonObject data = new JsonObject();
        data.addProperty("accessToken", Server.CENTRALC_TOKEN);
        data.addProperty("connectionIp", Server.PUBLIC_IP);
        data.addProperty("connectionPort", Server.PUBLIC_PORT);
        data.addProperty("type", Server.SERVER_TYPE);

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

                Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);

                if(player==null) return;

                player.sendMessage(MiniMessage.miniMessage().deserialize(jsonObject.get("message").getAsString()));

                break;
            }
            case "connect-response":{
                jsonObject = jsonObject.getAsJsonObject("data");
                System.out.println(jsonObject);

                break;
            }
            case "prepare":{
                jsonObject = jsonObject.getAsJsonObject("data");

                System.out.println(jsonObject.toString());

                UUID playerUuid = UUID.fromString(jsonObject.get("player").getAsString());
                ParkourSession session = new ParkourSession(
                        playerUuid,
                        UUID.fromString(jsonObject.get("resource").getAsString()),
                        ParkourSession.SessionAction.valueOf(jsonObject.get("action").getAsString())
                );

                Player player = MinecraftServer.getConnectionManager().getPlayer(playerUuid);

                if(player==null) {
                    // Player not on server, prepare to load for when they join
                    Server.playerSessions.put(playerUuid, session);

                    MinecraftServer.getSchedulerManager().buildTask(() -> {
                        if (Server.playerSessions.containsKey(playerUuid) && Server.playerSessions.get(playerUuid).equals(session)) {
                            Server.playerSessions.remove(playerUuid);
                        }
                    }).delay(Duration.ofSeconds(10)).schedule();
                }else{
                    // Player on server, load right now.
                    try {
                        session.load(player);
                    } catch (IOException | NBTException e) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>An error occurred loading your parkour session."));
                        throw new RuntimeException(e);
                    }
                }

                break;

            }
        }

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Socket reconnecting");
        try {
            Server.socketManager.reconnect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
