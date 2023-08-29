package dev.authorises.cavelet.customitems.intelligentitem.items;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import dev.authorises.cavelet.exceptions.InvalidBlockNameException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.lang.reflect.InvocationTargetException;

public class AlchemyNode extends IntelligentItem {

    public AlchemyNode(){
        setRarity(Rarity.DIVINE);
        setId("ALCHEMY_NODE");
        setDisplayName("Alchemy Node");
        setMaterial(Material.BREWING_STAND);
        setDescription("Used to create different potions with all sorts of uses");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(false);
        setEnchantable(false);
        setFlags();
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftLeftClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftRightClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void leftClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftLeftClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void placeItem(BlockPlaceEvent event, Player player) throws InvalidBlockNameException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        try {
            Cavelet.intelligentBlockManager.placeBlock("ALCHEMY_NODE", event.getBlockPlaced().getLocation(), event);
        }catch (Exception e){
            event.setCancelled(true);
        }
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
        try {
            Cavelet.intelligentBlockManager.placeBlock("ALCHEMY_NODE", event.getBlockPlaced().getLocation(), event);
        }catch (Exception e){
            event.setCancelled(true);
        }
    }

    @Override
    public void breakWith(BlockBreakEvent event, Player player) {

    }

    @Override
    public void shiftBreakWith(BlockBreakEvent event, Player player) {

    }

    @Override
    public void consume(PlayerItemConsumeEvent event, Player player) {

    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {

    }
}
