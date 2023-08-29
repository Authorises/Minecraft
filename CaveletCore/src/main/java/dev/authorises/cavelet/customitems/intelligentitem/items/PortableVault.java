package dev.authorises.cavelet.customitems.intelligentitem.items;

import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PortableVault extends IntelligentItem {

    public PortableVault(){
        setRarity(Rarity.DIVINE);
        setId("PORTABLE_VAULT");
        setDisplayName("Portable Vault");
        setMaterial(Material.ENDER_CHEST);
        setDescription("Right click to open your ender chest anywhere");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(false);
        setEnchantable(true);
        setFlags();
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void shiftLeftClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftRightClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void leftClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void shiftLeftClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void placeItem(BlockPlaceEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void breakWith(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void shiftBreakWith(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
        player.openInventory(player.getEnderChest());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
    }

    @Override
    public void consume(PlayerItemConsumeEvent event, Player player) {

    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {

    }
}
