package dev.authorises.CentralC.model.map;

import dev.authorises.CentralC.util.UUIDUtil;
import org.bson.Document;

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

    public MapScore(Document document){
        this.player = UUID.fromString(document.getString("player"));
        this.resource = UUID.fromString(document.getString("resource"));
        this.timeCompleted = document.getLong("timeCompleted");
        this.totalTime = document.getLong("totalTime");
    }

    public Document toDocument(){
        return new Document()
                .append("player", getPlayer().toString())
                .append("resource", getResource().toString())
                .append("timeCompleted", getTimeCompleted())
                .append("totalTime", getTotalTime());
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
