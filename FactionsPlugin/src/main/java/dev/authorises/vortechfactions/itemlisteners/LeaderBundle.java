package dev.authorises.vortechfactions.itemlisteners;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LeaderBundle implements ItemHandler {
    @Override
    public String itemName() {
        return "leaderbundle";
    }

    @Override
    public Boolean requiresPermission() {
        return false;
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
        e.setCancelled(true);
    }


    @Override
    public void place(BlockPlaceEvent e) throws Exception {
        Player p = e.getPlayer();
        p.sendMessage(ColorUtils.format("&b&lBundle!&f You have placed your Leader Bundle!"));
        e.setCancelled(true);
        if(p.getItemInHand().getAmount()>=2){
            p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
        }else {
            p.setItemInHand(null);
        }
        Block a = e.getBlock();
        Location l = a.getRelative(BlockFace.UP).getLocation();
        double x = l.getX();
        double z = l.getZ();
        l.add(x > 0 ? 0.5 : -0.5, 0.0, z > 0 ? 0.5 : -0.5);
        l.setPitch(0F);
        l.setYaw(0F);
        l=a.getLocation();
        Entity et = l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        ArmorStand as = (ArmorStand) et;
        as.setHeadPose(EulerAngle.ZERO);
        as.setHelmet(new ItemStack(Material.BEACON));
        as.setVisible(false);
        as.setGravity(false);
        VortechFactions.invincibleEntities.add(et.getUniqueId());
        long d = 10;
        final long[] add = {10};
        Sound s = Sound.ITEM_PICKUP;
        while (d<200) {
            d+= add[0];
            if(d >50){
                add[0] = 7;
            }
            if(d >100){
                add[0] = 5;
            }
            if(d >150){
                add[0] = 3;
                s=Sound.ORB_PICKUP;
            }
            if(d >180){
                add[0] = 1;
            }
            long finalD = d;
            Sound finalS = s;
            Bukkit.getScheduler().runTaskLater(VortechFactions.getPlugin(VortechFactions.class), new Runnable() {
                @Override
                public void run() {
                    if(finalD>100){
                        as.setSmall(true);
                    }
                    if(finalD >199){
                        p.playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
                        VortechFactions.invincibleEntities.remove(et.getUniqueId());
                        et.remove();
                        p.sendMessage(ColorUtils.format("&b&lBundle!&f You have received&a $500,000&f, 16x &bChunk Buster&f, 2x &bStandard Kit"));
                        p.getInventory().addItem(Items.items.get("standardkit"));
                        p.getInventory().addItem(Items.items.get("standardkit"));
                        // cba to make a loop
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        p.getInventory().addItem(Items.items.get("chunkbuster"));
                        VortechFactions.econ.depositPlayer(p, 500000);
                    }else{
                        p.playSound(p.getLocation(), finalS, 1F, 1F);
                        as.setHelmet(new ItemStack(Material.STAINED_GLASS, 1, (short) ThreadLocalRandom.current().nextLong(0, 15)));
                    }
                }
            }, d);
        }
    }

}
