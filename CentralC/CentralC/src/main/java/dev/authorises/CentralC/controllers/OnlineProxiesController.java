package dev.authorises.CentralC.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.access.PermissionType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineProxiesController {

    @GetMapping("/proxies")
    public String getServers(@RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return "Unauthorized";
        JsonArray array = new JsonArray();
        CentralCApplication.proxyManager.connectedProxies.values().forEach(s -> {
            JsonObject o = new JsonObject();
            o.addProperty("uuid", s.id.toString());
            o.addProperty("globalName", s.globalName);
            o.addProperty("timeAdded", s.timeAdded);
            array.add(o);
        });
        return array.toString();
    }

}
