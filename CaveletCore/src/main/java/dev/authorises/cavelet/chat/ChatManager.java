package dev.authorises.cavelet.chat;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.MFaction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ChatManager {
    public HashMap<UUID, ChatSettings> playerSettings;

    public ChatManager(){
        this.playerSettings = new HashMap<>();
    }

    public void playerJoins(Player player){
        this.playerSettings.put(player.getUniqueId(),new ChatSettings(player));
    }

    public void playerQuits(Player player){
        this.playerSettings.remove(player.getUniqueId());
    }

    public void dispatchMessage(Player player, String message){
        ChatChannel chatChannel = playerSettings.get(player.getUniqueId()).getCurrentChannel();

        if(!chatChannel.hasAccess.test(player)){
            playerSettings.get(player.getUniqueId()).updateAllowedChannels(player);
            dispatchMessage(player, message);
            return;
        }

        Bukkit.getLogger().info("CHAT ("+player.getUniqueId()+") ("+chatChannel+"): "+message);

        Bukkit.getOnlinePlayers().forEach((onlinePlayer) -> {
            if(playerSettings.get(onlinePlayer.getUniqueId()).getVisibility().getOrDefault(chatChannel, false)){
                if(chatChannel==ChatChannel.FACTION || chatChannel==ChatChannel.TRUSTED || chatChannel==ChatChannel.OFFICER){
                    MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                    if(faction.getMembers().containsKey(onlinePlayer.getUniqueId())){
                        onlinePlayer.sendMessage(Cavelet.miniMessage.deserialize(chatChannel.getMessagePrefix.apply(player)+player.getName()+": "+message));
                    }
                }

            }
        });
    }

}
