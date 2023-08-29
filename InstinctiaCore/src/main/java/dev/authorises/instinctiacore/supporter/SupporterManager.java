package dev.authorises.instinctiacore.supporter;

import dev.authorises.instinctiacore.InstinctiaCore;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class SupporterManager {

    public static void loop(){
        InstinctiaCore.instance.getProxy().getScheduler().schedule(InstinctiaCore.proxy.getPluginManager().getPlugin("InstinctiaCore"), new Runnable() {
            @Override
            public void run() {
                for(ProxiedPlayer pl : InstinctiaCore.proxy.getPlayers()){

                }
            }
        },0, 5, TimeUnit.SECONDS);
    }
}
