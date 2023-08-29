package dev.authorises.cavelet.chat;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.FactionRank;
import dev.authorises.cavelet.playerdata.MProfile;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ChatSettings {

    private HashMap<ChatChannel, Boolean> visibility;
    private ChatChannel currentChannel;

    public void updateAllowedChannels(Player player){
        HashMap<ChatChannel, Boolean> oldVisibility = (HashMap<ChatChannel, Boolean>) visibility.clone();
        visibility.clear();

        // check channels

        for(ChatChannel chatChannel : ChatChannel.values()){
            if(chatChannel.hasAccess.test(player)){
                visibility.put(chatChannel, oldVisibility.getOrDefault(chatChannel, true));
            }
        }

        // if they don't have access to the channel, stop them from typing in it;
        if(!this.currentChannel.hasAccess.test(player)){
            this.currentChannel = ChatChannel.PUBLIC;
        }

    }

    public ChatSettings(Player player){
        this.visibility = new HashMap<>();
        this.currentChannel = ChatChannel.PUBLIC;
        this.updateAllowedChannels(player);
    }

    public HashMap<ChatChannel, Boolean> getVisibility(){
        return this.visibility;
    }

    public void setVisibility(ChatChannel channel, Boolean visible){
        this.visibility.put(channel, visible);
    }

    public void setCurrentChannel(ChatChannel channel){
        this.currentChannel = channel;
    }

    public ChatChannel getCurrentChannel(){
        return this.currentChannel;
    }




}
