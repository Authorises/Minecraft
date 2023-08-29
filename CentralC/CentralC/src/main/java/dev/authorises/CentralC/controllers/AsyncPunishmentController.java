package dev.authorises.CentralC.controllers;

import dev.authorises.CentralC.CentralCApplication;
import dev.authorises.CentralC.model.access.PermissionType;
import dev.authorises.CentralC.model.players.PlayerData;
import dev.authorises.CentralC.model.punishments.Punishment;
import dev.authorises.CentralC.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class AsyncPunishmentController {

    @Autowired
    private AsyncService service;

    @GetMapping("/punishment/{uuid}")
    public CompletableFuture<Punishment> getPunishment(@PathVariable String uuid, @RequestParam(name="token") String token) throws Exception {
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        UUID id = UUID.fromString(uuid);
        return service.getPunishment(id);
    }

    @PostMapping(value="/punishment/{uuid}", headers="Accept=application/json")
    public CompletableFuture<HttpStatus> editPunishment(@RequestBody Punishment punishment, @RequestParam(name="token") String token){
        if(!(CentralCApplication.accessManager.canAccess(token, PermissionType.INTERNAL))) return null;
        service.setPunishment(punishment.id, punishment);
        return CompletableFuture.completedFuture(HttpStatus.OK);
    }
}
