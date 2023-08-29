package dev.authorises.CentralC.sockets;

import com.corundumstudio.socketio.*;

import java.net.UnknownHostException;

public class SocketManager {

    public SocketServer socketServer;

    public SocketManager() throws UnknownHostException {
        socketServer = new SocketServer(9092);
        socketServer.start();
        System.out.println("Websocket server started on port: " + socketServer.getPort());
    }
}
