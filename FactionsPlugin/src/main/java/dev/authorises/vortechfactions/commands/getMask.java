package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.masks.Mask;
import dev.authorises.vortechfactions.masks.maskUtils;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class getMask implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("vortechfactions.getmask")){
                boolean found = false;
                for(Mask m : VortechFactions.masks){
                    if(m.getName().equalsIgnoreCase(args[0])){
                        found = true;
                        p.getInventory().addItem(maskUtils.getMaskItem(m));
                    }
                }
                if(!(found)){
                    p.sendMessage(ColorUtils.format("&f(&b!&f) Unknown mask name. &7("+args[0]+")"));
                }
            }
        }
        return true;
    }
}
