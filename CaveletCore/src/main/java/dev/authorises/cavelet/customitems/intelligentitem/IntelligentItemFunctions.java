package dev.authorises.cavelet.customitems.intelligentitem;

import dev.authorises.cavelet.exceptions.InvalidBlockNameException;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.lang.reflect.InvocationTargetException;

public interface IntelligentItemFunctions {

    // Events

    void leftClickBlock(PlayerInteractEvent event, Player player);
    void rightClickBlock(PlayerInteractEvent event, Player player);
    void shiftLeftClickBlock(PlayerInteractEvent event, Player player);
    void shiftRightClickBlock(PlayerInteractEvent event, Player player);
    void leftClickAir(PlayerInteractEvent event, Player player);
    void rightClickAir(PlayerInteractEvent event, Player player);
    void shiftLeftClickAir(PlayerInteractEvent event, Player player);
    void shiftRightClickAir(PlayerInteractEvent event, Player player);
    void placeItem(BlockPlaceEvent event, Player player) throws InvalidBlockNameException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException;
    void shiftPlaceItem(BlockPlaceEvent event, Player player);
    void breakWith(BlockBreakEvent event, Player player);
    void shiftBreakWith(BlockBreakEvent event, Player player);
    void consume(PlayerItemConsumeEvent event, Player player);
    void shiftConsume(PlayerItemConsumeEvent event, Player player);

}
