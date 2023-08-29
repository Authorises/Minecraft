package dev.authorises.instinctiacore.events;

import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.objects.PlayerDataManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLogin implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event){
        ProxiedPlayer p = event.getPlayer();
        if(p.hasPermission("instinctia.staff")){
            if(!(InstinctiaCore.staffMode.containsKey(p.getUniqueId()))){
                InstinctiaCore.staffMode.put(p.getUniqueId(), false);
                p.sendMessage(ColorUtils.format("&7Staff mode is disabled. &7Enable it with &b/staff&7."));
            }
        }
        for(ProxiedPlayer pr : InstinctiaCore.proxy.getPlayers()){
            if(pr.hasPermission("instinctia.staff")){
                if(InstinctiaCore.staffMode.get(pr.getUniqueId())){
                    pr.sendMessage(ColorUtils.format("&bPlayer joined: &f"+p.getName()));
                }
            }
        }


    }
}