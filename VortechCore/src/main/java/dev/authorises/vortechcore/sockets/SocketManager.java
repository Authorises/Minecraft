package dev.authorises.vortechcore.sockets;

import dev.authorises.vortechcore.VortechCore;
import dev.authorises.vortechcore.utilities.ColorUtils;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        this.socket = this.startSocket();
    }
}
