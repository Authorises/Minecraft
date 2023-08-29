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

public class FactionGateway extends IntelligentItem {

    public FactionGateway(){
        setRarity(Rarity.DIVINE);
        setId("FACTION_GATEWAY");
        setDisplayName("Faction Gateway");
        setMaterial(Material.BEACON);
        setDescription("One can be placed per faction. Will count containers in the same chunk and tally the value of items inside them to determine your place on faction top. Does not drop when broken.");
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
            Cavelet.intelligentBlockManager.placeBlock("FACTION_GATEWAY", event.getBlockPlaced().getLocation(), event);
        }catch (Exception e){
            event.setCancelled(true);
        }
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
        try {
            Cavelet.intelligentBlockManager.placeBlock("FACTION_GATEWAY", event.getBlockPlaced().getLocation(), event);
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
