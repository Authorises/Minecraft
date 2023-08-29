package dev.authorises.cavelet.intelligentblocks.blocks;

import com.google.gson.JsonObject;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.gui.BrewingGUI;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import dev.authorises.cavelet.mining.Ore;
import dev.authorises.cavelet.utils.LootDecider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;
import java.util.UUID;

public class AlchemyNode extends IntelligentBlock {

    private String BLOCK_NAME = "ALCHEMY_NODE";

    // Empty constructor for getting name for initial gathering of intelligent blocks
    public AlchemyNode(){
        setBlockName(BLOCK_NAME);
    }

    // Creating block (i.e. placing a intelligent block)
    public AlchemyNode(Location location){
        setBlockName(BLOCK_NAME);
        setBlockUuid(UUID.randomUUID());
        setLocation(location);
        setData(new JsonObject());
    }

    // Loading block from storage
    public AlchemyNode(Location location, UUID blockUuid, JsonObject data){
        setBlockName(BLOCK_NAME);
        setLocation(location);
        setBlockUuid(blockUuid);
        setData(data);
    }

    @Override
    public void shiftRightClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        new BrewingGUI(event.getPlayer());
    }

    @Override
    public void rightClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        new BrewingGUI(event.getPlayer());
    }

    @Override
    public void broken(BlockBreakEvent event) {
        event.setDropItems(false);
        event.setExpToDrop(0);
        Cavelet.intelligentBlockManager.removeBlock(this);
        event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), new CItem(Cavelet.customItemsManager.getItemById("ALCHEMY_NODE")).getItemStack());
    }

    @Override
    public void shiftBroken(BlockBreakEvent event) {
        event.setDropItems(false);
        event.setExpToDrop(0);
        Cavelet.intelligentBlockManager.removeBlock(this);
        event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), new CItem(Cavelet.customItemsManager.getItemById("ALCHEMY_NODE")).getItemStack());
    }

    @Override
    public void placed(IntelligentBlockPlacedEvent event) throws InterruptedException {

    }

}
