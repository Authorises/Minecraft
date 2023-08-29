package dev.authorises.vortechfactions.rng;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RNGManager {

    static Boolean percentChance(Float chance) {
        return Math.random() <= chance;
    }

    public static void rngCheck(Player player, RNGChanceType type) throws InstantiationException, IllegalAccessException {
        List<RNGChanceItem> c = new ArrayList<>();
        for(RNGChanceItem cl : VortechFactions.rngChances){
            if(cl.getType().equals(type)){
                c.add(cl);
            }
        }
        Collections.shuffle(c);
        boolean r = false;
        for(RNGChanceItem lc : c){
            if(!(r)){
                r=percentChance(lc.getChance());
                if(r){
                    lc.execute(player);
                    player.playSound(player.getLocation(), lc.getRarity().sound, 1.0F, 1.0F);
                    if(lc.getChatAnnouncement()){
                        for(Player p : Bukkit.getOnlinePlayers()){
                            p.sendMessage(ColorUtils.format("&b&l============================="));
                            p.sendMessage(ColorUtils.format(player.getDisplayName()+" has received a "+lc.getRarity().getName()));
                            p.sendMessage(ColorUtils.format("&b&l============================="));
                        }
                    }
                }
            }
        }
    }

}
