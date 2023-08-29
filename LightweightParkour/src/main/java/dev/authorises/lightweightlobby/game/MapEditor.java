package dev.authorises.lightweightlobby.game;

import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.map.MapData;
import dev.authorises.lightweightlobby.util.FileUtil;
import dev.authorises.lightweightlobby.util.PlayerInput;
import dev.authorises.lightweightlobby.util.PlayerLeave;
import dev.authorises.lightweightlobby.util.TextUtil;
import dev.emortal.tnt.TNT;
import dev.emortal.tnt.TNTLoader;
import dev.emortal.tnt.source.FileTNTSource;
import io.minio.DownloadObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import kong.unirest.Unirest;
import net.hollowcube.block.placement.HCPlacementRules;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.TransactionOption;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.*;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class MapEditor extends ParkourPlayer {

    private final Player player;
    private final InstanceContainer instanceContainer;
    private final MapData parkourMap;
    private final Task halfSecondTask;
    private final DimensionType dimensionType;
    private final List<Block> checkpointMaterials = List.of(
            Block.STONE_PRESSURE_PLATE,
            Block.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            Block.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Block.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Block.OAK_PRESSURE_PLATE,
            Block.SPRUCE_PRESSURE_PLATE,
            Block.BIRCH_PRESSURE_PLATE,
            Block.JUNGLE_PRESSURE_PLATE,
            Block.ACACIA_PRESSURE_PLATE,
            Block.DARK_OAK_PRESSURE_PLATE,
            Block.MANGROVE_PRESSURE_PLATE,
            Block.CRIMSON_PRESSURE_PLATE,
            Block.WARPED_PRESSURE_PLATE
    );

    private boolean settingDescription = false;
    private boolean saving = false;

    public MapEditor(Player player, MapData parkourMap) throws IOException, NBTException {
        Long debug = System.currentTimeMillis();

        this.player = player;
        this.parkourMap = parkourMap;

        Boolean create = !parkourMap.getFirstSave();

        this.dimensionType = DimensionType.builder(NamespaceID.from("instinctia:"+UUID.randomUUID()))
                .ambientLight(1.0f)
                .build();

        MinecraftServer.getDimensionTypeManager().addDimension(this.dimensionType);

        if(create){
            this.instanceContainer = new InstanceContainer(UUID.randomUUID(), this.dimensionType);
            for(int x=-128;x<=128;x++){
                for(int z=-128;z<=128;z++){
                    this.instanceContainer.setBlock(x, -63, z, Block.BEDROCK);
                }
            }
            this.instanceContainer.setChunkLoader(new AnvilLoader("worlds\\"+parkourMap.getUuid()));
        }else{

            try {
                Server.minioClient.downloadObject(DownloadObjectArgs.builder()
                                .bucket("maps")
                                .object(parkourMap.getUuid()+".tnt")
                                .overwrite(true)
                                .filename("worlds\\"+parkourMap.getUuid()+".tnt")
                        .build());

                Long downloadTime = System.currentTimeMillis()-debug;

                player.sendMessage(MiniMessage.miniMessage().deserialize("Map download took: <aqua>"+downloadTime+"ms"));

            } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                     NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                     InternalException | IllegalArgumentException e) {
                end(false);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>An error occurred loading the map editor. Please try again, and if the issue persists contact staff."));
                throw new RuntimeException(e);
            }
            this.instanceContainer = new InstanceContainer(UUID.randomUUID(), this.dimensionType);

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

        }


        MinecraftServer.getInstanceManager().registerInstance(this.instanceContainer);

        this.instanceContainer.getWorldBorder().setCenter(1f, 1f);
        this.instanceContainer.getWorldBorder().setDiameter(255);

        this.instanceContainer.eventNode().addListener(AddEntityToInstanceEvent.class, (event) -> {
            if(event.getEntity() instanceof Player){
                Player p = (Player) event.getEntity();
                p.getInventory().clear();
                p.setFlying(true);
                p.setGameMode(GameMode.CREATIVE);
                Server.playersInSession.put(player, this);
                if(create) {
                    p.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<green><bold>Map Editor"), MiniMessage.miniMessage().deserialize("<gray>Welcome to the map editor. For help use the <red>Map Settings<gray>"), Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ofMillis(400))));
                }
            }
        });

        this.instanceContainer.eventNode().addListener(PlayerBlockBreakEvent.class, (event) -> {
            if(event.getBlock().compare(Block.BEDROCK) && event.getBlockPosition().blockY()==-63){
                event.setCancelled(true);
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>You cannot break the bottom layer of the world."));
            }
        });

        this.instanceContainer.eventNode().addListener(PlayerUseItemEvent.class, (event) -> {
            if(event.getItemStack().material()== Material.COMPASS){
                settingsGui();
            }
        });

        this.instanceContainer.eventNode().addListener(PlayerUseItemOnBlockEvent.class, (event) -> {
            if(event.getItemStack().material()== Material.COMPASS){
                settingsGui();
            }
        });

        instanceContainer.eventNode().addListener(PlayerBlockPlaceEvent.class, (event) -> {
            if(this.checkpointMaterials.contains(event.getBlock())){
                if(event.getPlayer().isSneaking()) return;
                if(parkourMap.getCheckpoints().size()>=54){
                    event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>Your map has reached the maximum amount of <yellow>checkpoints<gray> currently allowed."));
                    event.setCancelled(true);
                    return;
                }
                parkourMap.getCheckpoints().add(new Pos(event.getBlockPosition()));
                event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>(!) <gray>You have placed a new <yellow>checkpoint<gray>, you can remove it or change the order in the <red>Map Settings<gray>."));
            }
        });


        instanceContainer.eventNode().addListener(PlayerBlockBreakEvent.class, (event) -> {
            if(checkpointMaterials.contains(event.getBlock())){
                parkourMap.getCheckpoints().forEach((pos) -> {
                    if(pos.samePoint(event.getBlockPosition())){
                        event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>If you want to delete a <yellow>checkpoint<gray>, use the <red>Map Settings<gray>."));
                        event.setCancelled(true);
                    }
                });
            }
        });

        instanceContainer.eventNode().addListener(PlayerBlockInteractEvent.class, (event) -> {
            if(checkpointMaterials.contains(event.getBlock())){
                for(int i =0;i<parkourMap.getCheckpoints().size();i++){
                    Pos pos = parkourMap.getCheckpoints().get(i);
                    if(pos.samePoint(event.getBlockPosition())){
                        event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>This is <yellow>"+(i==0?"the start checkpoint":"Checkpoint #"+(i+1))+"<gray>."));
                        event.setCancelled(true);
                    }
                };
            }
        });

        PlayerLeave.add(player, this::destroy);

        this.instanceContainer.eventNode().addListener(PlayerChatEvent.class, (event) -> {
            if(settingDescription){
                event.setCancelled(true);
                settingDescription = false;
                String description = event.getMessage();
                if(description.equalsIgnoreCase("exit")) return;
                if(description.contains("<") || description.contains(">")){
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>Description must not contain minimessage components."));
                    settingsGui();
                    return;
                }

                if(description.length()>=300){
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>Description can not be longer than 300 characters."));
                    settingsGui();
                    return;
                }
                parkourMap.setDescription(description);
                settingsGui();
            }
        });

        this.instanceContainer.eventNode().addListener(ItemDropEvent.class, (event) -> {
            event.setCancelled(true);
        });

        this.instanceContainer.eventNode().addListener(RemoveEntityFromInstanceEvent.class, (event) -> {
            if(event.getEntity() instanceof Player){
                Server.playersInSession.remove((Player) event.getEntity(), this);
            }
            if(!saving) this.destroy();
        });

        this.halfSecondTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            this.player.sendActionBar(Component.text("You are editing the map", NamedTextColor.LIGHT_PURPLE));
            //this.player.sendActionBar(Component.text("Checkpoint count: "+parkourMap.getCheckpoints().size(), NamedTextColor.LIGHT_PURPLE));
            int i = 0;
            for(ItemStack stack : player.getInventory().getItemStacks()){
                if(stack.isSimilar(Server.settingsItem)) i++;
            }
            if(i==0){
                this.player.getInventory().takeItemStack(Server.settingsItem, TransactionOption.ALL);
                this.player.getInventory().setItemStack(8, Server.settingsItem);
                this.player.getInventory().update();
            }
        }, TaskSchedule.millis(0), TaskSchedule.millis(500));


        player.setInstance(this.instanceContainer, parkourMap.getSpawnPoint()).thenRun(() -> {
            Long timeTaken = System.currentTimeMillis()-debug;
            player.sendMessage(MiniMessage.miniMessage().deserialize("Map load took: <aqua>"+timeTaken+"ms"));
        });

    }

    private void updateCheckpointsGui(Inventory checkpoints){
        checkpoints.getInventoryConditions().clear();
        checkpoints.clear();
        for(int i=0;i<parkourMap.getCheckpoints().size();i++){
            Pos checkpoint = parkourMap.getCheckpoints().get(i);
            checkpoints.setItemStack(i,
                    ItemStack.builder(Material.BEACON)
                            .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>"+(i==0?"Start":"Checkpoint #"+(i+1))))
                            .lore(List.of(
                                    MiniMessage.miniMessage().deserialize("<!italic><gray> X "+checkpoint.x()),
                                    MiniMessage.miniMessage().deserialize("<!italic><gray> Y "+checkpoint.y()),
                                    MiniMessage.miniMessage().deserialize("<!italic><gray> Z "+checkpoint.z()),
                                    MiniMessage.miniMessage().deserialize(""),
                                    MiniMessage.miniMessage().deserialize("<!italic><gray>Left click to swap to left"),
                                    MiniMessage.miniMessage().deserialize("<!italic><gray>Right click to swap to right"),
                                    MiniMessage.miniMessage().deserialize(""),
                                    MiniMessage.miniMessage().deserialize("<!italic><red><u>Drop to delete")
                            ))
                            .build());
            int finalI = i;
            checkpoints.addInventoryCondition((clicker, slot, clickType, result) -> {
                if(slot!= finalI) return;
                switch (clickType){
                    case DROP:{
                        instanceContainer.setBlock((int)checkpoint.x(), (int)checkpoint.y(), (int)checkpoint.z(), Block.AIR);
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <yellow>"+(finalI==0?"Start checkpoint":"Checkpoint #"+(finalI+1))+"<gray> was deleted."));
                        if(finalI==0 && parkourMap.getCheckpoints().size()>1){
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(WARNING) <gray> The start checkpoint has now changed to what was previously <yellow>checkpoint #1<gray>, make sure that it is still reachable."));
                        }
                        parkourMap.getCheckpoints().remove(finalI);
                        updateCheckpointsGui(checkpoints);
                        break;
                    }
                    case LEFT_CLICK:{
                        if (!(finalI >0)){
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>This is already the start checkpoint"));
                            break;
                        }
                        Pos old = parkourMap.getCheckpoints().set(finalI-1, checkpoint);
                        parkourMap.getCheckpoints().set(finalI, old);
                        updateCheckpointsGui(checkpoints);
                        break;
                    }
                    case RIGHT_CLICK:{
                        if (!(finalI < parkourMap.getCheckpoints().size()-1)){
                            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>This is already the last checkpoint"));
                            break;
                        }
                        Pos old = parkourMap.getCheckpoints().set(finalI+1, checkpoint);
                        parkourMap.getCheckpoints().set(finalI, old);
                        updateCheckpointsGui(checkpoints);
                        break;
                    }
                }

                result.setCancel(true);
            });
        }
        if(parkourMap.getCheckpoints().size()<54){
            for(int i=0;i<54-parkourMap.getCheckpoints().size();i++){
                checkpoints.setItemStack(53-i,
                        ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
                                .displayName(MiniMessage.miniMessage().deserialize("<!italic><gray>You can add a checkpoint by placing down a <aqua>Pressure Plate<gray>."))
                                .build());
                checkpoints.addInventoryCondition((clicker, slot, clickType, result) -> {
                    result.setCancel(true);
                });
            }
        }

        checkpoints.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=-999) return;
            player.closeInventory();
        });
    }

    private void checkpointsGui(){

        Inventory checkpoints = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize("<red>Map Checkpoints"));
        updateCheckpointsGui(checkpoints);
        player.openInventory(checkpoints);
    }

    private void settingsGui(){

        Inventory settings = new Inventory(InventoryType.CHEST_1_ROW, MiniMessage.miniMessage().deserialize("<red>Map Settings"));

        settings.addItemStack(ItemStack.builder(Material.CHEST)
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><green>Save and exit <gray>(Click)"))
                .build());

        settings.addItemStack(ItemStack.builder(Material.BEACON)
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><yellow>Manage checkpoints <gray>(Click)"))
                .build());

        settings.addItemStack(ItemStack.builder(Material.NAME_TAG)
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>Change map name <gray>(Click)"))
                .lore(List.of(
                        MiniMessage.miniMessage().deserialize("<!italic><gray>Current name: <aqua>"+parkourMap.getName())
                ))
                .build());

        List<Component> l = new ArrayList<>();
        l.add(MiniMessage.miniMessage().deserialize("<!italic><gray>Current description: "));
        l.add(MiniMessage.miniMessage().deserialize(""));
        TextUtil.splitText(parkourMap.getDescription(), "<!italic><gray>", 55).forEach((s) -> {
            l.add(MiniMessage.miniMessage().deserialize(s));
        });

        settings.addItemStack(ItemStack.builder(Material.FEATHER)
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>Change map description <gray>(Click)"))
                .lore(l)
                .build());

        settings.addItemStack(ItemStack.builder(parkourMap.getMaterial())
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><gold>Change map icon <gray>(Click)"))
                .build());

        settings.addItemStack(ItemStack.builder(Material.ENDER_PEARL)
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><light_purple>Set spawn <gray>(Click)"))
                .build());

        if(parkourMap.getPublic()){
            settings.addItemStack(ItemStack.builder(Material.LIME_STAINED_GLASS_PANE)
                    .displayName(MiniMessage.miniMessage().deserialize("<!italic><green>Public <gray>(Click to toggle)"))
                    .build());
        }else{
            settings.addItemStack(ItemStack.builder(Material.RED_STAINED_GLASS_PANE)
                    .displayName(MiniMessage.miniMessage().deserialize("<!italic><red>Private <gray>(Click to toggle)"))
                    .build());
        }

        settings.setItemStack(8, ItemStack.builder(Material.BARRIER)
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><red>Exit without saving <gray>(Click)"))
                .build());

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=-999) return;
            player.closeInventory();
            result.setCancel(true);
        });

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=0) return;
            player.closeInventory();
            end(true);
            result.setCancel(true);
        });

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=1) return;
            player.closeInventory();
            checkpointsGui();
            result.setCancel(true);
        });


        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=2) return;
            player.closeInventory();
            PlayerInput.doSignInput(player, "Enter new name", (name) -> {
                if(Pattern.compile("[^a-zA-Z0-9]").matcher(name).find() || name.length()>10 || name.length()<3){
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>Map names must only contain letters and numbers and be 10 or less characters long."));
                }else{
                    parkourMap.setName(name);
                }
                settingsGui();
            });
        });

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=3) return;
            player.closeInventory();
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>(!) <gray>Enter the new description in chat, or enter exactly 'exit' to cancel."));
            settingDescription = true;
            result.setCancel(true);
        });

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=4) return;
            result.setCancel(true);
            PlayerInput.doIconInput(player, (icon) -> {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>(!) <gray>The icon has been updated"));
                parkourMap.setMaterial(icon);
                settingsGui();
            });
        });

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=5) return;
            parkourMap.setSpawnPoint(player.getPosition());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>(!) <gray>The spawn point was updated"));
            settingsGui();
            result.setCancel(true);
        });

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=6) return;

            parkourMap.setPublic(!parkourMap.getPublic());

            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>(!) <gray>The publicity of the map was changed to "+(parkourMap.getPublic()?"<green>Public":"<red>Private")+"<gray>."));

            settingsGui();
            result.setCancel(true);
        });

        settings.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=8) return;
            player.closeInventory();
            result.setCancel(true);
            end(false);
        });

        player.openInventory(settings);
    }

    @Override
    public void quit(){
        end(false);
    }

    public void end(Boolean save){
        saving = true;
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setFlying(false);
        this.halfSecondTask.cancel();
        this.player.getInventory().clear();
        if(this.instanceContainer==null) return;
        player.setInstance(Server.LOBBY, new Pos(84.8, 61.0, 84.0, -30.3F, 0.0F));

        if(save) {
            player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<green>Saving"), MiniMessage.miniMessage().deserialize("<gray>Your map is being saved..."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ZERO)));
            MinecraftServer.getSchedulerManager().buildTask(() -> {
                this.instanceContainer.setChunkLoader(new AnvilLoader("worlds\\"+parkourMap.getUuid()));
                try {
                    this.instanceContainer.saveChunksToStorage().get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                TNT.INSTANCE.convertAnvilToTNT(Path.of("worlds\\"+parkourMap.getUuid()), new FileTNTSource(Path.of("worlds\\"+parkourMap.getUuid()+".tnt")));
                destroy();

                parkourMap.setFirstSave(true);

                String s = Unirest.post("http://" + Server.CENTRALC_ENDPOINT + ":8080" + "/map/" + parkourMap.getUuid() + "?token=" + Server.CENTRALC_TOKEN)
                        .header("Content-Type", "application/json")
                        .body(parkourMap.toJson())
                        .asString()
                        .getBody();

                try {
                    TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream("worlds\\" + parkourMap.getUuid() + ".tar"));
                    out.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

                    // Add files to the archive
                    FileUtil.addDirectoryToTar(out, new File("worlds\\" + parkourMap.getUuid()), "");

                    // Close the archive
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Server.minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket("maps")
                                    .object(parkourMap.getUuid().toString() + ".tar")
                                    .filename("worlds\\" + parkourMap.getUuid().toString() + ".tar")
                                    .build());
                    Server.minioClient.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket("maps")
                                    .object(parkourMap.getUuid().toString() + ".tnt")
                                    .filename("worlds\\" + parkourMap.getUuid().toString() + ".tnt")
                                    .build());
                } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                         InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                         XmlParserException e) {
                    throw new RuntimeException(e);
                }
            }).executionType(ExecutionType.ASYNC).schedule();
        }else{
            player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<red>Exiting"), MiniMessage.miniMessage().deserialize("<gray>Leaving map without saving"), Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ZERO)));
            destroy();
        }
    }

    public void destroy(){
        MinecraftServer.getDimensionTypeManager().removeDimension(this.dimensionType);
        MinecraftServer.getInstanceManager().unregisterInstance(instanceContainer);
    }


}
