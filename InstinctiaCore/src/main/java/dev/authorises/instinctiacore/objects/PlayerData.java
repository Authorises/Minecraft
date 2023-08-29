package dev.authorises.instinctiacore.objects;

import com.google.gson.JsonObject;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.auth.TokenManager;
import kong.unirest.Unirest;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    public String lastUsername;
    public UUID uuid;
    public List<String> connectedIps;
    public boolean hasSupporter;
    public long supporterExpires;
    public long lastOnline;
    public String displayName;
    public List<UUID> friends;
    public List<UUID> requestsIn;
    public List<UUID> requestsOut;
    public JsonObject settings;

    public PlayerData(String lastUsername,
                      UUID uuid,
                      List<String> connectedIPs,
                      boolean hasSupporter,
                      long supporterExpires,
                      long lastOnline,
                      String displayName,
                      List<UUID> friends,
                      List<UUID> requestsIn,
                      List<UUID> requestsOut,
                      JsonObject settings
    ){
        this.lastUsername = lastUsername;
        this.connectedIps = connectedIPs;
        this.hasSupporter = hasSupporter;
        this.supporterExpires = supporterExpires;
        this.uuid = uuid;
        this.lastOnline = lastOnline;
        this.displayName = displayName;
        this.friends = friends;
        this.requestsIn = requestsIn;
        this.requestsOut = requestsOut;
        this.settings = settings;
    }

    public void update() throws IOException {
        Unirest.post("http://localhost:8080/player/"+uuid+"?token="+ TokenManager.token).body(InstinctiaCore.gson.toJson(this));
    }
}
