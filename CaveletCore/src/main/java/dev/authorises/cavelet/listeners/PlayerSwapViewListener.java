package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.gui.ViewGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapViewListener implements Listener {

    @EventHandler
    public void swapOffhand(PlayerSwapHandItemsEvent e){
        if(e.getPlayer().isSneaking()){
            e.setCancelled(true);
            new ViewGUI(e.getPlayer(), Cavelet.cachedMPlayers.get(e.getPlayer().getUniqueId()));
        }
    }
}
