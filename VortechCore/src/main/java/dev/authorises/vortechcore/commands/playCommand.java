package dev.authorises.vortechcore.commands;

import dev.authorises.vortechcore.gui.PlayGui;
import dev.authorises.vortechcore.utils.TunnelUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class playCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            Bukkit.getLogger().warning("Command: play, can only be executed by players");
            return true;
        }
        Player p = (Player) sender;

        if(args.length!=1){
            new PlayGui(p);
        }else{
            TunnelUtils.transfer(p, args[0]);
        }
        return true;
    }
}
