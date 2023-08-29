package dev.authorises.CentralC.controllers;

import com.mongodb.client.result.UpdateResult;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.JsonError;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.map.MapData;
import dev.authorises.CentralC.model.map.MapType;
import dev.authorises.CentralC.model.map.NewMap;
import dev.authorises.CentralC.model.players.PlayerData;
import dev.authorises.CentralC.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class AsyncMapController {

    @Autowired
    AsyncService asyncService;

    /**
     {
     "creators":[
     "1e3141e2-62ad-404f-9820-14b0c35459ac",
     "1e3141e2-62ad-404f-9820-14b0c35459ac"
     ],
     "uuid":"1e3141e2-62ad-404f-9820-14b0c35459ac",
     "name":"debugmaps",
     "checkpoints":[
     {"x":5.1, "y": 5.4, "z": 3.4},
     {"x":5.1, "y": 5.4, "z": 3.4},
     {"x":5.1, "y": 5.4, "z": 3.4},
     {"x":5.1, "y": 5.4, "z": 3.4}
     ],
     "type":"CHECKPOINT",
     "likes":12,
     "description":"the best parkour course",
     "material":"STONE",
     "spawnPoint":{"x":3,"y":4,"z":10}

     }
     */


    // general updating/creating and getting
    @PostMapping("/map/{uuid}")
    public CompletableFuture<String> setMap(@PathVariable String uuid, @RequestBody MapData mapData, @RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return CompletableFuture.completedFuture("Bad Auth");
        UUID id = UUID.fromString(uuid);
        return asyncService.setMap(id, mapData);
    }

    @GetMapping("/map/{uuid}")
    public CompletableFuture<String> getMap(@PathVariable String uuid, @RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.PRIVATE))) return CompletableFuture.completedFuture("Bad Auth");
        return asyncService.getMap(UUID.fromString(uuid)).thenApply((CentralCApplication.gson::toJson));
    }

    @GetMapping("/map/{uuid}/leaderboard")
    public String getMapLeaderboard(@PathVariable String uuid, @RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.PRIVATE))) return "Bad Auth";
        if(CentralCApplication.mapLeaderboardManager.leaderboards.containsKey(UUID.fromString(uuid))){
            return CentralCApplication.gson.toJson(CentralCApplication.mapLeaderboardManager.leaderboards.get(UUID.fromString(uuid)));
        }else{
            return JsonError.generateError("Leaderboard does not exist").toString();
        }

    }

    @GetMapping("/maps/{uuid}")
    public CompletableFuture<String> getPlayableMaps(@PathVariable String uuid, @RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return CompletableFuture.completedFuture("Bad Auth");
        return asyncService.getMapsForPlayer(UUID.fromString(uuid)).thenApply((CentralCApplication.gson::toJson));
    }

    @DeleteMapping("/map/{uuid}")
    public CompletableFuture<String> delMap(@PathVariable String uuid, @RequestParam(name="token") String token) throws ExecutionException, InterruptedException {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return CompletableFuture.completedFuture("Bad Auth");
        return asyncService.deleteMap(UUID.fromString(uuid)).thenApply((CentralCApplication.gson::toJson));
    }

    // player based stuff

    @PostMapping("/newmap")
    public CompletableFuture<String> createMap(@RequestParam String token, @RequestBody NewMap newMap) throws InterruptedException, ExecutionException {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return CompletableFuture.completedFuture("Bad Auth");
        return asyncService.createMap(newMap).thenApply((CentralCApplication.gson::toJson));
    }

}
