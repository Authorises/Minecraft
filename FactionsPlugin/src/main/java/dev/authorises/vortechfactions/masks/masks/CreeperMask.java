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

public class CreeperMask implements Mask {
    @Override
    public String getDisplayName() {
        return "Creeper";
    }

    @Override
    public String getName() {
        return "creeper";
    }

    @Override
    public String getValue() {
        return "f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorUtils.format("&7Strength II"));
        lore.add(ColorUtils.format("&7Fire Resistance I"));
        return lore;
    }

    @Override
    public void loopEffects(Player p) {
        p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, 1, true, false));
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 80, 0, true, false));
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
