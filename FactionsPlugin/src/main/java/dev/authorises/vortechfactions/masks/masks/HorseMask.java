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

public class HorseMask implements Mask {
    @Override
    public String getDisplayName() {
        return "Horse";
    }

    @Override
    public String getName() {
        return "horse";
    }

    @Override
    public String getValue() {
        return "2990821951fd9ab9301aebf5d76a9b60f15ebf619e1c9e8d668919d394d9a933";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorUtils.format("&7Speed III"));
        return lore;
    }

    @Override
    public void loopEffects(Player p) {
        p.removePotionEffect(PotionEffectType.SPEED);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 2, true, false));
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
