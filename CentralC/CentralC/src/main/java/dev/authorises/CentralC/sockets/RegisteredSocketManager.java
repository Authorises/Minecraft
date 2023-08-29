package dev.authorises.CentralC.sockets;

import com.corundumstudio.socketio.SocketIOClient;
import dev.authorises.CentralC.model.access.AccessToken;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class RegisteredSocketManager {
    public final HashMap<WebSocket, AccessToken> authenticatedSockets;

    public RegisteredSocketManager(){
        this.authenticatedSockets = new HashMap<>();
    }

    public void addSocket(WebSocket client, AccessToken token){
        this.authenticatedSockets.put(client, token);
    }

    public void removeSocket(WebSocket client){
        if(authenticatedSockets.containsKey(client)) authenticatedSockets.remove(client);
    }
}
