package dev.authorises.vortechfactions.masks;

import dev.authorises.vortechfactions.rng.Rarity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public interface Mask {
    String getDisplayName();
    String getName();
    String getValue();
    Rarity getRarity();
    ArrayList<String> getLore();
    void loopEffects(Player p);
    void putOn(Player p);
    void takeOff(Player p);
    void killWith(Player p);
    void dieWith(Player p);
    void placeBlock(BlockPlaceEvent e);
    void breakBlock(BlockBreakEvent e);
    void leftClickBlock(PlayerInteractEvent e);
    void rightClickBlock(PlayerInteractEvent e);
    void leftClickAir(PlayerInteractEvent e);
    void rightClickAir(PlayerInteractEvent e);
}
