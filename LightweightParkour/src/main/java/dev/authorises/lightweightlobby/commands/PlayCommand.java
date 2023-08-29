package dev.authorises.lightweightlobby.commands;

import dev.authorises.lightweightlobby.gui.EditorGui;
import dev.authorises.lightweightlobby.gui.PlayGui;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class PlayCommand extends Command {
    PlayCommand() {
        super("play");
        setCondition(((sender, commandString) -> !(sender instanceof ConsoleSender) ));

        addSyntax(((sender, context) -> {
            if(sender instanceof Player){
                new PlayGui((Player) sender).open();
            }
        }));
    }
}
