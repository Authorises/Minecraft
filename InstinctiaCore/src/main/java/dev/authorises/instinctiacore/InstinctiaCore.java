package dev.authorises.instinctiacore;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import dev.authorises.instinctiacore.auth.TokenManager;
import dev.authorises.instinctiacore.commands.*;
import dev.authorises.instinctiacore.events.PostLogin;
import dev.authorises.instinctiacore.events.PreLogin;
import dev.authorises.instinctiacore.events.ServerConnect;
import dev.authorises.instinctiacore.sockets.SocketManager;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class InstinctiaCore extends Plugin {
    public static InstinctiaCore instance;
    public static ProxyServer proxy = ProxyServer.getInstance();
    public static SocketManager socket;
    public static String version = "VPC-beta1";
    public static ArrayList<UUID> connectingPlayers = new ArrayList<>();
    public static ArrayList<ServerInfo> servers;
    public static HashMap<UUID, Long> supporterExpires;
    public static MongoClient client;
    public static ArrayList<String> whitelist = new ArrayList<>();
    public static LuckPerms luckPerms;

    public static MongoCollection playerData;
    public static MongoCollection punishments;

    public static ArrayList<ProxiedPlayer> onlineButBanned = new ArrayList<>();
    public static HashMap<UUID, Boolean> staffMode = new HashMap<>();
    public static Gson gson = new Gson();
    private BungeeAudiences adventure;

    public @NonNull BungeeAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Cannot retrieve audience provider while plugin is not enabled");
        }
        return this.adventure;
    }



    @Override
    public void onEnable() {
        /**
        try{
            Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            TokenManager.token = configuration.getString("api-token");
        }catch (IOException e){
            ProxyServer.getInstance().getLogger().log(Level.SEVERE, "config.yml was not found - shutting down server");
            ProxyServer.getInstance().stop("config.yml not found for instinctiacore");
        }*/

        TokenManager.token = "internal";

        this.adventure = BungeeAudiences.create(this);
        instance = this;
        try {
            socket = new SocketManager();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        servers = new ArrayList<>();
        supporterExpires = new HashMap<>();

        this.luckPerms = LuckPermsProvider.get();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Settings());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Status());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Supporter());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishmentCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PlayerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Staff());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new DiscordCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HelpCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new FriendCommand());

        ProxyServer.getInstance().getPluginManager().registerListener(this, new PostLogin());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PreLogin());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ServerConnect());

        whitelist.add("3c38cf0c-56b7-473a-bc3a-d2bf48d8d956");
        whitelist.add("c57f5a15-0685-473c-b9c9-de1757609546");
        whitelist.add("c64f5678-09b1-413c-833e-d453492900b3");
        whitelist.add("76b60dc6-1379-42b9-a4b6-3754a147e9ef");
        whitelist.add("e49e04cb-cbed-42df-bddb-6c423a3830e9");
        whitelist.add("1e3141e2-62ad-404f-9820-14b0c35459ac");
        whitelist.add("054af877-6262-4c33-8177-9d3fd4802c09");

        ProxyServer.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                JsonObject message = new JsonObject();
                JsonObject data = new JsonObject();
                JsonArray players = new JsonArray();
                for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
                    JsonObject player = new JsonObject();
                    player.addProperty("uuid", p.getUniqueId().toString());
                    player.addProperty("username", p.getName());

                    players.add(player);
                }


                data.add("players", players);

                message.addProperty("message", "proxy-players-list");
                message.add("data", data);

                if(socket.socket.isOpen()) {
                    socket.socket.send(message.toString());
                }

            }
        }, 0, 500, TimeUnit.MILLISECONDS);

    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

}
