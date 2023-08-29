package dev.authorises.cavelet.customitems.intelligentitem.items;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Water extends IntelligentItem {

    public Water(){
        setRarity(Rarity.COMMON);
        setId("WATER");
        setDisplayName("Water");
        setMaterial(Material.LIGHT_BLUE_STAINED_GLASS);
        setDescription("Water, stored in a solid form. Place down where you want water to liquidify");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(true);
        setEnchantable(false);
        setFlags();
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftLeftClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftRightClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void leftClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftLeftClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void placeItem(BlockPlaceEvent event, Player p) {
        event.getBlockPlaced().setType(Material.WATER, true);
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void breakWith(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftBreakWith(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void consume(PlayerItemConsumeEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {
        event.setCancelled(true);
    }
}
