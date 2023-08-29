package dev.authorises.instinctiacore.commands;

import com.google.gson.JsonObject;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.sockets.SocketManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    public HubCommand(){
        super("hub", "", "lobby", "back", "lm");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;

            JsonObject data = new JsonObject();
            data.addProperty("type", "lm");
            data.addProperty("uuid", p.getUniqueId().toString());

            JsonObject message = new JsonObject();
            message.addProperty("message", "player-transfer-generic");
            message.add("data", data);

            InstinctiaCore.socket.socket.send(message.toString());
        }
    }
}
