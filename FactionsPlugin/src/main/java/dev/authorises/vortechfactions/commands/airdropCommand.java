package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class airdropCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 1) {
                if(args[0].equalsIgnoreCase("start")){
                    if(p.hasPermission("instinctia.airdrop")) {
                        if (VortechFactions.airdropManager.isActive()) {
                            p.sendMessage(ColorUtils.format("&f(&b!&f) There is already an &b&lAirDrop&f active!"));
                        } else {
                            VortechFactions.airdropManager.start();
                            p.sendMessage(ColorUtils.format("&f(&b!&f) You started an &b&lAirDrop&f."));
                        }
                    }
                }
                if(args[0].equalsIgnoreCase("stop")){
                    if(p.hasPermission("instinctia.airdrop")){
                        if(VortechFactions.airdropManager.isActive()){
                            VortechFactions.airdropManager.stop();
                            p.sendMessage(ColorUtils.format("&f(&b!&f) You stopped the &b&lAirDrop&f."));
                        }else{
                            p.sendMessage(ColorUtils.format("&f(&b!&f) There is currently no &b&lAirDrop&f active!"));
                        }
                    }
                }
            } else {
                if (VortechFactions.airdropManager.isActive()) {
                    VortechFactions.airdropManager.getStats(p);
                } else {
                    p.sendMessage(ColorUtils.format("&f(&b!&f) There is currently no &b&lAirDrop&f active! Try again later."));
                }
            }
        }else{
            sender.sendMessage("You aren't a player! ");
        }
        return true;
    }
}
