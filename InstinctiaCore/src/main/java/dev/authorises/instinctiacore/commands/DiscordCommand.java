package dev.authorises.instinctiacore.commands;

import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DiscordCommand extends Command {

    public DiscordCommand(){
        super("discord", "", "disc", "community");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(ColorUtils.format("&fDiscord: &Bhttps://discord.gg/QBGTN5PDf5"));
    }
}
