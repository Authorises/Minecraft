package dev.authorises.CentralC.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.players.OnlinePlayer;
import dev.authorises.CentralC.model.server.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameTypeController {


    @GetMapping("/game/{game}")
    public String getServers(@RequestParam(name="token") String token, @PathVariable String game){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return "Unauthorized";
        JsonArray array = new JsonArray();
        Integer instances = 0;
        for(Server s: CentralCApplication.serverManager.connectedServers.values()){
            if(s.type.equalsIgnoreCase(game)) {
                instances += 1;
                JsonObject o = new JsonObject();
                o.addProperty("globalName", s.globalName);
                o.addProperty("ip", s.ip);
                o.addProperty("port", s.port);
                o.addProperty("type", s.type);
                o.addProperty("timeAdded", s.timeAdded);
                array.add(o);
            }
        }
        if(!(instances>=1)) return "Game not found";
        Integer players = 0;
        for(OnlinePlayer player : CentralCApplication.onlinePlayerManager.onlinePlayers.values()){
            if(player.currentServer.type.equalsIgnoreCase(game)) players+=1;
        }

        JsonObject message = new JsonObject();
        message.addProperty("instancesCount", instances);
        message.addProperty("players", players);
        message.add("instances", array);
        return message.toString();
    }

}
