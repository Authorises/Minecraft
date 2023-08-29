package dev.authorises.vortechfactions.commands;

import com.comphenix.protocol.PacketType;
import com.massivecraft.factions.cmd.reserve.ReserveObject;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class echestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length>=1){
                if(p.hasPermission("instinctia.endersee")){

                }
            }else {
                if(VortechFactions.combatLogger.attemptRestricted(p)) {
                    Inventory i = p.getEnderChest();
                    p.openInventory(i);
                    p.sendMessage(ColorUtils.format("&f(&b!&f) Opening your &d&nEnder&b&nChest"));
                    p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1.0F, 1.0F);
                }
            }
        }
        return true;
    }
}
