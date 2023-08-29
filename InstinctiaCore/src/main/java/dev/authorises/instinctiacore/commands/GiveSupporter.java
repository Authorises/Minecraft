package dev.authorises.instinctiacore.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class GiveSupporter extends Command {

    public GiveSupporter(){
        super("givesupporter", "instinctia.staff");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender.hasPermission("instinctia.staff")) {
            if (args.length >= 2) {

            }
        }
    }
}
