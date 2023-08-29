package dev.authorises.vortechfactions.commands;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.getFactionDisplay;
import dev.authorises.vortechfactions.utilities.homeUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class sethomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            Location home = p.getLocation();
            Faction factionat = VortechFactions.api.getFactionAt(home.getChunk());
            if(factionat.isWilderness() || factionat==VortechFactions.api.getFaction(p)){
                try {
                    homeUtils.setHome(p, home);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                p.sendMessage(ColorUtils.format("&cError!&f You cannot set your home in "+ getFactionDisplay.get(p, VortechFactions.api.getFactionAt(home.getChunk()))));
            }
        }
        return true;
    }
}
