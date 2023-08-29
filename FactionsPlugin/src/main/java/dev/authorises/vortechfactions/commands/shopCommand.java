package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.shop.ShopCategory;
import dev.authorises.vortechfactions.shop.ShopUtils;
import dev.authorises.vortechfactions.shop.shopManager;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class shopCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(VortechFactions.combatLogger.attemptRestricted(p)) {
                if (args.length >= 1) {
                    try {
                        new shopManager(p, Integer.parseInt(args[0]), ShopCategory.ALL).open();
                    } catch (NumberFormatException e) {
                        ShopCategory cat = ShopUtils.categoryFromString(args[0]);
                        new shopManager(p, 1, cat).open();
                    }
                } else if (args.length >= 2) {
                    if (args[0].equalsIgnoreCase("search")) {

                    }
                } else {
                    new shopManager(p, 1, ShopCategory.ALL).open();
                }
            }
        }
        return true;
    }
}
