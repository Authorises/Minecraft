package dev.authorises.cavelet.command;

import dev.authorises.cavelet.gui.skills.SkillsGUI;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.playerdata.MProfileManager;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender instanceof Player){
            Player p = (Player) commandSender;
            try {
                new SkillsGUI(p);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }


}
