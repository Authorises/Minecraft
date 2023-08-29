package dev.authorises.CentralC.mongo;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

public class MongoManager {
    public final MongoCollection playerdata;
    public final MongoCollection punishments;
    public final MongoCollection maps;
    public final MongoCollection scores;

    public MongoManager(String uri){
        MongoClient c = new MongoClient(new MongoClientURI(uri));
        this.playerdata = c.getDatabase("InstinctiaNetwork").getCollection("PlayerData");
        this.punishments = c.getDatabase("InstinctiaNetwork").getCollection("Punishments");
        this.maps = c.getDatabase("InstinctiaNetwork").getCollection("Maps");
        this.scores = c.getDatabase("InstinctiaNetwork").getCollection("Scores");
    }
}
