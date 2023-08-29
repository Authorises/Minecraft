package dev.authorises.vortechcore.scoreboard;

import dev.authorises.vortechcore.VortechCore;
import dev.authorises.vortechcore.utilities.ColorUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class lobbyMainScoreboard {
    public static HashMap<Player, FastBoard> scoreboardHashMap;
    public static HashMap<Player, Integer> stageHashMap;

    public static void playerJoin(Player p) throws Exception {
        FastBoard b = new FastBoard(p);
        scoreboardHashMap.put(p, b);
        stageHashMap.put(p, 1);

        b.updateTitle(ColorUtils.format("&b&lInstinctia"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime now = LocalDateTime.now();
        b.updateLines(
                ColorUtils.format("&bLoading..."));
    }

    public static void playerLeave(Player p){
        scoreboardHashMap.remove(p);
        stageHashMap.remove(p);
    }

    public static void init(){
        scoreboardHashMap = new HashMap<>();
        stageHashMap = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VortechCore.getPlugin(VortechCore.class), new Runnable() {
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                    LocalDateTime now = LocalDateTime.now();
                    int ping = 1337;
                    try {
                        ping = p.getPing();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    String s = "&a";
                    if(ping>50){
                        s="&6";
                    }
                    if(ping>100){
                        s="&c";
                    }
                    String a = "←";

                    switch (stageHashMap.get(p)){
                        case 1:
                            stageHashMap.put(p, 2);
                            break;
                        case 2:
                            stageHashMap.put(p, 3);
                            a="←";
                            break;
                        case 3:
                            stageHashMap.put(p, 1);
                            a="→";
                    }

                    try {
                        scoreboardHashMap.get(p).updateLines(
                                ColorUtils.format(s+p.getPing()+"ms "+a+"&7 "+VortechCore.name),
                                ColorUtils.format(""),
                                ColorUtils.format("&dLobby"),
                                ColorUtils.format(""),
                                ColorUtils.format(VortechCore.displayNames.get(p.getUniqueId())),
                                ColorUtils.format(""));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 40L, 15L);
    }
}
