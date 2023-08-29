package dev.authorises.cavelet.command;

import com.mitchtalmadge.asciidata.graph.ASCIIGraph;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.deepmarket.DeepMarketItem;
import dev.authorises.cavelet.deepmarket.DeepMarketUtils;
import dev.authorises.cavelet.gui.DeepMarketGUI;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeepMarketCommand implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(Cavelet.deepMarketManager.getOpen()) {
                new DeepMarketGUI(p);
            }else {
                p.sendMessage(ColorUtils.format("&cDeep Market is currently closed"));
            }
        }
        return true;
    }
}
