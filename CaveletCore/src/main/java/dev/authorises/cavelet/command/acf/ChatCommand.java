package dev.authorises.cavelet.command.acf;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.chat.ChatChannel;
import dev.authorises.cavelet.gui.ChatGUI;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.entity.Player;


@CommandAlias("chat")
public class ChatCommand extends BaseCommand {

    @Default
    @CommandCompletion("@chatchannels")
    public void chat(Player player, @Optional String channel){
        if(channel==null){
            new ChatGUI(player);
        }else{
            try{
                ChatChannel targetChannel = ChatChannel.valueOf(channel.toUpperCase());
                if(!targetChannel.hasAccess.test(player)){
                    player.sendMessage(Cavelet.miniMessage.deserialize("<red>You do not have access to "+targetChannel.colour+targetChannel.name+" Chat"));
                    return;
                }

                Cavelet.chatManager.playerSettings.get(player.getUniqueId()).setCurrentChannel(targetChannel);
                player.sendMessage(Cavelet.miniMessage.deserialize("<gray>You are now talking in "+targetChannel.colour+targetChannel.name+" Chat"));

            }catch (IllegalArgumentException e){
                player.sendMessage(ColorUtils.format("&cA chat channel of that name could not be found."));
            }

        }
    }


}
