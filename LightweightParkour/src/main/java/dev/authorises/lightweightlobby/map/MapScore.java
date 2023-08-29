package dev.authorises.lightweightlobby.map;

import com.google.gson.JsonObject;
import dev.authorises.lightweightlobby.util.UUIDUtil;

import java.util.UUID;

public class MapScore {

    private final UUID player;
    private final UUID resource;
    private final Long timeCompleted;
    private final Long totalTime;

    public MapScore(UUID player, UUID resource, Long timeCompleted, Long totalTime) {
        this.player = player;
        this.resource = resource;
        this.timeCompleted = timeCompleted;
        this.totalTime = totalTime;
    }

    public MapScore(JsonObject json){
        this.player = UUID.fromString(json.get("player").getAsString());
        this.resource = UUID.fromString(json.get("resource").getAsString());
        this.timeCompleted = json.get("timeCompleted").getAsLong();
        this.totalTime = json.get("totalTime").getAsLong();
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("id", getUuid().toString());
        object.addProperty("player", getPlayer().toString());
        object.addProperty("resource", getResource().toString());
        object.addProperty("timeCompleted", getTimeCompleted());
        object.addProperty("totalTime", getTotalTime());
        return object;
    }

    public UUID getUuid(){
        return UUIDUtil.combineUUIDs(getPlayer(), getResource());
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getResource() {
        return resource;
    }

    public Long getTimeCompleted() {
        return timeCompleted;
    }

    public Long getTotalTime() {
        return totalTime;
    }
}
