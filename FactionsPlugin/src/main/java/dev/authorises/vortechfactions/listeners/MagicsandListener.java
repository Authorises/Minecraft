package dev.authorises.vortechfactions.listeners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MagicsandListener implements Listener {
    public HashMap<UUID, ArrayList<Block>> blocksArray = new HashMap();
    private List<Block> getBlockList;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlockPlaced();
        final Location blockLocation = block.getLocation();
        ArrayList blockList;
        if (block.getData() == 4 && block.getType() == Material.WOOL && player.getItemInHand().getItemMeta().hasEnchants()) {
            player.sendMessage(ColorUtils.format("&f(&b!&f) You placed a &b&lMagic Sand&f."));
            blockList = new ArrayList();
            blockList.add(block);
            this.getBlockList = blockList;
            this.blocksArray.put(player.getUniqueId(), blockList);
            ArrayList finalBlockList = blockList;
            (new BukkitRunnable() {
                public void run() {
                    if (block.getData() == 4 && block.getType() == Material.WOOL) {
                        if (blockLocation.getWorld().getBlockAt(blockLocation).getType() != Material.AIR && blockLocation.getWorld().getBlockAt(blockLocation).getType() == Material.WOOL) {
                            Location sandLocation = blockLocation.clone();
                            sandLocation.add(0.0D, -1.0D, 0.0D);
                            if (blockLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && blocksArray.containsKey(player.getUniqueId())) {
                                blockLocation.getBlock().getRelative(BlockFace.DOWN).setType(Material.SAND);
                            }
                        }
                    } else {
                        this.cancel();
                    }

                    if (!blocksArray.containsKey(player.getUniqueId()) && !player.isOnline()) {
                        Iterator var3 = finalBlockList.iterator();

                        while(var3.hasNext()) {
                            Block b = (Block)var3.next();
                            b.setType(Material.SPONGE);
                        }
                    }

                }
            }).runTaskTimer(JavaPlugin.getPlugin(VortechFactions.class), 15L, 0L);
        }

        if (block.getData() == 7 && block.getType() == Material.WOOL && player.getItemInHand().getItemMeta().hasEnchants()) {
            player.sendMessage(ColorUtils.format("&f(&b!&f) You placed a &b&lMagic Gravel&f."));
            blockList = new ArrayList();
            blockList.add(block);
            this.blocksArray.put(player.getUniqueId(), blockList);
            ArrayList finalBlockList1 = blockList;
            (new BukkitRunnable() {
                public void run() {
                    if (block.getData() == 7 && block.getType() == Material.WOOL) {
                        if (blockLocation.getWorld().getBlockAt(blockLocation).getType() != Material.AIR && blockLocation.getWorld().getBlockAt(blockLocation).getType() == Material.WOOL) {
                            Location sandLocation = blockLocation.clone();
                            sandLocation.add(0.0D, -1.0D, 0.0D);
                            if (blockLocation.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && blocksArray.containsKey(player.getUniqueId())) {
                                blockLocation.getBlock().getRelative(BlockFace.DOWN).setType(Material.GRAVEL);
                            }
                        }
                    } else {
                        this.cancel();
                    }

                    if (!blocksArray.containsKey(player.getUniqueId()) && !player.isOnline()) {
                        Iterator var3 = finalBlockList1.iterator();

                        while(var3.hasNext()) {
                            Block b = (Block)var3.next();
                            b.setType(Material.WOOL);
                            b.setData(DyeColor.SILVER.getWoolData());
                        }
                    }

                }
            }).runTaskTimer(JavaPlugin.getPlugin(VortechFactions.class), 15L, 0L);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.blocksArray.remove(player.getUniqueId());
    }
}
