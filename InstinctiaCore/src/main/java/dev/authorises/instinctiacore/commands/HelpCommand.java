package dev.authorises.instinctiacore.commands;

import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class HelpCommand extends Command {

    public HelpCommand(){
        super("help", "", "assistance", "helpop");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(ColorUtils.format("" +
                "&f-------------&b[&dHelp&b]&f-------------\n" +
                "&fYou can find some help on our &bDiscord\n" +
                "&fOur discord is &bhttps://discord.gg/QBGTN5PDf5&f"));
    }
}
