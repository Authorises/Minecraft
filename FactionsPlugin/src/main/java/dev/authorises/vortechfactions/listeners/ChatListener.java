package dev.authorises.vortechfactions.listeners;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.settings.BooleanSetting;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.getFactionDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.BatchUpdateException;

public class ChatListener implements Listener {

    @EventHandler
    private void c(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(p);
        Bukkit.getLogger().info(p.getName() + " ( " + p.getUniqueId() + " ), chat: '" + e.getMessage() + "'");
        if(fPlayer.getChatMode().equals(ChatMode.PUBLIC)){
            e.setCancelled(true);
            for(Player lp : Bukkit.getOnlinePlayers()){
                if(((BooleanSetting)VortechFactions.settingsManager.getSetting("public-chat-visible")).getState(lp.getUniqueId())) {
                    String f = getFactionDisplay.get(lp, VortechFactions.api.getFaction(p));
                    lp.sendMessage(ColorUtils.format("&f(" + f + "&f)" + " " + p.getDisplayName() + "&f: " + e.getMessage()));
                }
            }
        }else if(fPlayer.getChatMode().equals(ChatMode.FACTION)){
            e.setCancelled(true);
            if(fPlayer.hasFaction()){
                for(Player lp : fPlayer.getFaction().getOnlinePlayers()){
                    FPlayer lfp = FPlayers.getInstance().getByPlayer(lp);
                    if(((BooleanSetting)VortechFactions.settingsManager.getSetting("faction-chat-visible")).getState(lp.getUniqueId())) {
                        String f = getFactionDisplay.get(lp, VortechFactions.api.getFaction(p));
                        lp.sendMessage(ColorUtils.format("&a[FACTION] &r"+fPlayer.getNameAndTitle(lfp)+"&f: "+e.getMessage()));
                    }
                }
            }

        }else if(fPlayer.getChatMode().equals(ChatMode.TRUCE)){
            e.setCancelled(true);
            if(fPlayer.hasFaction()){
                for(FPlayer fp : FPlayers.getInstance().getOnlinePlayers()){
                    if(fp.getRelationTo(fPlayer.getFaction()).equals(Relation.TRUCE) || fp.getFaction().equals(fPlayer.getFaction())){
                        Player lp = fp.getPlayer();
                        if(((BooleanSetting)VortechFactions.settingsManager.getSetting("truce-chat-visible")).getState(lp.getUniqueId())) {
                            String f = getFactionDisplay.get(lp, VortechFactions.api.getFaction(p));
                            lp.sendMessage(ColorUtils.format("&d[TRUCE] &r"+fPlayer.getNameAndTitle(fp)+"&f: "+e.getMessage()));
                        }
                    }
                }
            }

        }else if(fPlayer.getChatMode().equals(ChatMode.ALLIANCE)){
            e.setCancelled(true);
            if(fPlayer.hasFaction()){
                for(FPlayer fp : FPlayers.getInstance().getOnlinePlayers()){
                    if(fp.getRelationTo(fPlayer.getFaction()).equals(Relation.ALLY) || fp.getFaction().equals(fPlayer.getFaction())){
                        Player lp = fp.getPlayer();
                        if(((BooleanSetting)VortechFactions.settingsManager.getSetting("ally-chat-visible")).getState(lp.getUniqueId())) {
                            String f = getFactionDisplay.get(lp, VortechFactions.api.getFaction(p));
                            lp.sendMessage(ColorUtils.format("&5[ALLY] &r"+fPlayer.getNameAndTitle(fp)+"&f: "+e.getMessage()));
                        }
                    }
                }
            }

        }else if(fPlayer.getChatMode().equals(ChatMode.MOD)){
            e.setCancelled(true);
            if (fPlayer.getRole().isAtLeast(Role.MODERATOR)) {
                // Iterates only through the factions' members so we enhance performance.
                for (FPlayer lfp : fPlayer.getFaction().getFPlayersWhereOnline(true)) {
                    if (lfp.getRole().isAtLeast(Role.MODERATOR)) {
                        Player lp = lfp.getPlayer();
                        if(((BooleanSetting)VortechFactions.settingsManager.getSetting("mod-chat-visible")).getState(lp.getUniqueId())) {
                            String f = getFactionDisplay.get(lp, VortechFactions.api.getFaction(p));
                            lp.sendMessage(ColorUtils.format("&e[MOD] &r"+fPlayer.getNameAndTitle(lfp)+"&f: "+e.getMessage()));
                        }
                    }
                }
            } else {
                fPlayer.msg(TL.COMMAND_CHAT_MOD_ONLY);
                fPlayer.setChatMode(ChatMode.FACTION);
            }
        }
    }


}
