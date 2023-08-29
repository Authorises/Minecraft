package dev.authorises.cavelet.customitems.intelligentitem;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.exceptions.InvalidBlockNameException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class IntelligentItemListeners implements Listener {

    private HashMap<String, IntelligentItem> items;

    public IntelligentItemListeners(HashMap<String, IntelligentItem> items){
        this.items = items;
    }

    @EventHandler
    public void blockInteract(PlayerInteractEvent event){
        if(event.getItem()!=null){
            ItemStack i = event.getItem();
            NBTItem ni = new NBTItem(i);
            if(ni.hasKey("item_id")){
                String id = ni.getString("item_id");
                if(items.containsKey(id)){
                    if(event.getClickedBlock() != null) {
                        if (event.getAction().isLeftClick()) {
                            if (event.getPlayer().isSneaking()) {
                                items.get(id).shiftLeftClickBlock(event, event.getPlayer());
                            } else {
                                items.get(id).leftClickBlock(event, event.getPlayer());
                            }
                        } else if (event.getAction().isRightClick()) {
                            if (event.getPlayer().isSneaking()) {
                                items.get(id).shiftRightClickBlock(event, event.getPlayer());
                            } else {
                                items.get(id).rightClickBlock(event, event.getPlayer());
                            }
                        }
                    }else{
                        if (event.getAction().isLeftClick()) {
                            if (event.getPlayer().isSneaking()) {
                                items.get(id).shiftLeftClickAir(event, event.getPlayer());
                            } else {
                                items.get(id).leftClickAir(event, event.getPlayer());
                            }
                        } else if (event.getAction().isRightClick()) {
                            if (event.getPlayer().isSneaking()) {
                                items.get(id).shiftRightClickAir(event, event.getPlayer());
                            } else {
                                items.get(id).rightClickAir(event, event.getPlayer());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) throws InvalidBlockNameException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        ItemStack i = event.getItemInHand();
        NBTItem ni = new NBTItem(i);
        if(ni.hasKey("item_id")){
            String id = ni.getString("item_id");
            if(items.containsKey(id)) {
                if(event.getPlayer().isSneaking()){
                    items.get(id).shiftPlaceItem(event, event.getPlayer());
                }else{
                    items.get(id).placeItem(event, event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event){
        try {
            ItemStack i = event.getPlayer().getInventory().getItemInMainHand();
            NBTItem ni = new NBTItem(i);
            if (ni.hasKey("item_id")) {
                String id = ni.getString("item_id");
                if (items.containsKey(id)) {
                    if (event.getPlayer().isSneaking()) {
                        items.get(id).shiftBreakWith(event, event.getPlayer());
                    } else {
                        items.get(id).breakWith(event, event.getPlayer());
                    }
                }
            }
        }catch (NullPointerException ignored){

        }
    }

    @EventHandler
    public void consume(PlayerItemConsumeEvent event){
        ItemStack i = event.getPlayer().getActiveItem();
        NBTItem ni = new NBTItem(i);
        if (ni.hasKey("item_id")) {
            String id = ni.getString("item_id");
            if (items.containsKey(id)) {
                if (event.getPlayer().isSneaking()) {
                    items.get(id).shiftConsume(event, event.getPlayer());
                } else {
                    items.get(id).consume(event, event.getPlayer());
                }
            }
        }
    }



}
