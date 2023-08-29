package dev.authorises.vortechfactions.masks.masks;

import dev.authorises.vortechfactions.masks.Mask;
import dev.authorises.vortechfactions.rng.Rarity;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class TurtleMask implements Mask {
    @Override
    public String getDisplayName() {
        return "Turtle";
    }

    @Override
    public String getName() {
        return "turtle";
    }

    @Override
    public String getValue() {
        return "0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorUtils.format("&7Resistance I"));
        return lore;
    }

    @Override
    public void loopEffects(Player p) {
        p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 0, true, false));
    }

    @Override
    public void putOn(Player p) {

    }

    @Override
    public void takeOff(Player p) {

    }

    @Override
    public void killWith(Player p) {

    }

    @Override
    public void dieWith(Player p) {

    }

    @Override
    public void placeBlock(BlockPlaceEvent e) {

    }

    @Override
    public void breakBlock(BlockBreakEvent e) {

    }

    @Override
    public void leftClickBlock(PlayerInteractEvent e) {

    }

    @Override
    public void rightClickBlock(PlayerInteractEvent e) {

    }

    @Override
    public void leftClickAir(PlayerInteractEvent e) {

    }

    @Override
    public void rightClickAir(PlayerInteractEvent e) {

    }
}
