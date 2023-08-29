package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.TimeFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LeaderboardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length>=1){
            switch (args[0].toLowerCase()) {
                case "kills" -> {
                    sender.sendMessage(Cavelet.killsLeaderboard.append(Component.text(ColorUtils.format("\n\n&7Last Updated &b"+ TimeFormat.format(System.currentTimeMillis()-Cavelet.lastLeaderboardUpdate)+"&7 ago."))));
                    break;
                }
                case "balance" -> {
                    sender.sendMessage(Cavelet.balanceLeaderboard.append(Component.text(ColorUtils.format("\n\n&7Last Updated &b"+ TimeFormat.format(System.currentTimeMillis()-Cavelet.lastLeaderboardUpdate)+"&7 ago."))));
                    break;
                }
            }
        }
        return true;
    }
}
