package dev.authorises.cavelet.customitems.intelligentitem.items;

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

public class CompactedCrops extends IntelligentItem {

    public CompactedCrops(){
        setRarity(Rarity.UNCOMMON);
        setId("COMPACTED_CROPS");
        setDisplayName("Compacted Crops");
        setMaterial(Material.HAY_BLOCK);
        setDescription("");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(true);
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
        event.setCancelled(true);
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
        event.setCancelled(true);
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
