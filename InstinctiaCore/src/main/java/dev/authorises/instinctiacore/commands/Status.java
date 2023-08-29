package dev.authorises.instinctiacore.commands;

import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.sockets.SocketManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Command;

public class Status extends Command {

    public Status(){
        super("status", "instinctia.status", "netstat");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            //SocketManager.socket.emit("netstat", p.getUniqueId().toString());
        }else{
            InstinctiaCore.instance.getLogger().info("/status is for players only!");
        }

    }
}
