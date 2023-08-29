package dev.authorises.cavelet.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

public class DisableCraftingListener implements Listener {

    @EventHandler
    public void craftEvent2(CraftItemEvent e){
        if(e.getWhoClicked().getGameMode().equals(GameMode.SURVIVAL)){
            e.setCancelled(true);
        }
    }

}
