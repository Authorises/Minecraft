package dev.authorises.lightweightlobby.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.ServerSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class ShutdownCommand extends Command {
    ShutdownCommand() {
        super("shutdown", "end", "stop");
        setCondition(((sender, commandString) -> (sender instanceof ServerSender)
                || (sender instanceof ConsoleSender)
                || sender.hasPermission("admin.shutdown")
        ));

        addSyntax(((sender, context) -> {
            if(sender instanceof Player){
                Player player = (Player) sender;
                player.sendMessage(player.getUuid().toString());
            }else {
                MinecraftServer.stopCleanly();
            }
        }));
    }
}
