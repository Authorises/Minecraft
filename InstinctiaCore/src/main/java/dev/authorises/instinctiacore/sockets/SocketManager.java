package dev.authorises.instinctiacore.sockets;


import org.bson.Document;
import org.java_websocket.WebSocket;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SocketManager {
    public WebSocket socket;

    public SocketManager() throws URISyntaxException {
        this.socket = startSocket();
    }

    public WebSocket startSocket() throws URISyntaxException {
        SocketClient c = new SocketClient(new URI("ws://localhost:9092"));
        c.connect();
        return c.getConnection();
    }

    public void reconnect() throws URISyntaxException {
        this.socket = startSocket();
    }
}
