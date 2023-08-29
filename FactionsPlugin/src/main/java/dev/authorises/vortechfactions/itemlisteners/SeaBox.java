package dev.authorises.vortechfactions.itemlisteners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SeaBox implements ItemHandler {
    @Override
    public String itemName() {
        return "seabox";
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
        Player p = e.getPlayer();
        if(p.getItemInHand().getAmount()>=2){
            p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
        }else {
            p.setItemInHand(null);
        }
        VortechFactions.econ.depositPlayer(p, 50000);
        p.getInventory().addItem(Items.items.get("seaweapon"));
        p.sendMessage(ColorUtils.format("&bSea Box!&F You received &a$50,000&f and &b1x &n&lSea Weakon"));
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
        e.setCancelled(true);
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
