package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getItem implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("vortechfactions.getitem")){
                if(Items.items.containsKey(args[0])){
                    p.getInventory().addItem(Items.items.get(args[0]));
                }else{
                    p.sendMessage(ColorUtils.format("&f(&b!&f) Unknown item name. &7("+args[0]+")"));
                }
            }
        }
        return true;
    }
}
