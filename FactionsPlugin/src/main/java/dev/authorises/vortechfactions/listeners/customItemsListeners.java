package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.items.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static dev.authorises.vortechfactions.VortechFactions.itemHandlers;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class customItemsListeners implements Listener {

    @EventHandler
    private void playerLeftP(EntityDamageByEntityEvent e){
        try{
            if(e.getDamager() instanceof Player) {
                if(e.getEntity() instanceof Player) {
                    Player p = ((Player) e.getDamager()).getPlayer();
                    for (Class<? extends ItemHandler> handlerclass : itemHandlers) {
                        final ItemHandler handler = handlerclass.newInstance();
                        //e.getPlayer().sendMessage(ColorUtils.format("&6"+handler.itemName()+"&d"+String.valueOf(e.getItem().getItemMeta().equals(CookieFacsItems.items.get(handler.itemName()).getItemMeta())))+"&e"+e.getAction().toString());
                        if (p.getItemInHand().getItemMeta().equals(Items.items.get(handler.itemName()).getItemMeta())) {
                            handler.leftClickPlayer(e);
                        }
                    }
                }
            }
        }catch (Exception ex){

        }
    }


    @EventHandler
    private void blockPlace(BlockPlaceEvent e) throws Exception {
        try{
            for (Class<? extends ItemHandler> handlerclass : itemHandlers) {
                ItemHandler handler = handlerclass.newInstance();
                //e.getPlayer().sendMessage(ColorUtils.format("&6"+handler.itemName()+"&d"+String.valueOf(e.getItem().getItemMeta().equals(CookieFacsItems.items.get(handler.itemName()).getItemMeta())))+"&e"+e.getAction().toString());
                if (e.getPlayer().getItemInHand().getItemMeta().equals(Items.items.get(handler.itemName()).getItemMeta())) {
                    handler.place(e);
                }
            }
        }catch (Exception ex){

        }
    }

    @EventHandler
    private void playerInteractP(PlayerInteractAtEntityEvent e) throws Exception {
        try{
            for (Class<? extends ItemHandler> handlerclass : itemHandlers) {
                final ItemHandler handler = handlerclass.newInstance();
                //e.getPlayer().sendMessage(ColorUtils.format("&6"+handler.itemName()+"&d"+String.valueOf(e.getItem().getItemMeta().equals(CookieFacsItems.items.get(handler.itemName()).getItemMeta())))+"&e"+e.getAction().toString());
                if (e.getPlayer().getItemInHand().getItemMeta().equals(Items.items.get(handler.itemName()).getItemMeta())) {
                    handler.rightClickPlayer(e);
                }
            }
        }catch (Exception ex){

        }
    }

    @EventHandler
    private void playerInteract(PlayerInteractEvent e) throws Exception {
        try {
            for (Class<? extends ItemHandler> handlerclass : itemHandlers) {
                final ItemHandler handler = handlerclass.newInstance();
                //e.getPlayer().sendMessage(ColorUtils.format("&6"+handler.itemName()+"&d"+String.valueOf(e.getItem().getItemMeta().equals(CookieFacsItems.items.get(handler.itemName()).getItemMeta())))+"&e"+e.getAction().toString());
                if (e.getItem().getItemMeta().equals(Items.items.get(handler.itemName()).getItemMeta())) {
                    if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
                        handler.leftClickAir(e);
                    }
                    if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        handler.leftClickBlock(e);
                    }
                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        handler.rightClickBlock(e);
                    }
                    if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        handler.rightClickAir(e);
                    }
                }
            }
        }catch (Exception ex){

        }
    }
}
