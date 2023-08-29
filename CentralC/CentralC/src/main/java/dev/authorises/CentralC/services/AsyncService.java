package dev.authorises.CentralC.services;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.JsonError;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.map.*;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import dev.authorises.CentralC.model.players.PlayerData;
import dev.authorises.CentralC.model.players.SerializableOnlinePlayer;
import dev.authorises.CentralC.model.punishments.Punishment;
import dev.authorises.CentralC.model.punishments.PunishmentType;
import dev.authorises.CentralC.util.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scripting.support.StandardScriptEvalException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.print.Doc;

import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.*;

@Service
public class AsyncService {

    private static Logger log = LoggerFactory.getLogger(AsyncService.class);

    @Async("asyncExecutor")
    public CompletableFuture<String> setMap(UUID mapId, MapData map){
        UpdateResult result = CentralCApplication.mongoManager.maps.updateOne(new Document("_id", mapId.toString()), combine(
                set("_id", map.getUuid().toString()),
                set("creators", map.getCreators().stream().map(UUID::toString).toList()),
                set("name", map.getName()),
                set("checkpoints", map.getCheckpoints().stream().map(Pos::toString).toList()),
                set("type", map.getType().toString()),
                set("likes", map.getLikes()),
                set("description", map.getDescription()),
                set("material", map.getMaterial()),
                set("spawn", map.getSpawnPoint().toString()),
                set("firstSave", map.getFirstSave()),
                set("verified", map.getVerified()),
                set("public", map.getPublic())
        ), new UpdateOptions().upsert(true));
        return CompletableFuture.completedFuture("OK");
    }

    @Async("asyncExecutor")
    public CompletableFuture<MapData> getMap(UUID uuid){

        FindIterable<Document> f = CentralCApplication.mongoManager.maps.find(new Document("_id", uuid.toString()));
        if(f.first()==null) return CompletableFuture.completedFuture(null);

        Document document = f.first();

        return CompletableFuture.completedFuture(new MapData(document));
    }

