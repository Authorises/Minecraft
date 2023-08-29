package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length>=1){
                try {
                    CItem cItem = new CItem(Cavelet.customItemsManager.getItemById(args[0].toUpperCase()));
                    p.getInventory().addItem(cItem.getItemStack());
                }catch (Exception e){
                    p.sendMessage(ColorUtils.format("&cItem not found with id: &7"+args[0]));
                    e.printStackTrace();
                }
            }else{
                p.sendMessage(ColorUtils.format("&7Usage: &b/item &f<item id>"));
            }
        }
        return true;
    }
}
