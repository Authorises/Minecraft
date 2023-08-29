package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.exceptions.PlayerNotFoundException;
import dev.authorises.cavelet.gui.ViewGUI;
import dev.authorises.cavelet.playerdata.MProfileManager;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ViewCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length>=1){
                try{
                    UUID u = UUID.fromString(args[0]);
                    new ViewGUI(p, MProfileManager.getPlayerById(u));
                }catch (IllegalArgumentException e){
                    try {
                        new ViewGUI(p, MProfileManager.getPlayerByName(args[0]));
                    } catch (PlayerNotFoundException ex) {
                        p.sendMessage(ColorUtils.format("&cA player was not found &7("+args[0]+")"));
                    } catch (Exception ex2){
                        p.sendMessage(ColorUtils.format("cAn error occurred whilst processing your request."));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    p.sendMessage(ColorUtils.format("&cNo online players found with uuid: &7"+args[0]));
                }
            }else{
                new ViewGUI(p, Cavelet.cachedMPlayers.get(p.getUniqueId()));
            }
        }
        return true;
    }

}
