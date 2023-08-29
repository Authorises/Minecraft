package dev.authorises.vortechfactions.commands;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.FPlayer;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class fAlertCommand implements Listener {

    @EventHandler
    public void h(PlayerCommandPreprocessEvent e){
        String x = e.getMessage().toLowerCase(Locale.ROOT);
        if(x.toUpperCase().startsWith("/F ALERT ")){
            e.setCancelled(true);
            String xx = e.getMessage().substring(9, e.getMessage().length());
            Faction fac = VortechFactions.api.getFaction(e.getPlayer());
            for(FPlayer fp : fac.getOnlineMembers()){
                Player p = fp.getPlayer();
                p.sendMessage(ColorUtils.format("&f(&bFaction Alert&f) &b"+e.getPlayer().getDisplayName()+"&f: "+xx));
                p.sendTitle(ColorUtils.format("&b&lFaction Alert"), ColorUtils.format("&7"+xx));
                //sendTitle.sendHeaderAndFooter(p, ColorUtils.format("&b&lFaction Alert"), ColorUtils.format("&7"+xx));
                p.playSound(p.getLocation(), Sound.BAT_DEATH, 1.0F, 1.0F);
            }
        }else if(x.toUpperCase().equals("/F ALERT")){
            e.getPlayer().sendMessage(ColorUtils.format("&f(&b!&f) Usage: &b/f alert <message>"));
            e.setCancelled(true);
        }
    }

}
