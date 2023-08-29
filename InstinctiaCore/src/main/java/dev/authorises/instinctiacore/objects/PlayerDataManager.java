package dev.authorises.instinctiacore.objects;

import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.auth.TokenManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import dev.authorises.instinctiacore.utilities.UUIDFetcher;
import kong.unirest.Unirest;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import org.bson.Document;

import javax.print.Doc;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Updates.*;

public class PlayerDataManager {

    public static PlayerData fromUsername(String username){
        try{
            JsonObject player = new JsonParser().parse(Unirest.get("http://localhost:8080/player/"+username+"?token="+ TokenManager.token).getBody().get().toString()).getAsJsonObject();
            ArrayList<String> connectedIps = new ArrayList<>();
            player.get("connectedIps").getAsJsonArray().forEach(s -> {
                connectedIps.add(s.getAsString());
            });
            ArrayList<UUID> friends = new ArrayList<>();
            player.get("friends").getAsJsonArray().forEach(s->friends.add(UUID.fromString(s.getAsString())));

            ArrayList<UUID> requestsIn = new ArrayList<>();
            player.get("requestsIn").getAsJsonArray().forEach(s->requestsIn.add(UUID.fromString(s.getAsString())));

            ArrayList<UUID> requestsOut = new ArrayList<>();
            player.get("requestsOut").getAsJsonArray().forEach(s->requestsOut.add(UUID.fromString(s.getAsString())));

            PlayerData playerData = new PlayerData(
                    player.get("lastUsername").getAsString(),
                    UUID.fromString(player.get("uuid").getAsString()),
                    connectedIps,
                    player.get("hasSupporter").getAsBoolean(),
                    player.get("supporterExpires").getAsLong(),
                    player.get("lastOnline").getAsLong(),
                    player.get("displayName").getAsString(),
                    friends,
                    requestsIn,
                    requestsOut,
                    player.get("settings").getAsJsonObject()
                    //new JsonParser().parse(player.get("settings").getAsString()).getAsJsonObject()

            );
            return playerData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void manageJoin(PendingConnection player, PreLoginEvent e){
        try {
            e.registerIntent(InstinctiaCore.instance);
            ArrayList<String> ips = new ArrayList<>();
            UUID uuid = UUIDFetcher.getUUID(player.getName());
            CompletableFuture.supplyAsync(() -> {
                try {
                    return PunishmentManager.getPunishments(uuid);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }).thenApply(punishments -> {
                punishments.forEach(pu -> {
                    if(pu.isActive()){
                        if(pu.getType().equals(PunishmentType.ban)){
                            e.completeIntent(InstinctiaCore.instance);
                            e.setCancelled(true);
                            e.setCancelReason(new TextComponent(ColorUtils.format("&fYou are currently unable to join the &bInstinctia Network" +
                                    "\n" +
                                    "&fReason: &b"+pu.getReason()+"\n" +
                                    "&fPunishment: &b"+pu.getID().toString())));
                            return;
                        }
                    }
                });
                return 0;
            });
            CompletableFuture.supplyAsync(() -> {
                return getPlayerById(uuid);
            }).thenApply((pdata) -> {
                if(pdata!=null){
                    InstinctiaCore.instance.getLogger().info("Player "+uuid.toString()+" has already joined, loading their data.");
                    if (pdata.hasSupporter) {
                        if (System.currentTimeMillis() > pdata.supporterExpires) {
                            pdata.hasSupporter = false;
                            pdata.supporterExpires = -1;
                        }
                    }
                    if (!(pdata.connectedIps.contains(player.getSocketAddress().toString()))) {
                        pdata.connectedIps.add(player.getSocketAddress().toString());
                    }

                    pdata.lastUsername = player.getName();
                    pdata.lastOnline=System.currentTimeMillis();
                    try {
                        pdata.update();
                    } catch (IOException ex) {
                        e.completeIntent(InstinctiaCore.instance);
                        e.setCancelReason(ColorUtils.format("&fA server-side error&f occurred managing your login.\nReason: &c"+ex.toString()));
                        ex.printStackTrace();
                        e.setCancelled(true);
                    }
                }else{
                    ips.add(player.getSocketAddress().toString());
                    PlayerData p = new PlayerData(player.getName(), uuid, ips, false, -1, System.currentTimeMillis(), "&7"+player.getName(), Collections.emptyList(), Collections.emptyList(),Collections.emptyList(), new JsonObject());
                    InstinctiaCore.supporterExpires.put(uuid, (long) -1);
                    p.lastOnline=System.currentTimeMillis();
                    try {
                        p.update();
                    } catch (IOException ex) {
                        e.completeIntent(InstinctiaCore.instance);
                        e.setCancelReason(ColorUtils.format("&fA server-side error&f occurred managing your login.\nReason: &c"+ex.toString()));
                        ex.printStackTrace();
                        e.setCancelled(true);
                    }
                }
                e.completeIntent(InstinctiaCore.instance);
               return 0;
            });
        }catch (Exception ex){
            e.completeIntent(InstinctiaCore.instance);
            e.setCancelReason(ColorUtils.format("&fA server-side error&f occurred managing your login.\nReason: &c"+ex.toString()));
            ex.printStackTrace();
            e.setCancelled(true);
        }
    }

    public static PlayerData getPlayerById(UUID id){
        try{
            JsonObject player = new JsonParser().parse(Unirest.get("http://localhost:8080/player/"+id+"?token="+ TokenManager.token).getBody().get().toString()).getAsJsonObject();
            if(player==null) return null;
            ArrayList<String> connectedIps = new ArrayList<>();
            player.get("connectedIps").getAsJsonArray().forEach(s -> {
                connectedIps.add(s.getAsString());
            });
            ArrayList<UUID> friends = new ArrayList<>();
            player.get("friends").getAsJsonArray().forEach(s->friends.add(UUID.fromString(s.getAsString())));

            ArrayList<UUID> requestsIn = new ArrayList<>();
            player.get("requestsIn").getAsJsonArray().forEach(s->requestsIn.add(UUID.fromString(s.getAsString())));

            ArrayList<UUID> requestsOut = new ArrayList<>();
            player.get("requestsOut").getAsJsonArray().forEach(s->requestsOut.add(UUID.fromString(s.getAsString())));

            PlayerData playerData = new PlayerData(
                    player.get("lastUsername").getAsString(),
                    UUID.fromString(player.get("uuid").getAsString()),
                    connectedIps,
                    player.get("hasSupporter").getAsBoolean(),
                    player.get("supporterExpires").getAsLong(),
                    player.get("lastOnline").getAsLong(),
                    player.get("displayName").getAsString(),
                    friends,
                    requestsIn,
                    requestsOut,
                    player.get("settings").getAsJsonObject()
                    //new JsonParser().parse(player.get("settings").toString()).getAsJsonObject()
            );
            return playerData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static TextComponent playerDataView(PlayerData p){
        TextComponent t = new TextComponent();
        if(p!=null){
            t.setText(p.uuid.toString());
            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    ColorUtils.format(
                            "&fUUID: &b"+p.uuid.toString()+"\n"+
                                    "&fLast Username: &b"+p.lastUsername+"\n"+
                                    "&fConnected IPS: &b"+p.connectedIps.size()+"\n"+
                                    "&fSupporter: "+(p.hasSupporter?"&aYes":"&cNo")+"\n"+
                                    "&b&nClick to manage this player"
                    )
            ).create()));
            t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/p "+p.uuid.toString()));
        }else{
            t.setText("Error occurred generating hoverable player name.");
        }
        return t;
    }

    public static TextComponent playerDataCopy(PlayerData p){
        TextComponent t = new TextComponent();
        if(p!=null){
            t.setText(p.uuid.toString());
            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    ColorUtils.format(
                            "&fUUID: &b"+p.uuid.toString()+"\n"+
                                    "&fLast Username: &b"+p.lastUsername+"\n"+
                                    "&fConnected IPS: &b"+p.connectedIps.size()+"\n"+
                                    "&fSupporter: "+(p.hasSupporter?"&aYes":"&cNo")+"\n"+
                                    "&b&nClick to copy UUID"
                    )
            ).create()));
            t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, p.uuid.toString()));
        }else{
            t.setText("Error occurred generating hoverable player name.");
        }
        return t;
    }

    public static BaseComponent playerDataInfo(PlayerData pl){
        TextComponent warnButton = new TextComponent(ColorUtils.format("&6&lWARN"));
        warnButton.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/punishment punish "+pl.uuid.toString()+" warn REASON"));
        warnButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&b&l(!)&F Click to warn this player, the player will be notified they have been warned.")).create()));

        TextComponent muteButton = new TextComponent(ColorUtils.format("&d&lMUTE"));
        muteButton.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/punishment punish "+pl.uuid.toString()+" mute REASON"));
        muteButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&b&l(!)&F Click to mute this player, this means the player can not talk in chat.")).create()));

        TextComponent banButton = new TextComponent(ColorUtils.format("&c&lBAN"));
        banButton.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/punishment punish "+pl.uuid.toString()+" ban REASON"));
        banButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&b&l(!)&F Click to ban this player, this means the player can not join the server.")).create()));

        TextComponent punsButton = new TextComponent(ColorUtils.format("&b&lPUNISHMENTS"));
        punsButton.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/punishment list "+pl.uuid.toString()));
        punsButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&b&l(!)&F Click to view all of this player's punishments")).create()));

        BaseComponent c = new TextComponent();
        c.addExtra(ColorUtils.format("&fPlayer: "));
        c.addExtra(PlayerDataManager.playerDataCopy(pl));
        c.addExtra(ColorUtils.format(
                "\n&fLast Username: &b"+pl.lastUsername+"\n"+
                        "&fConnected IPS: &b"+pl.connectedIps.size()+"\n"+
                        "&fSupporter: "+(pl.hasSupporter?"&aYes":"&cNo")+"\n"
        ));
        c.addExtra(warnButton);
        c.addExtra(new TextComponent(ColorUtils.format("&f | ")));
        c.addExtra(muteButton);
        c.addExtra(new TextComponent(ColorUtils.format("&f | ")));
        c.addExtra(banButton);
        c.addExtra(new TextComponent(ColorUtils.format("&f | ")));
        c.addExtra(punsButton);
        return c;
    }


}
