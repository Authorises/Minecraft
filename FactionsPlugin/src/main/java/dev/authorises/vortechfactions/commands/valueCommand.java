package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class valueCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            try {
                ItemStack x = p.getItemInHand();
                Material m = x.getType();
                long va = Items.metaSellValues.get(x.getItemMeta());
                p.sendMessage(ColorUtils.format("&f(&b!&f) Your held item is worth &a$" + va + " ea ($" + (va * x.getAmount()) + " total)"));
            }catch (Exception e){
                p.sendMessage(ColorUtils.format("&f(&b!&f) Your held item has &c&nno&f value."));
            }
        }else{
            VortechFactions.log.info("Only players can use this command.");
        }
        return true;
    }
}
