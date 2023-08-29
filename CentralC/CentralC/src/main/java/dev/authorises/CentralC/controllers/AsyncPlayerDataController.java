package dev.authorises.CentralC.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.result.UpdateResult;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.friends.FriendRequest;
import dev.authorises.CentralC.model.map.MapScore;
import dev.authorises.CentralC.model.players.*;
import dev.authorises.CentralC.model.punishments.Punishment;
import dev.authorises.CentralC.services.AsyncService;
import dev.authorises.CentralC.util.ColorUtils;
import io.netty.util.concurrent.CompleteFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class AsyncPlayerDataController {

    @Autowired
    private AsyncService service;

    @GetMapping("/player/{identifier}")
    public CompletableFuture<PlayerData> getPlayerData(@PathVariable String identifier, @RequestParam(name="token") String token) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;

        try {
            return service.getPlayer(UUID.fromString(identifier));
        }catch (IllegalArgumentException e){
            return service.getPlayerByName(identifier);
        }
    }

    @GetMapping("/player/{identifier}/punishments")
    public CompletableFuture<List<Punishment>> getPlayerPunishments(@PathVariable String identifier, @RequestParam(name="token") String token) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;

        try {
            return service.getPunishments(UUID.fromString(identifier));
        }catch (IllegalArgumentException e){
            return service.getPunishmentsByName(identifier);
        }
    }

    @PostMapping("/player/{uuid}")
    public CompletableFuture<String> editPlayerData(@PathVariable String uuid, @RequestBody PlayerData playerData, @RequestParam(name="token") String token) throws ExecutionException, InterruptedException {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        UUID id = UUID.fromString(uuid);
        return service.setPlayerData(id, playerData);
    }

    @GetMapping("/player/{uuid}/friend-list")
    public CompletableFuture<JsonObject> getPlayerFriends(@PathVariable String uuid, @RequestParam(name="token") String token, @RequestParam(name="page", defaultValue = "1") Integer page) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.getFriendsList(UUID.fromString(uuid), page);
    }

    @GetMapping("/player/{uuid}/maps")
    public CompletableFuture<String> getPlayerMaps(@PathVariable String uuid, @RequestParam(name="token") String token) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.getMapsFromPlayer(UUID.fromString(uuid)).thenApply(CentralCApplication.gson::toJson);
    }

    @GetMapping("/player/{uuid}/scores")
    public CompletableFuture<String> getPlayerScores(@PathVariable String uuid, @RequestParam(name="token") String token) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.getScores(UUID.fromString(uuid)).thenApply(CentralCApplication.gson::toJson);
    }

    @GetMapping("/player/{uuid}/score/{resource}")
    public CompletableFuture<String> getPlayerScore(@PathVariable String uuid, @PathVariable String resource, @RequestParam(name="token") String token) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.getScore(UUID.fromString(uuid), UUID.fromString(resource)).thenApply(CentralCApplication.gson::toJson);
    }

    @PostMapping("/score")
    public CompletableFuture<String> getPlayerScore(@RequestBody MapScore score, @RequestParam(name="token") String token) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.setScore(score).thenApply(CentralCApplication.gson::toJson);
    }

    @PostMapping("/friend-remove")
    public CompletableFuture<Object> friendRemove(@RequestBody FriendRequest friendRequest, @RequestParam(name="token") String token) throws InterruptedException {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.getPlayerByName(friendRequest.player).thenApply((playerData -> {
            if(playerData==null){
                return "Player not found! Have they joined before?";
            }else{
                try {
                    if(!(playerData.friends.contains(friendRequest.sender))) return "You are not friends with that player";
                    return service.getPlayer(friendRequest.sender).thenApply((senderData) -> {
                        return service.removeFriend(senderData, playerData);
                    });
                }catch (InterruptedException e){
                    e.printStackTrace();
                    return "An error occurred on the backend server.";
                }
            }
        }));
    }

    @PostMapping("/friend-accept")
    public CompletableFuture<Object> friendAccept(@RequestBody FriendRequest friendRequest, @RequestParam(name="token") String token) throws InterruptedException {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.getPlayerByName(friendRequest.player).thenApply((playerData -> {
            if(playerData==null){
                return "Player not found! Have they joined before?";
            }else{
                try {
                    if(!(playerData.requestsOut.contains(friendRequest.sender))) return "You do not have a friend request from that player";
                    return service.getPlayer(friendRequest.sender).thenApply((senderData) -> {
                       return service.addFriend(senderData, playerData);
                    });
                }catch (InterruptedException e){
                    e.printStackTrace();
                    return "An error occurred on the backend server.";
                }
            }
        }));
    }

    @PostMapping("/friend-request")
    public CompletableFuture<Object> friendRequest(@RequestBody FriendRequest friendRequest, @RequestParam(name="token") String token) throws InterruptedException {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        return service.getPlayerByName(friendRequest.player).thenApply((playerData -> {
            if(playerData==null) {
                return "Player not found! Have they joined before?";
            }else {
                try {
                    if(playerData.uuid.equals(friendRequest.sender)) return "You cannot send a friend request to yourself";
                    return service.getPlayer(friendRequest.sender).thenApply((senderData) -> {
                        if(playerData.friends.contains(senderData.uuid)) return "You are already friends with "+playerData.displayName;
                        if(playerData.friends.size()>=150) return "You cannot have more than 150 friends added at one time";
                        if(senderData.friends.size()>=150) return senderData.displayName+" currently has the maximum number of friends";
                        if(senderData.requestsOut.size()>=10) return "You have sent too many friend requests! Wait for them to expire.";
                        if(senderData.requestsIn.contains(playerData.uuid)){
                            return service.addFriend(playerData, senderData);
                        }else {
                            if(playerData.requestsIn.contains(senderData.uuid)){
                                return "You have already sent this player a friend request";
                            }else {
                                if (playerData.settings.has("friendRequests")) {
                                    if (playerData.settings.get("friendRequests").getAsBoolean()) {
                                        return service.addRequest(playerData, senderData);
                                    } else {
                                        return "Player has friend requests disabled";
                                    }
                                } else {
                                    return service.addRequest(playerData, senderData);
                                }
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "An error occurred on the backend server.";
                }
            }
        }));
    }


}
