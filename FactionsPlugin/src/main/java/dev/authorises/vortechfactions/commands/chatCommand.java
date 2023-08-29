package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.chat.chatGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class chatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(args.length>=1) {
                String newCommand = "f chat";
                for (String s : args) {
                    newCommand += (" " + s);
                    ((Player) sender).performCommand(newCommand);
                }
            }else {
                new chatGui((Player) sender).open();
            }
        }
        return true;
    }
}
