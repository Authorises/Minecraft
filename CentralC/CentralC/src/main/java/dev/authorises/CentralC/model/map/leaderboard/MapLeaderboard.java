package dev.authorises.CentralC.model.map.leaderboard;

import com.google.gson.JsonObject;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.map.MapScore;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapLeaderboard {

    private List<MapScore> scores;
    private UUID resource;
    private Long timeCalculated;

    public MapLeaderboard(UUID resource, List<Document> scores){
        this.resource = resource;
        this.scores = new ArrayList<>();
        scores.forEach((scoreDocument) -> {
            System.out.println(scoreDocument.toJson());
            this.scores.add(new MapScore(scoreDocument));
        });
        this.timeCalculated = System.currentTimeMillis();
    }

    public JsonObject toJson(){
        JsonObject object = new JsonObject();
        object.addProperty("resource", resource.toString());
        object.addProperty("time", timeCalculated);
        object.add("scores", CentralCApplication.gson.toJsonTree(scores));
        return object;
    }

}
