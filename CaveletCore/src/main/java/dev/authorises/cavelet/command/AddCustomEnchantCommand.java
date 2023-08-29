package dev.authorises.cavelet.command;

import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddCustomEnchantCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            try {
                CItem ci = new CItem(p.getInventory().getItemInMainHand());
                if(args.length>=2){
                    ci.addEnchantment(new CustomEnchant(CustomEnchantType.valueOf(args[0]), Integer.valueOf(args[1])));
                    p.getInventory().setItemInMainHand(ci.getItemStack());
                }else{
                    p.sendMessage(ColorUtils.format("&cUsage: /addcustomenchant <enchant id> <enchant level>"));
                }
            } catch (InvalidItemIdException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
