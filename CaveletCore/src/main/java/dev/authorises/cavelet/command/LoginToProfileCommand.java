package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.playerdata.MProfileManager;
import dev.authorises.cavelet.scoreboard.MainScoreboard;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LoginToProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;

            try {
                p.sendMessage(ColorUtils.format("&aLogging you into the profile with ID: "+args[0]));
                MProfile targetProfile = MProfileManager.getPlayerById(UUID.fromString(args[0]));

                // Fake the player leaving the server
                MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                Cavelet.cachedMPlayers.remove(p.getUniqueId());
                mp.save(p);
                MainScoreboard.playerLeave(p);

                // Login to the target profile for the player
                targetProfile.loadInventory(p);
                Cavelet.cachedMPlayers.put(p.getUniqueId(), targetProfile);
                MainScoreboard.playerJoin(p);
            } catch (Exception e) {
                p.sendMessage(ColorUtils.format("&cA profile with that UUID was not found."));
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
