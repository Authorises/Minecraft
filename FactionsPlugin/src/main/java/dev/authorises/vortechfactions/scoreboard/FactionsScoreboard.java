package dev.authorises.vortechfactions.scoreboard;

import cc.javajobs.factionsbridge.bridge.impl.saberfactions.SaberFactionsAPI;
import com.massivecraft.factions.*;
import com.massivecraft.factions.integration.Econ;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.PingUtil;
import dev.authorises.vortechfactions.utilities.chunkUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FactionsScoreboard {
    public static HashMap<Player, FastBoard> scoreboardHashMap;

    public static List<String> chunk(String s, int limit) {
        List<String> parts = new ArrayList<String>();
        while(s.length() > limit) {
            int splitAt = limit-1;
            for(;splitAt>0 && !Character.isWhitespace(s.charAt(splitAt)); splitAt--);
            if(splitAt == 0)
                return parts; // can't be split
            parts.add(ColorUtils.format("&f"+s.substring(0, splitAt)));
            s = s.substring(splitAt+1);
        }
        parts.add(ColorUtils.format("&f"+s));
        return parts;
    }

    public static String coolNumberFormat(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.#");
        String value = format.format(count / Math.pow(1000, exp));
        return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
    }

    public static void playerJoin(Player p) throws Exception {
        FastBoard b = new FastBoard(p);
        scoreboardHashMap.put(p, b);

        b.updateTitle(ColorUtils.format("&b&lInstinctia"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime now = LocalDateTime.now();
        FPlayer fp = FPlayers.getInstance().getByPlayer(p);
        Faction f = fp.getFaction();
        FLocation loc = new FLocation(fp);
        Faction factionAt = Board.getInstance().getFactionAt(loc);
        if(f.isWilderness()) {
            List<String> s =
                    chunk(
                            "You are currently &cnot&f in a &bFaction&f, you can create a &bFaction&f with &a/f create&f.",
                            26
                    );
            List<String> slines = new ArrayList<>();
            slines.add(ColorUtils.format("&7" + dtf.format(now) + " &a" + PingUtil.getPlayerPing(p) + "ms"));
            slines.add("");
            slines.addAll(s);
            b.updateLines(slines);
        }else{

            List<String> slines = new ArrayList<>();
            slines.add(ColorUtils.format("&7" + dtf.format(now) + " &a" + PingUtil.getPlayerPing(p) + "ms"));
            slines.add("");
            slines.add(ColorUtils.format("&b"+p.getDisplayName()));
            slines.add(ColorUtils.format("  &fBal: &b$"+coolNumberFormat(((Double)VortechFactions.econ.getBalance(p)).longValue())));
            slines.add(ColorUtils.format(""));
            slines.add(ColorUtils.format("&bChunk &7("+loc.getChunk().getX()+", "+loc.getChunk().getZ()+")"));
            slines.add(ColorUtils.format("  &fOwner: "+factionAt.getTag(fp)));
            slines.add(ColorUtils.format("  &fType: "+ chunkUtils.getType(loc.getChunk()).getColor()+chunkUtils.getType(loc.getChunk()).getName()));
            slines.add(ColorUtils.format(""));
            slines.add(ColorUtils.format("&b"+f.getTag()+" &7&o("+f.getOnlinePlayers().size()+")"));
            slines.add(ColorUtils.format("  &fBal: &b$Test"));
            b.updateLines(slines);
        }
    }

    public static void playerLeave(Player p){
        scoreboardHashMap.remove(p);
    }

    public static void init(){
        scoreboardHashMap = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VortechFactions.getPlugin(VortechFactions.class), new BukkitRunnable() {
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                    LocalDateTime now = LocalDateTime.now();
                    try {
                        FPlayer fp = FPlayers.getInstance().getByPlayer(p);
                        Faction f = fp.getFaction();
                        FLocation loc = new FLocation(fp);
                        Faction factionAt = Board.getInstance().getFactionAt(loc);
                        if(f.isWilderness()) {
                            List<String> s =
                                    chunk(
                                            "You are currently &cnot&f in a &bFaction&f you can create a &bFaction&f with &a/f create&f.",
                                            26
                                    );
                            List<String> slines = new ArrayList<>();
                            slines.add(ColorUtils.format("&7" + dtf.format(now) + " &a" + PingUtil.getPlayerPing(p) + "ms"));
                            slines.add("");
                            slines.addAll(s);
                            scoreboardHashMap.get(p).updateLines(slines);
                        }else{
                            List<String> slines = new ArrayList<>();
                            slines.add(ColorUtils.format("&7" + dtf.format(now) + " &a" + PingUtil.getPlayerPing(p) + "ms"));
                            slines.add("");
                            slines.add(ColorUtils.format("&b"+p.getDisplayName()));
                            slines.add(ColorUtils.format("  &fBal: &b$"+coolNumberFormat(((Double)VortechFactions.econ.getBalance(p)).longValue())));
                            slines.add(ColorUtils.format(""));
                            Integer combat = VortechFactions.combatLogger.getLeft(p);
                            if(combat==null) {
                                if (VortechFactions.airdropManager.isActive()) {
                                    slines.add(ColorUtils.format("&b&lAirDrop &f - &eSpawn"));
                                    slines.add(ColorUtils.format("  &fLeft: &b" + VortechFactions.airdropManager.clicksLeft));
                                } else {
                                    slines.add(ColorUtils.format("&bChunk &7(" + loc.getChunk().getX() + ", " + loc.getChunk().getZ() + ")"));
                                    slines.add(ColorUtils.format("  &fOwner: " + factionAt.getTag(fp)));
                                    slines.add(ColorUtils.format("  &fType: " + chunkUtils.getType(loc.getChunk()).getColor() + chunkUtils.getType(loc.getChunk()).getName()));
                                }
                            }else{
                                slines.add(ColorUtils.format("&bCombat"));
                                slines.add(ColorUtils.format("  &fLeft: &c"+combat));
                                slines.add(ColorUtils.format("  &fHealth: &c"+Math.round(p.getHealth())+"❤"));
                            }
                            slines.add(ColorUtils.format(""));
                            slines.add(ColorUtils.format("&b"+f.getTag()+" &7&o("+f.getOnlinePlayers().size()+")"));
                            slines.add(ColorUtils.format("  &fBal: &b$"+coolNumberFormat(((Double)f.getFactionBalance()).longValue())));
                            scoreboardHashMap.get(p).updateLines(slines);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0L, 1L);
    }
}
