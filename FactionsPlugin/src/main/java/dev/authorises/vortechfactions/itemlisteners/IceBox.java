package dev.authorises.vortechfactions.itemlisteners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.items.RandomItemType;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class IceBox implements ItemHandler {

    @Override
    public String itemName() {
        return "icebox";
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
        VortechFactions.econ.depositPlayer(p, 100000);
        p.getInventory().addItem(Items.random(RandomItemType.FROST));
        p.getInventory().addItem(Items.items.get("frostysword"));
        p.sendMessage(ColorUtils.format("&bIce Box!&F You received &a$100,000&f and &b1x Frost Armour&f and &b1x Frost Sword"));
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
