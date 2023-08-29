package dev.authorises.vortechcore.utils;

import com.google.gson.JsonObject;
import dev.authorises.vortechcore.VortechCore;
import org.bukkit.entity.Player;

public class TunnelUtils {

    public static void transfer(Player p, String type){
        JsonObject data = new JsonObject();
        data.addProperty("type", type);
        data.addProperty("uuid", p.getUniqueId().toString());

        JsonObject message = new JsonObject();
        message.addProperty("message", "player-transfer-generic");
        message.add("data", data);

        if(VortechCore.socketManager.socket!=null){
            VortechCore.socketManager.socket.send(message.toString());
        }
    }

}
