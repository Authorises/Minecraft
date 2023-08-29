package dev.authorises.CentralC.controllers;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.players.SerializableOnlinePlayer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlinePlayersController {

    @GetMapping("/online-players")
    public String getServers(@RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.GLOBAL))) return "Unauthorized";
        JsonArray array = new JsonArray();
        CentralCApplication.onlinePlayerManager.onlinePlayers.values().forEach(s -> {
            array.add((CentralCApplication.gson.toJsonTree(new SerializableOnlinePlayer(s))));
        });
        return array.toString();
    }
}
