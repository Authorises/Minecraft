package dev.authorises.cavelet.scoreboard;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.FactionRank;
import dev.authorises.cavelet.factions.FactionTop;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.objectives.Objective;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.NumberFormat;
import fr.mrmicky.fastboard.FastBoard;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainScoreboard {
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

        b.updateTitle(ColorUtils.format("&bCavelet"));
        b.updateLines(ColorUtils.format("&cLoading scoreboard..."));
    }

    public static void playerLeave(Player p){
        scoreboardHashMap.remove(p);
    }

    public static void init(){
        scoreboardHashMap = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), new Runnable() {
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(!scoreboardHashMap.containsKey(p)){
                        try {
                            playerJoin(p);
                            return;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    try {
                        Integer i = p.getPing();
                        String pingColour = "&b";
                        if(i>10){
                            pingColour="&a";
                        }
                        if(i>60){
                            pingColour="&6";
                        }
                        else if(i>100){
                            pingColour="&c";
                        }

                        if(!Cavelet.cachedMPlayers.containsKey(p.getUniqueId())){
                            return;
                        }
                        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                        ArrayList<Objective> objectivesLeft = new ArrayList<>(Cavelet.objectiveManager.getObjectives());
                        objectivesLeft.removeAll(mp.getCompletedObjectives());
                        List<String> slines = new ArrayList<>();
                        slines.add(ColorUtils.format("&7" + dtf.format(now) + " "+pingColour + p.getPing() + "ms"));
                        slines.add("");
                        /**
                        if(objectivesLeft.size()>0) {
                            Objective o = objectivesLeft.get(0);
                            slines.add(ColorUtils.format("&7Current Objective"));
                            slines.addAll(chunk(objectivesLeft.get(0).getMessage(), 30));
                            slines.add("");
                        }
                         */
                        slines.add(ColorUtils.format("&7Balance: &b$"+ NumberFormat.coolNumberFormat(mp.getBalance().longValue())));
                        slines.add(ColorUtils.format("&7Dark Souls: &d"+ NumberFormat.coolNumberFormat(mp.getDarkSouls().longValue())));
                        slines.add(ColorUtils.format(""));
                        if(Cavelet.factionManager.playersFactions.containsKey(p.getUniqueId())){
                            MFaction faction = Cavelet.factionManager.playersFactions.get(p.getUniqueId());
                            FactionRank rank = faction.getMembers().get(p.getUniqueId());
                            slines.add(ColorUtils.format("&7Faction: &a"+faction.getName()));
                            slines.add(ColorUtils.format("&7 - Rank: "+Cavelet.legacyComponentSerializer.serialize(Cavelet.miniMessage.deserialize("<"+rank.colour+">"+rank.display))));
                            slines.add(ColorUtils.format("&7 - Members: &a"+faction.getOnlinePlayers().size()+"/"+faction.getMembers().size()));
                            Integer ftoprank = Cavelet.factionManager.factionTop.leaderBoard.contains(faction)?Cavelet.factionManager.factionTop.leaderBoard.indexOf(faction)+1:-1;
                            if(ftoprank!=-1){
                                slines.add(ColorUtils.format("&7 - F TOP: "+Cavelet.legacyComponentSerializer.serialize(Cavelet.miniMessage.deserialize(FactionTop.getColorFor(ftoprank)+"#"+ftoprank +" ("+String.format("%,.0f", faction.lastUpdatedPoints.doubleValue())+")"))));
                            }
                        }else{
                            slines.add(ColorUtils.format("&7Faction: &cN/A"));
                            slines.add(ColorUtils.format("&7 - &7You can create a"));
                            slines.add(ColorUtils.format("&7 - &afaction&7 by using"));
                            slines.add(ColorUtils.format("&7 - &7/f create"));
                        }

                        if(mp.combatLogLeft!=null && mp.combatLogLeft!=-1){
                            slines.add(ColorUtils.format(""));
                            slines.add(ColorUtils.format("&7Combat: &cEngaged"));
                            slines.add(ColorUtils.format("&7Left: &b"+mp.combatLogLeft+"s"));
                            slines.add(ColorUtils.format(""));
                            slines.add(ColorUtils.format("&7You will &cdie&7 if"));
                            slines.add(ColorUtils.format("&7you log out."));
                        }

                        scoreboardHashMap.get(p).updateLines(slines);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0L, 2L);
    }
}
