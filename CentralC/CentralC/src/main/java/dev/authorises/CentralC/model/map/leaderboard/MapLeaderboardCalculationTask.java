package dev.authorises.CentralC.model.map.leaderboard;

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.map.MapData;
import dev.authorises.CentralC.model.map.MapScore;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Sorts.descending;

public class MapLeaderboardCalculationTask extends TimerTask {

    @Override
    public void run() {

        MongoCursor<Document> cursor = CentralCApplication.mongoManager.maps
                .find(new Document("firstSave", true).append("public", true).append("verified", true))
                .iterator();

        cursor.forEachRemaining((map) -> {
            MapData mapData = new MapData(map);
            System.out.println("Started calculating leaderboard for "+mapData.getUuid());
            CompletableFuture.supplyAsync(() -> {

                List<Bson> leaderboardPipeline = new ArrayList<>();
                leaderboardPipeline.add(Aggregates.match(Filters.eq("resource", mapData.getUuid().toString())));
                leaderboardPipeline.add(Aggregates.sort(Sorts.ascending("totalTime")));
                leaderboardPipeline.add(Aggregates.limit(10));

                ArrayList<Document> docs = new ArrayList<>();

                CentralCApplication.mongoManager.scores.aggregate(leaderboardPipeline).into(docs);

                CentralCApplication.mapLeaderboardManager.leaderboards.put(mapData.getUuid(), new MapLeaderboard(
                        mapData.getUuid(),
                        docs
                ));

                return "Finished calculating leaderboard for "+mapData.getUuid();
            }).thenAccept(System.out::println);

        });

    }

}
