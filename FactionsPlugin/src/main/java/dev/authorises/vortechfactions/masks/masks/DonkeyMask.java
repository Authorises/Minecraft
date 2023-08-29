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

public class DonkeyMask implements Mask {
    @Override
    public String getDisplayName() {
        return "Donkey";
    }

    @Override
    public String getName() {
        return "donkey";
    }

    @Override
    public String getValue() {
        return "2144bdad6bc18a3716b196dc4a4bd695265eccaadd0d945beb976443f82693b";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorUtils.format("&7Speed II"));
        return lore;
    }

    @Override
    public void loopEffects(Player p) {
        p.removePotionEffect(PotionEffectType.SPEED);
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1, true, false));
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
