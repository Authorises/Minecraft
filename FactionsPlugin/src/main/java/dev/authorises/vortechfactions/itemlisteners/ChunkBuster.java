package dev.authorises.vortechfactions.itemlisteners;

import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChunkBuster implements ItemHandler {
    @Override
    public String itemName() {
        return "chunkbuster";
    }

    @Override
    public Boolean requiresPermission() {
        return null;
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String permissionMessage() {
        return null;
    }

    @Override
    public void rightClickAir(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent e) throws Exception {

    }

    @Override
    public void rightClickPlayer(PlayerInteractAtEntityEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickAir(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickPlayer(EntityDamageByEntityEvent e) throws Exception {

    }

    @Override
    public void place(BlockPlaceEvent e) throws Exception {
        e.getBlock().setType(Material.AIR);
        e.getPlayer().sendMessage(ColorUtils.format("&f(&b!&f) You have placed a &b&nChunk Buster&f!"));
        Location l = e.getBlock().getLocation();
        int X = l.getChunk().getX() * 16;
        int Z = l.getChunk().getZ() * 16;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < l.getY(); y++) {
                    Block b = l.getWorld().getBlockAt(X+x, y, Z+z);
                    if(b.getType()!=Material.BEDROCK){
                        b.setType(Material.AIR, false);
                    }
                }
            }
        }
    }
}
