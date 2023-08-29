package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.settings.SettingsCategory;
import dev.authorises.vortechfactions.settings.SettingsUtils;
import dev.authorises.vortechfactions.settings.gui.SettingsGUI;
import dev.authorises.vortechfactions.shop.ShopCategory;
import dev.authorises.vortechfactions.shop.ShopUtils;
import dev.authorises.vortechfactions.shop.shopManager;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class factionSettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length>=1){
                try {
                    new SettingsGUI(p, Integer.parseInt(args[0]), SettingsCategory.ALL).open();
                }catch (NumberFormatException e){
                    SettingsCategory cat = SettingsUtils.categoryFromString(args[0]);
                    new SettingsGUI(p, 1, cat).open();
                }
            }else if (args.length>=2){
                if(args[0].equalsIgnoreCase("search")){

                }
            }
            else {
                new SettingsGUI(p, 1, SettingsCategory.ALL).open();
            }
        }
        return true;
    }
}
