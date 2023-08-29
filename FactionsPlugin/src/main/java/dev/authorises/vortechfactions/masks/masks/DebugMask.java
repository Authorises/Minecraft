package dev.authorises.vortechfactions.masks.masks;

import dev.authorises.vortechfactions.masks.Mask;
import dev.authorises.vortechfactions.rng.Rarity;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class DebugMask {
   // @Override
    public String getDisplayName() {
        return "Debug";
    }

  //  @Override
    public String getName() {
        return "debug";
    }

  //  @Override
    public String getValue() {
        return "56b503694116ef544acd882c21864af4381acb5102038197f9dc5dea190b9408";
    }

   // @Override
    public Rarity getRarity() {
        return Rarity.DIVINE;
    }

   // @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorUtils.format("&7&oThis mask was the first ever mask created,"));
        lore.add(ColorUtils.format("&7&oand it has the power of the &d&lDEBUGGER LORD"));
        lore.add(ColorUtils.format(""));
        lore.add(ColorUtils.format("&7Created by &b&nAuthorises&7."));
        return lore;
    }

   // @Override
    public void loopEffects(Player p) {
        p.sendMessage("loopeffects");
    }

   // @Override
    public void putOn(Player p) {
        p.sendMessage("puton");
    }

   // @Override
    public void takeOff(Player p) {
        p.sendMessage("takeoff");
    }

   // @Override
    public void killWith(Player p) {
        p.sendMessage("killwith");
    }

   // @Override
    public void dieWith(Player p) {
        p.sendMessage("diewith");
    }

   // @Override
    public void placeBlock(BlockPlaceEvent e) {
        e.getPlayer().sendMessage("placeblock");
    }

   // @Override
    public void breakBlock(BlockBreakEvent e) {
        e.getPlayer().sendMessage("breakblock");
    }

   // @Override
    public void leftClickBlock(PlayerInteractEvent e) {
        e.getPlayer().sendMessage("leftclickblock");
    }

   // @Override
    public void rightClickBlock(PlayerInteractEvent e) {
        e.getPlayer().sendMessage("rightclickblock");
    }

   // @Override
    public void leftClickAir(PlayerInteractEvent e) {
        e.getPlayer().sendMessage("leftclickair");
    }

   // @Override
    public void rightClickAir(PlayerInteractEvent e) {
        e.getPlayer().sendMessage("rightclickair");
    }
}
