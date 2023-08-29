package dev.authorises.lightweightlobby;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.platform.PlatformReadyEvent;
import com.sk89q.worldedit.event.platform.PlatformsRegisteredEvent;
import com.sk89q.worldedit.internal.block.BlockStateIdAccess;
import com.sk89q.worldedit.registry.state.Property;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.item.ItemType;
import dev.authorises.lightweightlobby.commands.Commands;
import dev.authorises.lightweightlobby.game.MapEditor;
import dev.authorises.lightweightlobby.game.ParkourPlayer;
import dev.authorises.lightweightlobby.game.ParkourSession;
import dev.authorises.lightweightlobby.map.MapData;
import dev.authorises.lightweightlobby.sockets.SocketManager;
import dev.authorises.lightweightlobby.util.InventoryClose;
import dev.authorises.lightweightlobby.util.PlayerLeave;
import dev.authorises.lightweightlobby.worldedit.platform.MinestomPlatform;
import dev.authorises.lightweightlobby.worldedit.platform.adapters.MinestomAdapter;
import dev.authorises.lightweightlobby.worldedit.platform.config.WorldEditConfiguration;
import gg.astromc.slimeloader.loader.SlimeLoader;
import gg.astromc.slimeloader.source.SlimeSource;
import io.minio.MinioClient;
import kong.unirest.Unirest;
import net.hollowcube.block.placement.HCPlacementRules;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.*;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Server {

    public static Gson gson = new Gson();

    public static InstanceContainer LOBBY;

    public static SocketManager socketManager;
    public static MinioClient minioClient;

    public static HashMap<Player, ParkourPlayer> playersInSession;

    public static HashMap<UUID, ParkourSession> playerSessions;

    public static String VELOCITY_SECRET;
    public static Integer LISTEN_PORT;
    public static String LISTEN_HOST;
    public static String CENTRALC_ENDPOINT;
    public static String CENTRALC_TOKEN;
    public static String SERVER_TYPE;
    public static String PUBLIC_IP;
    public static Integer PUBLIC_PORT;
    public static String TEMP_WORLD_DIR;
    public static String MINIO_ENDPOINT;
    public static String MINIO_ACCESS_KEY;
    public static String MINIO_SECRET_KEY;

    public static WorldEditConfiguration worldEditConfig;

    public static ItemStack settingsItem = ItemStack.builder(Material.COMPASS)
            .displayName(MiniMessage.miniMessage().deserialize("<!italic><red>Map Settings"))
            .build();

    public static ItemStack lastCheckpointItem = ItemStack.builder(Material.ENDER_EYE)
            .displayName(MiniMessage.miniMessage().deserialize("<!italic><yellow>Last Checkpoint"))
            .build();

    public static ItemStack restartItem = ItemStack.builder(Material.ENDER_PEARL)
            .displayName(MiniMessage.miniMessage().deserialize("<!italic><red>Restart"))
            .build();

    public static ItemStack quitItem = ItemStack.builder(Material.BARRIER)
            .displayName(MiniMessage.miniMessage().deserialize("<!italic><red>Quit"))
            .build();

    public static void main(String[] args) throws URISyntaxException {
        System.setProperty("minestom.chunk-view-distance", "16");
        System.setProperty("minestom.entity-view-distance", "2");

        System.out.println(args);

        if(args.length==1) {
            VELOCITY_SECRET = "";
            LISTEN_PORT = ""
            LISTEN_HOST = "";
            CENTRALC_ENDPOINT = "";
            CENTRALC_TOKEN = "";
            SERVER_TYPE = "";
            PUBLIC_IP = "";
            PUBLIC_PORT = ""
            TEMP_WORLD_DIR = "";
            MINIO_ENDPOINT="";
            MINIO_ACCESS_KEY="";
            MINIO_SECRET_KEY="";
        }else{
            List.of(
                    "VELOCITY_SECRET",
                    "LISTEN_PORT",
                    "LISTEN_HOST",
                    "CENTRALC_ENDPOINT",
                    "CENTRALC_TOKEN",
                    "SERVER_TYPE",
                    "PUBLIC_IP",
                    "PUBLIC_PORT",
                    "TEMP_WORLD_DIR",
                    "MINIO_ENDPOINT",
                    "MINIO_ACCESS_KEY",
                    "MINIO_SECRET_KEY").forEach((environmentVariable) -> {
                        if(System.getenv(environmentVariable)==null){
                            System.out.println("Missing environment variable: "+environmentVariable);
                            System.exit(0);
                        }
                VELOCITY_SECRET = System.getenv("VELOCITY_SECRET");
                LISTEN_PORT = Integer.valueOf(System.getenv("LISTEN_PORT"));
                LISTEN_HOST = System.getenv("LISTEN_HOST");
                CENTRALC_ENDPOINT = System.getenv("CENTRALC_ENDPOINT");
                CENTRALC_TOKEN = System.getenv("CENTRALC_TOKEN");
                SERVER_TYPE = System.getenv("SERVER_TYPE");
                PUBLIC_IP = System.getenv("PUBLIC_IP");
                PUBLIC_PORT = Integer.valueOf(System.getenv("PUBLIC_PORT"));
                TEMP_WORLD_DIR = System.getenv("TEMP_WORLD_DIR");
                MINIO_ENDPOINT = System.getenv("MINIO_ENDPOINT");
                MINIO_ACCESS_KEY = System.getenv("MINIO_ACCESS_KEY");
                MINIO_SECRET_KEY = System.getenv("MINIO_SECRET_KEY");
            });



        }

        playersInSession = new HashMap<>();
        playerSessions = new HashMap<>();

        // Initialise server
        MinecraftServer server = MinecraftServer.init();



        LOBBY = MinecraftServer.getInstanceManager().createInstanceContainer(DimensionType.OVERWORLD);
        LOBBY.setChunkLoader(new SlimeLoader(LOBBY, new SlimeSource() {
            @NotNull
            @Override
            public InputStream load() {
                return Objects.requireNonNull(getClass().getResourceAsStream("/lobby.slime"),
                        "Slime world missing!");
            }

            @NotNull
            @Override
            public OutputStream save() {
                return null;
            }
        }, true));

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, (event) -> {
            event.setSpawningInstance(LOBBY);
            event.getPlayer().setRespawnPoint(new Pos(84.8, 61.0, 84.0, -30.3F, 0.0F));
            event.getPlayer().setGameMode(GameMode.ADVENTURE);

            if(playerSessions.containsKey(event.getPlayer().getUuid())){
                ParkourSession session = playerSessions.remove(event.getPlayer().getUuid());
                try {
                    session.load(event.getPlayer());
                } catch (IOException | NBTException e) {
                    event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>An error occurred loading your parkour session."));
                    throw new RuntimeException(e);
                }
            }

        });


        MinecraftServer.getCommandManager().register(Commands.SHUTDOWN);
        MinecraftServer.getCommandManager().register(Commands.RESTART);
        MinecraftServer.getCommandManager().register(Commands.EDITOR);
        MinecraftServer.getCommandManager().register(Commands.PLAY);

        VelocityProxy.enable(VELOCITY_SECRET);

        MinecraftServer.LOGGER.info("Listening on " + LISTEN_HOST + ":" + LISTEN_PORT);

        minioClient =
                MinioClient.builder()
                        .endpoint(MINIO_ENDPOINT)
                        .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
                        .build();


        worldEditConfig = new WorldEditConfiguration();

        MinestomAdapter.platform = new MinestomPlatform();

        WorldEdit.getInstance().getPlatformManager().register(MinestomAdapter.platform);
        WorldEdit.getInstance().getEventBus().post(new PlatformReadyEvent(MinestomAdapter.platform));
        WorldEdit.getInstance().getEventBus().post(new PlatformsRegisteredEvent());
        WorldEdit.getInstance().getConfiguration().defaultChangeLimit=400000;
        WorldEdit.getInstance().getConfiguration().maxBrushRadius=20;
        WorldEdit.getInstance().getConfiguration().calculationTimeout=5;

        registerItems();
        registerBlocks();

        InventoryClose.init();
        PlayerLeave.init();

        // Start server
        server.start(LISTEN_HOST, LISTEN_PORT);

        // Connect to CentralC
        socketManager = new SocketManager();

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            JsonObject message = new JsonObject();
            JsonObject data = new JsonObject();
            JsonArray players = new JsonArray();

            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> {
                JsonObject playerObj = new JsonObject();
                playerObj.addProperty("uuid", player.getUuid().toString());
                playerObj.addProperty("username", player.getUsername());
                players.add(playerObj);
            });

            data.add("players", players);

            message.addProperty("message", "server-players-list");
            message.add("data", data);

            if(socketManager.socket.isOpen()) {
                socketManager.socket.send(message.toString());
            }
        }, TaskSchedule.tick(1), TaskSchedule.millis(500));

        HCPlacementRules.init();

    }

    private static void registerItems() {
        for (Material itemType : Material.values()) {
            String id = itemType.name();
            if (!ItemType.REGISTRY.keySet().contains(id)) {
                ItemType.REGISTRY.register(id, new ItemType(id));
            }
        }
    }

    private static void registerBlocks() {
        for(Block minestomBlock : Block.values()) {
            String id = minestomBlock.name();
            if (!BlockType.REGISTRY.keySet().contains(id)) {
                BlockType block = new BlockType(id, null);

                for(BlockState state : block.getAllStates()) {
                    SortedMap<String, String> stateMap = new TreeMap<>();
                    for(Map.Entry<Property<?>, Object> entry : state.getStates().entrySet()) {
                        stateMap.put(entry.getKey().getName(), entry.getValue().toString());
                    }
                    /*String[] stateStrings = new String[stateMap.size()];

                    int i=0;
                    for(Map.Entry<String, String> entry : stateMap.entrySet()) {
                        stateStrings[i++] = entry.getKey() + "=" + entry.getValue();
                    }*/

                    short stateId = minestomBlock.withProperties(stateMap).stateId();
                    BlockStateIdAccess.register(state, stateId);
                }
                BlockType.REGISTRY.register(id, block);
            }
        }
    }
}