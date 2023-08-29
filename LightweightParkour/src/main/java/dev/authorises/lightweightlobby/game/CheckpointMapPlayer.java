package dev.authorises.lightweightlobby.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.map.MapData;
import dev.authorises.lightweightlobby.map.MapScore;
import dev.authorises.lightweightlobby.util.FireworkUtils;
import dev.authorises.lightweightlobby.util.PlayerLeave;
import dev.authorises.lightweightlobby.util.TextUtil;
import dev.emortal.tnt.TNTLoader;
import dev.emortal.tnt.source.FileTNTSource;
import io.minio.DownloadObjectArgs;
import io.minio.errors.*;
import kong.unirest.Unirest;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.Material;
import net.minestom.server.item.firework.FireworkEffect;
import net.minestom.server.item.firework.FireworkEffectType;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.timer.Task;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class CheckpointMapPlayer extends ParkourPlayer{

    private final Player player;
    private final InstanceContainer instanceContainer;
    private final MapData parkourMap;
    private Integer lastCheckpoint;
    private Long startTime;
    private final Sidebar sidebar;
    private final Task scoreboardTask;
    private MapScore bestScore;
    private Pos lastCheckpointPos;
    private final DimensionType dimensionType;
    private Boolean quitting;

    public CheckpointMapPlayer(Player player, MapData parkourMap) throws IOException, NBTException {
        quitting = false;
        this.player = player;
        this.dimensionType = DimensionType.builder(NamespaceID.from("instinctia:"+UUID.randomUUID()))
                .ambientLight(1.0f)
                //.skylightEnabled(true)
                //.fixedTime(1000L)
                .build();
        MinecraftServer.getDimensionTypeManager().addDimension(dimensionType);
        this.instanceContainer = new InstanceContainer(UUID.randomUUID(), this.dimensionType);
        this.parkourMap = parkourMap;
        this.lastCheckpointPos = null;

        CompletableFuture.supplyAsync(() -> {
            JsonObject score = JsonParser.parseString(Unirest.get("http://"+Server.CENTRALC_ENDPOINT+":8080"+"/player/"+player.getUuid()+"/score/"+parkourMap.getUuid()+"?token="+Server.CENTRALC_TOKEN)
                    .asString()
                    .getBody()).getAsJsonObject();

            if(score.has("error")){
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Error fetching best score: <gray>"+score.toString()));
                return null;
            }

            return new MapScore(score);
        }).thenAccept((bestScore) -> {
            this.bestScore = bestScore;
        });

        this.lastCheckpoint = null;
        this.startTime = null;

        try {
            Server.minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket("maps")
                    .object(parkourMap.getUuid()+".tnt")
                    .overwrite(true)
                    .filename("worlds\\"+parkourMap.getUuid()+".tnt")
                    .build());
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException | IllegalArgumentException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>An error occurred loading the map editor. Please try again, and if the issue persists contact staff."));
            throw new RuntimeException(e);
        }
        /**
        try (TarArchiveInputStream tarIn = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream("worlds\\"+parkourMap.getUuid()+".tar")))) {
            TarArchiveEntry entry;
            while ((entry = tarIn.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File outputFile = new File("worlds\\"+parkourMap.getUuid(), entry.getName());
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                byte[] buffer = new byte[4096];
                int bytesRead;
                try (var outputStream = new java.io.FileOutputStream(outputFile)) {
                    while ((bytesRead = tarIn.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
            tarIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
        this.instanceContainer.setChunkLoader(new TNTLoader(this.instanceContainer, new FileTNTSource(Path.of("worlds\\"+parkourMap.getUuid()+".tnt")), new Pos(0.0, 0.0, 0.0)));

        MinecraftServer.getInstanceManager().registerInstance(this.instanceContainer);

        this.sidebar = new Sidebar(MiniMessage.miniMessage().deserialize("<aqua><bold>Cavelet"));
        this.sidebar.createLine(new Sidebar.ScoreboardLine("l0", Component.empty(),5));
        this.sidebar.createLine(new Sidebar.ScoreboardLine("l1", Component.empty(),4));
        this.sidebar.createLine(new Sidebar.ScoreboardLine("l2", Component.empty(),3));
        this.sidebar.createLine(new Sidebar.ScoreboardLine("l3", Component.empty(),2));
        this.sidebar.createLine(new Sidebar.ScoreboardLine("l4", Component.empty(),1));
        this.sidebar.createLine(new Sidebar.ScoreboardLine("l5", Component.empty(),0));

        this.instanceContainer.getWorldBorder().setCenter(1f, 1f);
        this.instanceContainer.getWorldBorder().setDiameter(255);

        PlayerLeave.add(player, this::destroy);

        this.instanceContainer.eventNode().addListener(RemoveEntityFromInstanceEvent.class, (event) -> {
            Server.playersInSession.remove(player);
            if(event.getEntity() instanceof Player && !quitting) {
                quit();
            }
        });

        this.instanceContainer.eventNode().addListener(AddEntityToInstanceEvent.class, (event) -> {
            if(event.getEntity() instanceof Player){

                Player p = (Player) event.getEntity();

                Server.playersInSession.put(player, this);

                p.getInventory().clear();
                p.setFlying(false);
                p.setGameMode(GameMode.ADVENTURE);

                this.player.getInventory().setItemStack(8, Server.quitItem);
                this.player.getInventory().setItemStack(7, Server.restartItem);
                this.player.getInventory().setItemStack(6, Server.lastCheckpointItem);

                /**
                p.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<green><bold>Checkpoint Map"), MiniMessage.miniMessage().deserialize("<gray>Walk on the checkpoints <aqua>(Beacons)<gray>."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ofMillis(400))));
                MinecraftServer.getSchedulerManager().buildTask(() -> {
                    p.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<green><bold>Checkpoint Map"), MiniMessage.miniMessage().deserialize("<gray>Use your hotbar to navigate the map."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ofMillis(400))));
                }).delay(Duration.ofSeconds(3)).schedule();
                 */
                p.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<aqua><bold><map_name>", Placeholder.unparsed("map_name", parkourMap.getName())), MiniMessage.miniMessage().deserialize("<gray>Walk on the pressure plate to start."), Title.Times.times(Duration.ZERO, Duration.ofHours(100), Duration.ZERO)));
                this.sidebar.addViewer(player);
            }
        });

        this.instanceContainer.eventNode().addListener(PlayerUseItemEvent.class, (event) -> {
            event.setCancelled(true);
            if(event.getItemStack().material()== Material.ENDER_PEARL){
                restart();
            }else if(event.getItemStack().material()== Material.ENDER_EYE){
                tpLastCheckpoint();
            }else if(event.getItemStack().material()== Material.BARRIER){
                quit();
            }
        });

        double pixelOffset = 0.0625;

        this.instanceContainer.eventNode().addListener(PlayerMoveEvent.class, (event) -> {

            if(!(parkourMap.getCheckpoints().size()>=1)) return;

            Pos middle = event.getNewPosition();

            Pos[] corners = {
                    middle.add(0.3-pixelOffset , 0, 0.3-pixelOffset),
                    middle.add(0.3-pixelOffset, 0, -0.3+pixelOffset),
                    middle.add(-0.3+pixelOffset, 0, 0.3-pixelOffset),
                    middle.add(-0.3+pixelOffset, 0, -0.3+pixelOffset)
            };

            for(Pos corner : corners) {
                if(lastCheckpoint==null && corner.sameBlock(parkourMap.getCheckpoints().get(0))){
                    lastCheckpointPos = event.getNewPosition();
                    lastCheckpoint = 1;
                    startTime = System.currentTimeMillis();
                    player.clearTitle();
                    player.showTitle(Title.title(Component.empty(), MiniMessage.miniMessage().deserialize("<green>Map Started"), Title.Times.times(Duration.ZERO, Duration.ofMillis(700), Duration.ZERO)));
                    player.playSound(Sound.sound((SoundEvent.BLOCK_BEACON_ACTIVATE), Sound.Source.PLAYER, 1f, 1f));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<aqua>(!) Map started"));
                    return;
                }
                else if(lastCheckpoint!=null && (lastCheckpoint+1)<parkourMap.getCheckpoints().size() && corner.sameBlock(parkourMap.getCheckpoints().get(lastCheckpoint)) && event.getNewPosition().y() < middle.blockY() + pixelOffset){
                    lastCheckpointPos = event.getNewPosition();
                    lastCheckpoint+=1;

                    player.playSound(Sound.sound((SoundEvent.ENTITY_ARROW_HIT_PLAYER), Sound.Source.PLAYER, 1f, 1f));
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>(!) Checkpoint #"+lastCheckpoint+" reached"));

                    return;
                }
                else if(lastCheckpoint!=null && (lastCheckpoint+1)==parkourMap.getCheckpoints().size() && corner.sameBlock(parkourMap.getCheckpoints().get(lastCheckpoint)) && event.getNewPosition().y() < middle.blockY() + pixelOffset){
                    lastCheckpoint+=1;

                    // finished map
                    long timeFinish = System.currentTimeMillis();
                    event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>(!) Map finished in "+TextUtil.formatTime(timeFinish-startTime)));
                    finish(timeFinish);

                    for (int x=0;x<20;x++) {
                        ThreadLocalRandom random = ThreadLocalRandom.current();
                        List<FireworkEffect> effects = List.of(
                                new FireworkEffect(
                                        false,
                                        false,
                                        FireworkEffectType.values()[random.nextInt(0, 5)],
                                        List.of(new net.minestom.server.color.Color(Color.HSBtoRGB(random.nextFloat(), 1f, 1f))),
                                        List.of(new net.minestom.server.color.Color(Color.HSBtoRGB(random.nextFloat(), 1f, 1f)))
                                )
                        );

                        FireworkUtils.showFirework(Set.of(player), this.instanceContainer, new Pos((player.getPosition().x()+x), (player.getPosition().y()), (player.getPosition().z())), effects);
                    }

                    return;
                }
            }
        });

        this.instanceContainer.eventNode().addListener(InventoryPreClickEvent.class, (event) -> {
            event.setCancelled(true);
        });

        this.scoreboardTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
                    this.sidebar.updateLineContent("l0", MiniMessage.miniMessage().deserialize(""));
                    this.sidebar.updateLineContent("l1", MiniMessage.miniMessage().deserialize("<white><map_name>", Placeholder.unparsed("map_name", parkourMap.getName())));
                    this.sidebar.updateLineContent("l2", MiniMessage.miniMessage().deserialize("<gray> - <white>Checkpoints <aqua><current_checkpoint><gray>/<aqua><total_checkpoints>", Placeholder.unparsed("current_checkpoint",lastCheckpoint==null?"-":String.valueOf(lastCheckpoint)), Placeholder.unparsed("total_checkpoints", String.valueOf(parkourMap.getCheckpoints().size()))));
                    this.sidebar.updateLineContent("l3", MiniMessage.miniMessage().deserialize("<gray> - <white>Elapsed <aqua><time_status>", Placeholder.unparsed("time_status", startTime==null?"N/A":TextUtil.formatTime(System.currentTimeMillis()-startTime))));
                    this.sidebar.updateLineContent("l4", MiniMessage.miniMessage().deserialize(""));
                    this.sidebar.updateLineContent("l5", MiniMessage.miniMessage().deserialize("<aqua><u>cavelet.net"));
        })
                .repeat(Duration.ofMillis(55))
                .schedule();

        player.setInstance(this.instanceContainer, parkourMap.getSpawnPoint());

    }

    @Override
    public void quit(){
        quitting = true;
        this.player.setGameMode(GameMode.ADVENTURE);
        this.scoreboardTask.cancel();
        this.player.getInventory().clear();
        this.sidebar.removeViewer(player);
        if(this.instanceContainer==null) return;
        player.setInstance(Server.LOBBY, new Pos(84.8, 61.0, 84.0, -30.3F, 0.0F));
        player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<red>Exiting"), MiniMessage.miniMessage().deserialize("<gray>Leaving map"), Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ZERO)));
        destroy();
    }

    private void restart(){
        this.lastCheckpoint = null;
        this.startTime = null;
        this.lastCheckpointPos = null;
        this.player.teleport(parkourMap.getSpawnPoint());
    }

    private void tpLastCheckpoint(){
        if(lastCheckpointPos!=null) {
            this.player.teleport(this.lastCheckpointPos);
        }else{
            this.player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>You have not reached a checkpoint yet"));
        }
    }

    private void finish(long finish){
        MapScore newScore = new MapScore(
                player.getUuid(),
                parkourMap.getUuid(),
                System.currentTimeMillis(),
                finish-startTime
        );

        if(bestScore==null){
            bestScore = newScore;
            player.sendMessage(MiniMessage.miniMessage().deserialize("<light_purple>(!) <white>You have completed the map."));
            player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<light_purple><bold>Map Completed"), MiniMessage.miniMessage().deserialize("<gray>You have completed this map for the first time")));
            CompletableFuture.runAsync(() -> {
                String result = Unirest.post("http://"+ Server.CENTRALC_ENDPOINT+":8080"+"/score"+"?token="+Server.CENTRALC_TOKEN)
                        .header("Content-Type", "application/json")
                        .body(bestScore.toJson().toString())
                        .asString()
                        .getBody();
                //player.sendMessage(MiniMessage.miniMessage().deserialize("<aqua>POST /score response: "+result));
            });
        }else if(bestScore.getTotalTime()>newScore.getTotalTime()){
            bestScore = newScore;
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold>(!) <white>You have beat your best score for this map."));
            player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<gold><bold>Personal Record"), MiniMessage.miniMessage().deserialize("<gray>You beat your personal record for this map")));
            CompletableFuture.runAsync(() -> {
                String result = Unirest.post("http://"+ Server.CENTRALC_ENDPOINT+":8080"+"/score"+"?token="+Server.CENTRALC_TOKEN)
                        .header("Content-Type", "application/json")
                        .body(bestScore.toJson().toString())
                        .asString()
                        .getBody();
                //player.sendMessage(MiniMessage.miniMessage().deserialize("<aqua>POST /score response: "+result));
            });
        }

        restart();

    }

    private void destroy(){
        MinecraftServer.getDimensionTypeManager().removeDimension(this.dimensionType);
        MinecraftServer.getInstanceManager().unregisterInstance(this.instanceContainer);
    }



}
