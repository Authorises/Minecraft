package dev.authorises.lightweightlobby.game;

import com.google.gson.JsonParser;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.map.MapData;
import kong.unirest.Unirest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.minestom.server.entity.Player;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ParkourSession {
    private final UUID playerId;
    private final UUID resourceId;
    private final SessionAction sessionAction;

    public ParkourSession(UUID playerId, UUID resourceId, SessionAction sessionAction) {
        this.playerId = playerId;
        this.resourceId = resourceId;
        this.sessionAction = sessionAction;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public SessionAction getSessionAction() {
        return sessionAction;
    }

    public enum SessionAction{
        PLAY,
        EDIT;
    }

    public void load(Player player) throws IOException, NBTException {

        player.showTitle(Title.title(Component.empty(), Component.empty()));

        switch (getSessionAction()){
            case EDIT:{
                player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<aqua>Loading Editor"), MiniMessage.miniMessage().deserialize("<gray>Your map editor is being created..."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(10), Duration.ZERO)));
                String res = Unirest.get("http://"+Server.CENTRALC_ENDPOINT+":8080"+"/map/"+getResourceId()+"?token="+Server.CENTRALC_TOKEN)
                        .asString()
                        .getBody();
                MapData data = new MapData(JsonParser.parseString(res).getAsJsonObject());
                new MapEditor(player, data);
                break;
            }
            case PLAY:{
                player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<aqua>Loading map"), MiniMessage.miniMessage().deserialize("<gray>Your map editor is being created..."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(10), Duration.ZERO)));
                String res = Unirest.get("http://"+Server.CENTRALC_ENDPOINT+":8080"+"/map/"+getResourceId()+"?token="+Server.CENTRALC_TOKEN)
                        .asString()
                        .getBody();
                MapData data = new MapData(JsonParser.parseString(res).getAsJsonObject());
                switch (data.getType()){
                    case CHECKPOINT -> new CheckpointMapPlayer(player, data);
                }
                break;
            }
        }
    }
}