    @Async("asyncExecutor")
    public CompletableFuture<Object> createMap(NewMap newMap) throws InterruptedException, ExecutionException {
        PlayerData playerData = getPlayer(newMap.getCreator()).get();

        if(playerData==null || !(playerData.mapsOwned.size()<=4 || playerData.settings.has("infiniteMaps"))){
            return CompletableFuture.completedFuture(JsonError.generateError("You can only have 5 maps. If you need to create more either delete unused maps or contact staff."));
        }

        MapData data = newMap.generate();

        setMap(data.getUuid(), data).get();

        playerData.mapsOwned.add(data.getUuid());

        setPlayerData(playerData.uuid, playerData).get();

        return CompletableFuture.completedFuture(data);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Object> deleteMap(UUID mapId) throws ExecutionException, InterruptedException {
        MapData map = getMap(mapId).get();
        if(map==null) return CompletableFuture.completedFuture(JsonError.generateError("Could not find a map with the provided ID"));

        map.getCreators().forEach((creator) -> {
            try {
                PlayerData pd = getPlayer(creator).get();
                pd.mapsCollab.remove(mapId);
                pd.mapsOwned.remove(mapId);
                setPlayerData(pd.uuid, pd).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        CentralCApplication.mongoManager.maps.deleteOne(new Document("_id", mapId.toString()));

        // evac players playing the map

        return CompletableFuture.completedFuture(JsonError.generateError("Map was deleted"));


    }

    @Async("asyncExecutor")
    public CompletableFuture<Object> getMapsFromPlayer(UUID player){
        List<MapData> maps = new ArrayList<>();

        MongoCursor cursor = CentralCApplication.mongoManager.maps
                .find(new Document("creators", new Document("$in", Arrays.asList(player.toString()))))
                .iterator();

        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();
            maps.add(new MapData(document));
        }
        return CompletableFuture.completedFuture(maps);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Object> getMapsForPlayer(UUID player){
        JsonArray maps = new JsonArray();

        MongoCursor cursor = CentralCApplication.mongoManager.maps
                .find(new Document("firstSave", true).append("public", true))
                .sort(descending("verified"))
                .iterator();

        HashMap<UUID, MapScore> scores = new HashMap<>();

        CentralCApplication.mongoManager.scores.find(new Document("player", player.toString())).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                MapScore mapScore = new MapScore(document);
                scores.put(mapScore.getResource(), mapScore);
            }
        });

        while (cursor.hasNext()) {
            Document document = (Document) cursor.next();
            JsonObject object = new JsonObject();
            UUID id = UUID.fromString(document.getString("_id"));
            object.add("map", CentralCApplication.gson.toJsonTree(new MapData(document)));
            if(scores.containsKey(id)){
                object.add("score", CentralCApplication.gson.toJsonTree(scores.get(id)));
            }
            maps.add(object);
        }
        return CompletableFuture.completedFuture(maps);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Object> getScores(UUID player){
        List<MapScore> maps = new ArrayList<>();

        MongoCursor cursor = CentralCApplication.mongoManager.scores
                .find(new Document("player", player.toString()))
                .iterator();

        while (cursor.hasNext()) {
            maps.add(new MapScore((Document) cursor.next()));
        }

        return CompletableFuture.completedFuture(maps);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Object> getScore(UUID player, UUID resource){
        FindIterable<Document> search = CentralCApplication.mongoManager.scores
                .find(new Document("player", player.toString()).append("resource", resource.toString()));
        if(search.first()==null) return CompletableFuture.completedFuture(JsonError.generateError("Score not found"));

        return CompletableFuture.completedFuture(new MapScore(search.first()));
    }

    @Async("asyncExecutor")
    public CompletableFuture<Object> setScore(MapScore score){
        CentralCApplication.mongoManager.scores.updateOne(new Document("player", score.getPlayer().toString()).append("resource", score.getResource().toString()), combine(
                set("player", score.getPlayer().toString()),
                set("resource", score.getResource().toString()),
                set("timeCompleted", score.getTimeCompleted()),
                set("totalTime", score.getTotalTime())
        ), new UpdateOptions().upsert(true));

        return CompletableFuture.completedFuture("OK");
    }



    @Async("asyncExecutor")
    public CompletableFuture<PlayerData> getPlayer(UUID uuid) throws InterruptedException {
            FindIterable<Document> f = CentralCApplication.mongoManager.playerdata.find(new Document("_id", uuid.toString()));
            if (f.first() != null) {
                Document d = f.first();
                List<UUID> friends = d.getList("friends", String.class).stream().map(s-> UUID.fromString(s)).collect(Collectors.toList());
                List<UUID> requestsIn = d.getList("requestsIn", String.class).stream().map(s-> UUID.fromString(s)).collect(Collectors.toList());
                List<UUID> requestsOut = d.getList("requestsOut", String.class).stream().map(s-> UUID.fromString(s)).collect(Collectors.toList());
                PlayerData pd = new PlayerData(
                        d.getString("lastUsername"),
                        UUID.fromString(d.getString("_id")),
                        d.getBoolean("supporter"),
                        d.getLong("supporterExpires"),
                        d.getLong("lastOnline"),
                        d.getString("displayName"),
                        d.getList("friends", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("requestsIn", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("requestsOut", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("maps", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("mapsCollab", String.class).stream().map(UUID::fromString).toList(),
                        //d.get("settings", JsonObject.class)
                        CentralCApplication.gson.fromJson(d.getString("settings"), JsonElement.class).getAsJsonObject()
                );
                if (CentralCApplication.onlinePlayerManager.onlinePlayers.containsKey(uuid)) {
                    pd.onlinePlayer = new SerializableOnlinePlayer(CentralCApplication.onlinePlayerManager.onlinePlayers.get(uuid));
                }

                return CompletableFuture.completedFuture(pd);

            } else {
                return CompletableFuture.completedFuture(null);
            }
    }

    @Async("asyncExecutor")
    public CompletableFuture<PlayerData> getPlayerByName(String name) throws InterruptedException {
            FindIterable<Document> f = CentralCApplication.mongoManager.playerdata.find(new Document("lastUsernameCaps", name.toUpperCase()));
            if (f.first() != null) {
                Document d = f.first();
                UUID uuid = UUID.fromString(d.getString("_id"));
                PlayerData pd = new PlayerData(
                        d.getString("lastUsername"),
                        UUID.fromString(d.getString("_id")),
                        d.getBoolean("supporter"),
                        d.getLong("supporterExpires"),
                        d.getLong("lastOnline"),
                        d.getString("displayName"),
                        d.getList("friends", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("requestsIn", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("requestsOut", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("maps", String.class).stream().map(UUID::fromString).toList(),
                        d.getList("mapsCollab", String.class).stream().map(UUID::fromString).toList(),
                        //new JsonParser().parse(com.mongodb.util.JSON.serialize(d.get("settings", Document.class))).getAsJsonObject()
                        CentralCApplication.gson.fromJson(d.getString("settings"), JsonElement.class).getAsJsonObject()
                );
                if (CentralCApplication.onlinePlayerManager.onlinePlayers.containsKey(uuid)) {
                    pd.onlinePlayer = new SerializableOnlinePlayer(CentralCApplication.onlinePlayerManager.onlinePlayers.get(uuid));
                }

                return CompletableFuture.completedFuture(pd);
            } else {
                return CompletableFuture.completedFuture(null);
            }
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> setPlayerData(UUID uuid, PlayerData newPlayerData){
        UpdateResult result = CentralCApplication.mongoManager.playerdata.updateOne(new Document("_id", uuid.toString()), combine(
                set("lastUsername", newPlayerData.lastUsername),
                set("_id", newPlayerData.uuid.toString()),
                set("supporter", newPlayerData.hasSupporter),
                set("supporterExpires", newPlayerData.supporterExpires),
                set("lastOnline", newPlayerData.lastOnline),
                set("displayName", newPlayerData.displayName),
                set("friends", newPlayerData.friends.stream().map(UUID::toString).toList()),
                set("requestsIn", newPlayerData.requestsIn.stream().map(UUID::toString).toList()),
                set("requestsOut", newPlayerData.requestsOut.stream().map(UUID::toString).toList()),
                set("maps", newPlayerData.mapsOwned.stream().map(UUID::toString).toList()),
                set("mapsCollab", newPlayerData.mapsCollab.stream().map(UUID::toString).toList()),
                set("settings", newPlayerData.settings.toString()),
                set("lastUsernameCaps", newPlayerData.lastUsername.toUpperCase())
        ), new UpdateOptions().upsert(true));
        return CompletableFuture.completedFuture("OK");
    }

    @Async("asyncExecutor")
    public CompletableFuture<Punishment> getPunishment(UUID uuid){
        if(CentralCApplication.punishmentsCache.cachedPunishments.containsKey(uuid)){
            return  CompletableFuture.completedFuture(CentralCApplication.punishmentsCache.cachedPunishments.get(uuid));
        }else {
            FindIterable<Document> f = CentralCApplication.mongoManager.punishments.find(new Document("_id", uuid.toString()));
            if (f.first() != null) {
                Document d = f.first();
                Punishment p = new Punishment(
                        uuid,
                        PunishmentType.valueOf(d.getString("type")),
                        UUID.fromString(d.getString("player")),
                        UUID.fromString(d.getString("punisher")),
                        d.getString("reason"),
                        d.getBoolean("active"),
                        d.getLong("date")
                );
                CentralCApplication.punishmentsCache.cachedPunishments.put(uuid, p);
                return CompletableFuture.completedFuture(p);
            } else {
                return CompletableFuture.completedFuture(null);
            }
        }
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<Punishment>> getPunishments(UUID uuid){
            FindIterable<Document> f = CentralCApplication.mongoManager.punishments.find(new Document("player", uuid.toString()));
            return CompletableFuture.completedFuture(f.map(d -> new Punishment(
                    UUID.fromString(d.getString("_id")),
                    PunishmentType.valueOf(d.getString("type")),
                    UUID.fromString(d.getString("player")),
                    UUID.fromString(d.getString("punisher")),
                    d.getString("reason"),
                    d.getBoolean("active"),
                    d.getLong("date")
            )).into(new ArrayList<>()));
    }

    @Async("asyncExecutor")
    public CompletableFuture<List<Punishment>> getPunishmentsByName(String name){
        FindIterable<Document> player = CentralCApplication.mongoManager.playerdata.find(new Document("lastUsername", name));
        UUID uuid = UUID.fromString(player.first().getString("_id"));
        FindIterable<Document> f = CentralCApplication.mongoManager.punishments.find(new Document("player", uuid.toString()));
        return CompletableFuture.completedFuture(f.map(d -> new Punishment(
                UUID.fromString(d.getString("_id")),
                PunishmentType.valueOf(d.getString("type")),
                UUID.fromString(d.getString("player")),
                UUID.fromString(d.getString("punisher")),
                d.getString("reason"),
                d.getBoolean("active"),
                d.getLong("date")
        )).into(new ArrayList<>()));
    }

    @Async("asyncExecutor")
    public CompletableFuture<UpdateResult> setPunishment(UUID uuid, Punishment punishment){
        UpdateResult result = CentralCApplication.mongoManager.punishments.updateOne(new Document("_id", uuid.toString()), combine(
                set("_id", punishment.id.toString()),
                set("type", punishment.type.toString()),
                set("player", punishment.player.toString()),
                set("punisher", punishment.punisher.toString()),
                set("reason", punishment.reason),
                set("active", punishment.active),
                set("date", punishment.date)
        ), new UpdateOptions().upsert(true));
        CentralCApplication.punishmentsCache.cachedPunishments.put(uuid, punishment);
        return CompletableFuture.completedFuture(result);
    }

    public CompletableFuture<Object> removeFriend(PlayerData requestSender, PlayerData requestReceiver){
        requestSender.friends.remove(requestReceiver.uuid);
        requestReceiver.friends.remove(requestSender.uuid);

        return setPlayerData(requestSender.uuid, requestSender).thenApply((x) -> {
            return setPlayerData(requestReceiver.uuid, requestReceiver).thenApply((x2) -> {
                HashMap<UUID, OnlinePlayer> op = CentralCApplication.onlinePlayerManager.onlinePlayers;
                if(op.containsKey(requestSender.uuid)){
                    op.get(requestSender.uuid).sendMessage(Component.text(ColorUtils.format("&aFriends | &rYou are no longer friends with "+requestReceiver.displayName)));
                }
                if(op.containsKey(requestReceiver.uuid)){
                    op.get(requestReceiver.uuid).sendMessage(Component.text(ColorUtils.format("&aFriends | &rYou are no longer friends with "+requestSender.displayName)));
                }
                return "OK";
            });
        });
    }

    public CompletableFuture<Object> addFriend(PlayerData requestSender, PlayerData requestReceiver){
        requestReceiver.requestsOut.remove(requestSender.uuid);
        requestSender.requestsIn.remove(requestReceiver.uuid);
        requestSender.friends.add(requestReceiver.uuid);
        requestReceiver.friends.add(requestSender.uuid);
        return setPlayerData(requestReceiver.uuid, requestReceiver).thenApply((x) -> {
            return setPlayerData(requestSender.uuid, requestSender).thenApply((xx) -> {
                HashMap<UUID, OnlinePlayer> op = CentralCApplication.onlinePlayerManager.onlinePlayers;
                if(op.containsKey(requestSender.uuid)){
                    op.get(requestSender.uuid).sendMessage(Component.text(ColorUtils.format("&aFriends | &rYou are now friends with "+requestReceiver.displayName)));
                }
                if(op.containsKey(requestReceiver.uuid)){
                    op.get(requestReceiver.uuid).sendMessage(Component.text(ColorUtils.format("&aFriends | &rYou are now friends with "+requestSender.displayName)));
                }
                return "OK";
            });
        });
    }

    public CompletableFuture<Object> addRequest(PlayerData player, PlayerData sender){
        player.requestsIn.add(sender.uuid);
        sender.requestsOut.add(player.uuid);
        return setPlayerData(player.uuid, player).thenApply((x) -> {
            return setPlayerData(sender.uuid, sender).thenApply((x2) -> {
                HashMap<UUID, OnlinePlayer> op = CentralCApplication.onlinePlayerManager.onlinePlayers;
                if(op.containsKey(player.uuid)){
                    op.get(player.uuid).sendMessage(
                            Component.text(ColorUtils.format("&aFriends | &rYou have received a &aFriend Request&f from "+sender.displayName+"&r, click here to accept it or run &a/f accept "+sender.lastUsername)).
                                    clickEvent(ClickEvent.runCommand("/friend accept "+sender.lastUsername)).
                                    hoverEvent(HoverEvent.showText(Component.text(ColorUtils.format("&aClick to accept the request from &r"+sender.displayName+"&r.")))));
                }
                if(op.containsKey(sender.uuid)){
                    op.get(sender.uuid).sendMessage(Component.text(ColorUtils.format("&aFriends | &rYou have sent a &aFriend Request&f to "+player.displayName)));
                }
                return "OK";
            });
        });
    }

    public CompletableFuture<JsonObject> getFriendsList(UUID uuid, Integer page) throws InterruptedException {
        return getPlayer(uuid).thenApply((playerData -> {

            JsonObject response = new JsonObject();

            System.out.println("FRIENDS: "+playerData.friends);

            // sort the friends list by putting all the online players at the front
            List<UUID> sortedFriends = new ArrayList<>();
            for(UUID loopUuid : playerData.friends){
                if(CentralCApplication.onlinePlayerManager.onlinePlayers.containsKey(loopUuid)){
                    sortedFriends.add(loopUuid);
                }
            }
            for(UUID loopUuid : playerData.friends){
                if(!(sortedFriends.contains(loopUuid)) && playerData.friends.contains(loopUuid)) sortedFriends.add(loopUuid);
            }

            System.out.println("SFRIENDS: "+sortedFriends);

            int p = page;
            int maxPages = Double.valueOf(Math.ceil(playerData.friends.size()/5D)).intValue();
            if(p>maxPages) p = maxPages;

            if(!(playerData.friends.size()>=1)) maxPages = 0;

            response.addProperty("maxPages", maxPages);

            if(maxPages==0) return response;

            Boolean lastPage = p == maxPages;
            int first = (p-1)*5;
            int last = lastPage?playerData.friends.size():first+5;

            sortedFriends = sortedFriends.subList(first, last);

            JsonArray friends = new JsonArray();

            sortedFriends.forEach(loopUuid -> {
                System.out.println("LUUID: "+loopUuid);
                try {
                    CompletableFuture<Object> object = getPlayer(loopUuid).thenApply(loopPlayerData -> {
                        return CentralCApplication.gson.toJsonTree(loopPlayerData);
                    });
                    object.thenApply((o) -> {
                        friends.add((JsonElement) o);
                        return 0;
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            response.add("page", friends);
            return response;

           // 13 friends
            // 1 friend

            /**
             * page 1 0:1
             * page 2 5:9
             * page 3 10:12
             *
             */
        }));
    }
}