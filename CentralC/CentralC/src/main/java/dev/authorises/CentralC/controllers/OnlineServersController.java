package dev.authorises.CentralC.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.server.Server;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OnlineServersController {

    @GetMapping("/servers")
    public String getServers(@RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return "Unauthorized";
        return CentralCApplication.gson.toJson(CentralCApplication.serverManager.connectedServers.values());
        /**
        JsonArray array = new JsonArray();
        CentralCApplication.serverManager.connectedServers.values().forEach(s -> {
            JsonObject o = new JsonObject();
            o.addProperty("globalName", s.globalName);
            o.addProperty("ip", s.ip);
            o.addProperty("port", s.port);
            o.addProperty("type", s.type);
            o.addProperty("timeAdded", s.timeAdded);
            array.add(o);
        });
        return array.toString();
         */
    }

    @GetMapping("/server/{serverName}")
    public String getServer(@PathVariable String serverName, @RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return "Unauthorized";
        if(CentralCApplication.serverManager.connectedServers.containsKey(serverName)){
            Server s = CentralCApplication.serverManager.connectedServers.get(serverName);
            JsonObject o = new JsonObject();
            o.addProperty("globalName", s.globalName);
            o.addProperty("ip", s.ip);
            o.addProperty("port", s.port);
            o.addProperty("type", s.type);
            o.addProperty("timeAdded", s.timeAdded);
            return o.toString();
        }else{
            return "ERROR";
        }
    }

}
