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

public class Vault extends IntelligentItem {

    public Vault(){
        setRarity(Rarity.DIVINE);
        setId("VAULT");
        setDisplayName("Vault");
        setMaterial(Material.BLAST_FURNACE);
        setDescription("Can be used to store four items. Any containers in the same chunk as your <#db65d7>Faction Gateway<#9eb5db>, will have the value of their items counted for their <aqua>Faction  Top<#9eb5db> ranking. Vault does not drop when broken, but items inside do drop. You can remove or add items to the vault by right clicking it.");
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
        Cavelet.intelligentBlockManager.placeBlock("VAULT", event.getBlockPlaced().getLocation(), event);
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {

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
