package dev.authorises.cavelet.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ItemBuilder;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.Random;

public class FarmsBreakBlockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreakBlockEvent(BlockBreakBlockEvent e){
        if(e.getBlock().getType()==Material.WHEAT||e.getBlock().getType()==Material.POTATOES){
            e.getDrops().clear();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreakBlockEvent(PlayerInteractEvent e){
        if(e.getAction()==Action.PHYSICAL){
            if(e.getClickedBlock().getType()==Material.FARMLAND){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakWheat(BlockBreakEvent e){
        if(e.getBlock().getType().equals(Material.WHEAT)){
            if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                int x = (int) p.getLocation().getX();
                int y = (int) p.getLocation().getY();
                int z = (int) p.getLocation().getZ();
                boolean in = (Cavelet.container.get(BukkitAdapter.adapt(Bukkit.getWorlds().get(0))).getRegion("spawn").contains(x,y,z));
                if(e.getBlock().getBlockData() instanceof Ageable) {

                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.WHEAT_SEEDS, 2));

                    if(((Ageable) e.getBlock().getBlockData()).getAge()==7 && !in){
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new CItem(Cavelet.customItemsManager.getItemById("WHEAT")).getItemStack());
                        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                        mp.addFarmingXP(15, p);
                    }

                    if(in) {
                        BlockData b = e.getBlock().getBlockData();
                        ((Ageable) b).setAge(0);
                        e.getBlock().setBlockData(b);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getProvidingPlugin(Cavelet.class), new Runnable() {
                            @Override
                            public void run() {
                                ((Ageable) b).setAge(7);
                                e.getBlock().setBlockData(b);
                            }
                        }, 200L);
                    }else{
                        e.getBlock().setType(Material.AIR);
                    }
                }
            }
        } else if (e.getBlock().getType().equals(Material.POTATOES)) {
            if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                Player p = e.getPlayer();
                if(e.getBlock().getBlockData() instanceof Ageable) {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new CItem(Cavelet.customItemsManager.getItemById("GOLDEN_POTATO_SEED")).getItemStack());
                    if(((Ageable) e.getBlock().getBlockData()).getAge()==7){
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(),  new CItem(Cavelet.customItemsManager.getItemById("GOLDEN_POTATO")).getItemStack());
                        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                        mp.addFarmingXP(40, p);
                    }
                }
                e.setCancelled(true);
                e.getBlock().setType(Material.AIR, true);
            }
        }
    }


}
