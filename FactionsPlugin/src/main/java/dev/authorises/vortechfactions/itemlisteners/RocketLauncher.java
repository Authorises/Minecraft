package dev.authorises.vortechfactions.itemlisteners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;

public class RocketLauncher implements ItemHandler {

    public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    @Override
    public String itemName() {
        return "rocketlauncher";
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
        Block b = getTargetBlock(e.getPlayer(), 1000);
        VortechFactions.rocketLocs.add(b.getLocation());
        e.getPlayer().sendMessage(ColorUtils.format("&f- &cAdded Location at: &b" + b.getX() + " " + b.getY() + " " + b.getZ()+" &f(&b"+VortechFactions.rocketLocs.size()+"&f)"));
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void rightClickPlayer(PlayerInteractAtEntityEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickAir(PlayerInteractEvent e) throws Exception {
        if(e.getPlayer().isSneaking()){
            VortechFactions.rocketLocs.clear();
            e.getPlayer().sendMessage(ColorUtils.format("&cCleared locations!"));
        }
        for(Location l : VortechFactions.rocketLocs) {
            e.getPlayer().sendMessage(ColorUtils.format("&cLaunching attack!!!"));
            l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 10.0F);
            e.getPlayer().sendMessage(ColorUtils.format("&f- &cLaunched missile at: &b" + l.getX() + " " + l.getY() + " " + l.getZ()));
        }
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void leftClickPlayer(EntityDamageByEntityEvent e) throws Exception {
        e.setCancelled(true);
    }

    @Override
    public void place(BlockPlaceEvent e) throws Exception {
        e.setCancelled(true);
    }
}
