package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.rng.RNGChanceType;
import dev.authorises.vortechfactions.rng.RNGManager;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.TitleUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class CaneBreakListener implements Listener {

    @EventHandler
    public void blockBreak(BlockBreakEvent e) throws InstantiationException, IllegalAccessException {
        if(e.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)){
            if(e.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_HOE)){
                Player p = e.getPlayer();
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                float caneBroke = 0;
                Block b = e.getBlock();
                e.setCancelled(true);
                while(b.getType().equals(Material.SUGAR_CANE_BLOCK)){
                    caneBroke+=1;
                    RNGManager.rngCheck(e.getPlayer(), RNGChanceType.FARMING);
                    b.setType(Material.AIR, false);
                    p.getInventory().addItem(new ItemStack(Material.SUGAR_CANE));
                    b=b.getRelative(BlockFace.UP);
                }
                TitleUtils.sendActionBar(p, ColorUtils.format("You broke &b"+caneBroke+"x &aSugar Cane"));

            }
        }
    }

}
