package dev.authorises.cavelet.customitems;

import dev.authorises.cavelet.Cavelet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BlockDropMatchManager implements Listener  {

    private HashMap<Material, ItemStack> materialsCItemsMap;

    public BlockDropMatchManager(){
        this.materialsCItemsMap = new HashMap<>();
    }

    public void addMatch(Material material, ItemStack item){
        this.materialsCItemsMap.put(material, item);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void blockBreak(BlockBreakEvent event){
        Material type = event.getBlock().getType();
        if(materialsCItemsMap.containsKey(type)){
            event.setExpToDrop(0);
            event.setDropItems(false);
            Bukkit.getWorlds().get(0).dropItemNaturally(event.getBlock().getLocation(), materialsCItemsMap.get(type));
        }else{
            if(!event.isCancelled()){
                event.setDropItems(false);
            }
        }
    }

}
