package dev.authorises.vortechfactions.masks.masks;

import com.mojang.authlib.BaseUserAuthentication;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.masks.Mask;
import dev.authorises.vortechfactions.masks.maskUtils;
import dev.authorises.vortechfactions.rng.Rarity;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class MysteryMask implements Mask {
    @Override
    public String getDisplayName() {
        return "Mystery";
    }

    @Override
    public String getName() {
        return "mystery";
    }

    @Override
    public String getValue() {
        return "badc048a7ce78f7dad72a07da27d85c0916881e5522eeed1e3daf217a38c1a";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public ArrayList<String> getLore() {
        return null;
    }

    @Override
    public void loopEffects(Player p) {

    }

    @Override
    public void putOn(Player p) {

    }

    @Override
    public void takeOff(Player p) {
;
    }

    @Override
    public void killWith(Player p) {

    }

    @Override
    public void dieWith(Player p) {

    }

    @Override
    public void placeBlock(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void breakBlock(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void leftClickAir(PlayerInteractEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void rightClickAir(PlayerInteractEvent e) {
        for(ItemStack i : e.getPlayer().getInventory().getContents()){
            try {
                if (new NBTItem(i).hasKey("mask_name")) {
                    if (new NBTItem(i).getString("mask_name").equals("mystery")) {
                        e.getPlayer().getInventory().remove(i);
                        e.getPlayer().sendMessage(ColorUtils.format("&f(&b!&f) You are opening your &b&lMystery Mask&f..."));
                        Mask m = VortechFactions.masks.get(new Random().nextInt(VortechFactions.masks.size()));
                        for(Player p : Bukkit.getOnlinePlayers()){
                            p.sendMessage(ColorUtils.format(e.getPlayer().getDisplayName()+"&f has received a "+m.getRarity().getColor()+m.getDisplayName()+" Mask&f!"));
                        }
                        e.getPlayer().getInventory().addItem(maskUtils.getMaskItem(m));
                    }
                }
            }catch (Exception ex){

            }
        }
    }
}
