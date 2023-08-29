package dev.authorises.instinctiacore.commands;

import com.mongodb.client.FindIterable;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.objects.PlayerData;
import dev.authorises.instinctiacore.objects.PlayerDataManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.bson.Document;

import java.util.UUID;

public class PlayerCommand extends Command {

    public PlayerCommand(){
        super("player", "instinctia.staff", "p");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(args.length>=1){
                try {
                    PlayerData pl = PlayerDataManager.getPlayerById(UUID.fromString(args[0]));
                    if (pl != null) {
                        p.sendMessage(PlayerDataManager.playerDataInfo(pl));
                    } else {
                        p.sendMessage(ColorUtils.format("&cError! A player with that UUID was not found."));
                    }
                }catch (Exception e){
                    PlayerData pl = PlayerDataManager.fromUsername(args[0]);
                    if (pl != null) {
                        p.sendMessage(PlayerDataManager.playerDataInfo(pl));
                    } else {
                        p.sendMessage(ColorUtils.format("&cError! A player with that username was not found."));
                    }
                }
            } else{
                p.sendMessage(ColorUtils.format("&cError! Format: &b/player [Player UUID/Username]"
                +       "\n&7You can only use a player's username if they are online." +
                        "\n&7You can use an offline player's uuid if they have joined before."));
            }

        }else{
            sender.sendMessage(ColorUtils.format("&cOnly players can use this command."));
        }
    }
}
