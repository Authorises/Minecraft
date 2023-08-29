package dev.authorises.vortechcore;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.authorises.vortechcore.auth.TokenManager;
import dev.authorises.vortechcore.commands.nukeCommand;
import dev.authorises.vortechcore.commands.playCommand;
import dev.authorises.vortechcore.events.chatEvent;
import dev.authorises.vortechcore.events.lobby.anti;
import dev.authorises.vortechcore.events.playerJoin;
import dev.authorises.vortechcore.events.playerLeave;
import dev.authorises.vortechcore.scoreboard.lobbyMainScoreboard;
import dev.authorises.vortechcore.sockets.SocketManager;
import dev.authorises.vortechcore.utilities.ColorUtils;
import dev.authorises.vortechcore.utils.Lag;
import dev.authorises.vortechcore.utils.TunnelUtils;
import lombok.NonNull;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

public final class VortechCore extends JavaPlugin {

    public static String version = "VortechCore-v1.01";
    public FileConfiguration config = this.getConfig();
    public static HashMap<String,Integer> onlineCount = new HashMap<>();
    public static String serverName ="Connecting";
    public static boolean telemetry;
    public static lobbyMainScoreboard sb;
    public static boolean nuke = false;
    public static LuckPerms luckPerms;
    public static HashMap<UUID, String> displayNames = new HashMap<>();
    public static HashMap<Player, Location> afklocs = new HashMap<>();
    public static HashMap<Player, Location> afklocs2 = new HashMap<>();
    public static SocketManager socketManager;
    public static VortechCore instance;
    public static String name = "connecting";

    @Override
    public void onEnable() {
        telemetry = false;
        saveDefaultConfig();
        try {
            socketManager = new SocketManager();
            this.luckPerms = LuckPermsProvider.get();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        TokenManager.token=getConfig().getString("accessToken");
        Bukkit.getLogger().info("\n\n\n\n\n\n\n\n\n\nTOKEN: "+ TokenManager.token);
        Bukkit.getPluginManager().registerEvents(new playerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new playerLeave(), this);
        getCommand("play").setExecutor(new playCommand());
        getCommand("nuke").setExecutor(new nukeCommand());
        if(this.getConfig().getString("serverType").equalsIgnoreCase("lm")){
            sb = new lobbyMainScoreboard();
            sb.init();
            Bukkit.getPluginManager().registerEvents(new chatEvent(), this);
            Bukkit.getPluginManager().registerEvents(new anti(), this);
        }else{
            int x=0;
            while(x<10){
                x++;
                Bukkit.getLogger().severe("Unknown server type!");
            }
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            JsonObject message = new JsonObject();
            JsonObject data = new JsonObject();
            JsonArray players = new JsonArray();
            for(Player p : Bukkit.getOnlinePlayers()){
                JsonObject player = new JsonObject();
                player.addProperty("uuid", p.getUniqueId().toString());
                player.addProperty("username", p.getName());

                players.add(player);
            }

            data.add("players", players);

            message.addProperty("message", "server-players-list");
            message.add("data", data);

            if(socketManager.socket.isOpen()) {
                socketManager.socket.send(message.toString());
            }
        }, 0L, 1L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Player p : Bukkit.getOnlinePlayers()){
                if(afklocs.containsKey(p)){
                    if(p.getLocation().equals(afklocs.get(p))){
                        afklocs2.put(p, p.getLocation());
                        afklocs.remove(p);
                        p.sendMessage(ColorUtils.format("&cAFK! &fIf you do not move, you will be placed in AFK in 30 seconds."));
                        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,  20F, 20F);
                    }else{
                        afklocs.remove(p);
                    }
                }else{
                    if(afklocs2.containsKey(p)){
                        if(p.getLocation().equals(afklocs2.get(p))){
                            afklocs2.remove(p);
                            TunnelUtils.transfer(p, "limbo");
                        }else{
                            afklocs2.remove(p);
                        }
                    }else{
                        afklocs.put(p, p.getLocation());
                    }
                }
            }
        }, 0L, 600L);
        instance=this;
    }

    @Override
    public void onDisable() {

    }
}
