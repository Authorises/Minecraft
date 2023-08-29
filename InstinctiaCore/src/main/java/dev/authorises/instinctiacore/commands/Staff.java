package dev.authorises.instinctiacore.commands;

import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Staff extends Command {

    public Staff(){
        super("staff", "instinctia.staff", "s");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) sender;
            Boolean b = !(InstinctiaCore.staffMode.get(p.getUniqueId()));
            InstinctiaCore.staffMode.put(p.getUniqueId(), b);
            p.sendMessage(ColorUtils.format("&f(&b!&F) Staff mode set to "+(b?"&aEnabled":"&cDisabled")));
        }
    }
}
