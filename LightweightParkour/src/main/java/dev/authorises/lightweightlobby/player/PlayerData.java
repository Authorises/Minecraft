package dev.authorises.lightweightlobby.player;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    public String lastUsername;
    public UUID uuid;
    public boolean hasSupporter;
    public long supporterExpires;
    public long lastOnline;
    public String displayName;
    public List<UUID> friends;
    public List<UUID> requestsIn;
    public List<UUID> requestsOut;
    public List<UUID> mapsOwned;
    public List<UUID> mapsCollab;
    public JsonObject settings;


    // {"uuid":"1e3141e2-62ad-404f-9820-14b0c35459ac", "lastUsername":"Authorises", "hasSupporter":false, "supporterExpires":-1, "lastOnline":1000000, "displayName":"Auth", "friends": [], "requestsIn":[], "requestsOut":[], "settings":{}, "mapsOwned":[], "mapsCollab":[]}
    public PlayerData(String lastUsername, UUID uuid, boolean hasSupporter, long supporterExpires, long lastOnline, String displayName, List<UUID> friends, List<UUID> requestsIn, List<UUID> requestsOut, List<UUID> mapsOwned, List<UUID> mapsCollab, JsonObject settings) {
        this.lastUsername = lastUsername;
        this.uuid = uuid;
        this.hasSupporter = hasSupporter;
        this.supporterExpires = supporterExpires;
        this.lastOnline = lastOnline;
        this.displayName = displayName;
        this.friends = new ArrayList<>(friends);
        this.requestsIn = new ArrayList<>(requestsIn);
        this.requestsOut = new ArrayList<>(requestsOut);
        this.mapsOwned = new ArrayList<>(mapsOwned);
        this.mapsCollab = new ArrayList<>(mapsCollab);
        this.settings = settings;

    }
}
