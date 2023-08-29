package dev.authorises.CentralC.sockets;

import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.access.AccessToken;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import dev.authorises.CentralC.model.proxy.Proxy;
import dev.authorises.CentralC.model.server.Server;
import dev.authorises.CentralC.services.AsyncService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class SocketServer extends WebSocketServer {

    @Autowired
    private AsyncService asyncService;

    public SocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    public SocketServer(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Client connected!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        if(CentralCApplication.serverManager.serverSockets.containsKey(webSocket)) CentralCApplication.serverManager.removeServer(webSocket);
        if(CentralCApplication.proxyManager.proxySockets.containsKey(webSocket)) CentralCApplication.proxyManager.removeProxy(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String string) {
        JsonObject jsonObject = new JsonParser().parse(string).getAsJsonObject();
        switch (jsonObject.get("message").getAsString()){
            case "connect-server":{
                // get message body
                jsonObject = jsonObject.getAsJsonObject("data");

                // if its not authenticated; this message is only for authentication
                if(!(CentralCApplication.registeredSocketManager.authenticatedSockets.containsKey(webSocket))){
                    // check it has all of the required parameters
                    AtomicBoolean pass = new AtomicBoolean(true);
                    List<String> requiredKeys = List.of("accessToken", "type", "connectionIp", "connectionPort");
                    JsonObject finalJsonObject = jsonObject;
                    requiredKeys.forEach(s -> {
                        if(!finalJsonObject.has(s)) pass.set(false);
                    });
                    if(!pass.get()) return;
                    // Check that the access token provided has the required permissions
                    if(!(CentralCApplication.accessManager.canAccess(jsonObject.get("accessToken").getAsString(), PermissionType.INTERNAL))) return;
                    String hash = Hashing.sha256()
                            .hashString(jsonObject.get("accessToken").getAsString(), StandardCharsets.UTF_8)
                            .toString();
                    // add the server to the local server managers
                    CentralCApplication.registeredSocketManager.addSocket(webSocket, CentralCApplication.accessManager.accessTokens.get(hash));
                    String type = jsonObject.get("type").getAsString();
                    Server serverToAdd = new Server(
                            CentralCApplication.serverManager.generateGlobalName(type),
                            jsonObject.get("connectionIp").getAsString(),
                            jsonObject.get("connectionPort").getAsInt(),
                            type
                    );
                    serverToAdd.webSocket = webSocket;
                    CentralCApplication.serverManager.addServer(serverToAdd);

                    JsonObject data = new JsonObject();
                    data.addProperty("server", CentralCApplication.gson.toJson(serverToAdd));
                    JsonObject message = new JsonObject();
                    message.addProperty("message", "connect-response");
                    message.add("data", data);
                    serverToAdd.webSocket.send(message.toString());
                }
                break;
            }
            case "connect-proxy":{
                // get message body
                jsonObject = jsonObject.getAsJsonObject("data");
                // check its not already authenticated
                if(!(CentralCApplication.registeredSocketManager.authenticatedSockets.containsKey(webSocket))){
                    // check it has the required parameters
                    AtomicBoolean pass = new AtomicBoolean(true);
                    List<String> requiredKeys = List.of("accessToken");
                    JsonObject finalJsonObject = jsonObject;
                    requiredKeys.forEach(s -> {
                        if(!finalJsonObject.has(s)) pass.set(false);
                    });
                    if(!pass.get()) return;
                    // check it has permission to be added
                    if(!(CentralCApplication.accessManager.canAccess(jsonObject.get("accessToken").getAsString(), PermissionType.INTERNAL))) return;
                    String hash = Hashing.sha256()
                            .hashString(jsonObject.get("accessToken").getAsString(), StandardCharsets.UTF_8)
                            .toString();
                    // add it internally
                    CentralCApplication.registeredSocketManager.addSocket(webSocket, CentralCApplication.accessManager.accessTokens.get(hash));
                    Proxy proxy = new Proxy(
                            CentralCApplication.proxyManager.generateGlobalName()
                    );
                    proxy.webSocket = webSocket;
                    CentralCApplication.proxyManager.addProxy(proxy);

                    JsonObject data = new JsonObject();
                    data.addProperty("proxy", CentralCApplication.gson.toJson(proxy));

                    JsonObject message = new JsonObject();
                    message.addProperty("message", "connect-response");
                    message.add("data", data);
                    proxy.webSocket.send(message.toString());
                }
                break;
            }
            case "server-players-list":{
                // check if the socket is authenticated
                if(!(CentralCApplication.registeredSocketManager.authenticatedSockets.containsKey(webSocket))) return;
                // get the message body
                jsonObject = jsonObject.getAsJsonObject("data");
                // check it has all of the required parameters
                AtomicBoolean pass = new AtomicBoolean(true);
                List<String> requiredKeys = List.of("players");
                JsonObject finalJsonObject = jsonObject;
                requiredKeys.forEach(s -> {
                    if(!finalJsonObject.has(s)) pass.set(false);
                });
                if(!pass.get()) return;
                // check it has permission to send players list
                AccessToken token = CentralCApplication.registeredSocketManager.authenticatedSockets.get(webSocket);
                if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return;
                // update players list for the server
                Server server = CentralCApplication.serverManager.serverSockets.get(webSocket);
                JsonArray array = jsonObject.getAsJsonArray("players");
                array.iterator().forEachRemaining(s -> {
                    JsonObject ob = s.getAsJsonObject();
                    CentralCApplication.onlinePlayerManager.update(
                            UUID.fromString(ob.get("uuid").getAsString()),
                            ob.get("username").getAsString(),
                            server);
                });
                break;
            }
            case "proxy-players-list":{
                // check socket is authenticated
                if(!(CentralCApplication.registeredSocketManager.authenticatedSockets.containsKey(webSocket))) return;
                // get message body
                jsonObject = jsonObject.getAsJsonObject("data");
                // check it has all the required parameters
                AtomicBoolean pass = new AtomicBoolean(true);
                List<String> requiredKeys = List.of("players");
                JsonObject finalJsonObject = jsonObject;
                requiredKeys.forEach(s -> {
                    if(!finalJsonObject.has(s)) pass.set(false);
                });
                if(!pass.get()) return;

                // check it has permissions to send player list updates
                AccessToken token = CentralCApplication.registeredSocketManager.authenticatedSockets.get(webSocket);
                if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return;

                // update player lists internally
                Proxy proxy = CentralCApplication.proxyManager.proxySockets.get(webSocket);
                JsonArray array = jsonObject.getAsJsonArray("players");
                array.iterator().forEachRemaining(s -> {
                    JsonObject ob = s.getAsJsonObject();
                    CentralCApplication.onlinePlayerManager.update(
                            UUID.fromString(ob.get("uuid").getAsString()),
                            ob.get("username").getAsString(),
                            proxy);
                });
                break;
            }
            case "player-transfer-generic":{
                // check if it is authenticated
                if(!(CentralCApplication.registeredSocketManager.authenticatedSockets.containsKey(webSocket))) return;
                AccessToken token = CentralCApplication.registeredSocketManager.authenticatedSockets.get(webSocket);
                if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return;
                // get the data
                jsonObject = jsonObject.getAsJsonObject("data");
                // validate all the required parameters are present
                AtomicBoolean pass = new AtomicBoolean(true);
                List<String> requiredKeys = List.of("type", "uuid");
                JsonObject finalJsonObject = jsonObject;
                requiredKeys.forEach(s -> {
                    if(!finalJsonObject.has(s)) pass.set(false);
                });
                if(!pass.get()) return;

                UUID uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
                if(!CentralCApplication.onlinePlayerManager.onlinePlayers.containsKey(uuid)) return;
                OnlinePlayer p = CentralCApplication.onlinePlayerManager.onlinePlayers.get(uuid);
                Server s = CentralCApplication.serverManager.getRandomByType(jsonObject.get("type").getAsString());
                if(s!=null){
                    try {
                        p.connect(s);
                    } catch (Exception e) {
                        p.sendMessage(Component.text("An error occurred whilst transferring you to "+s.globalName));
                        throw new RuntimeException(e);
                    }
                }else{
                    p.sendMessage(Component.text("An error occurred finding a server to transfer you to"));
                }
            }

            case "player-parkour-request":{
                // check if it is authenticated
                if(!(CentralCApplication.registeredSocketManager.authenticatedSockets.containsKey(webSocket))) return;
                AccessToken token = CentralCApplication.registeredSocketManager.authenticatedSockets.get(webSocket);
                if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return;
                // get the data
                jsonObject = jsonObject.getAsJsonObject("data");
                // validate all the required parameters are present
                AtomicBoolean pass = new AtomicBoolean(true);
                List<String> requiredKeys = List.of("player", "resource", "action");
                JsonObject finalJsonObject = jsonObject;
                requiredKeys.forEach(s -> {
                    if(!finalJsonObject.has(s)) pass.set(false);
                });
                if(!pass.get()) return;

                UUID player = UUID.fromString(jsonObject.get("player").getAsString());

                String action = jsonObject.get("action").getAsString();

                Server s = CentralCApplication.serverManager.getRandomByType("pk");

                JsonObject sendObjectData = new JsonObject();
                sendObjectData.addProperty("player", player.toString());
                sendObjectData.addProperty("resource", jsonObject.get("resource").getAsString());
                sendObjectData.addProperty("action", action);

                JsonObject sendObject = new JsonObject();
                sendObject.addProperty("message", "prepare");
                sendObject.add("data", sendObjectData);

                s.webSocket.send(sendObject.toString());

                try {
                    OnlinePlayer p = CentralCApplication.onlinePlayerManager.onlinePlayers.get(player);
                    p.sendMessage(Component.text(jsonObject.toString()));
                    // only connect if they aren't already connected
                    if(p.currentServer!=s) {
                        CompletableFuture.runAsync(() -> {
                            try {
                                Thread.sleep(1000);
                                p.connect(s);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("start");
    }
}
