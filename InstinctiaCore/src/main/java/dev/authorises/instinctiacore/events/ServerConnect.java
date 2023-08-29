package dev.authorises.instinctiacore.events;

import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Random;

public class ServerConnect implements Listener {

    @EventHandler
    public void e(ServerConnectEvent e){
        /**
        if(!e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)){
            return;
        }
        ArrayList<ServerInfo> options = new ArrayList<>();
        for(ServerInfo s : InstinctiaCore.servers){
            if(s.getMotd().equalsIgnoreCase("lm")){
                options.add(s);
            }
        }
        Random r = new Random();
        e.setTarget(options.get(r.nextInt(options.size())));
         */
    }
}
