package dev.authorises.cavelet.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.mining.Ore;
import dev.authorises.cavelet.mining.OreRarity;
import dev.authorises.cavelet.mining.OreSpawnRarity;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.LootDecider;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MinesBreakBlockListener implements Listener {

    @EventHandler
    public void playerBreakBlock(BlockBreakEvent e) throws InvalidItemIdException, ParseException {
        Player p = e.getPlayer();
        int x = (int) p.getLocation().getX();
        int y = (int) p.getLocation().getY();
        int z = (int) p.getLocation().getZ();
        boolean in = (Cavelet.container.get(BukkitAdapter.adapt(Bukkit.getWorlds().get(0))).getRegion("spawn").contains(x,y,z));
        if(!in) return;
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        if(p.getGameMode().equals(GameMode.SURVIVAL)) {
            for (Ore o : Cavelet.oreManager.getOres()) {
                if (o.getBlockMaterial().equals(e.getBlock().getType())) {
                    e.setExpToDrop(0);
                    if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() instanceof Damageable){
                        ItemMeta m = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                        Damageable d = (Damageable) m ;
                        d.setDamage(d.getDamage()+new Random().nextInt(1, 4));
                        e.getPlayer().getInventory().getItemInMainHand().setItemMeta(m);
                    }
                    e.getBlock().setType(Material.STONE);
                    e.setCancelled(true);
                    e.getPlayer().getInventory().addItem(((BasicCustomItem)Cavelet.customItemsManager.getItemById(o.getOreId().toUpperCase()).getItem()).getItem());
                    int xpAdded = 0;
                    switch (o.getRarity()){
                        case DEFAULT:
                            xpAdded+=10;
                            break;
                        case PROCESSED:
                            xpAdded+=30;
                            break;
                        case PURE:
                            xpAdded+=100;
                            break;
                    }
                    switch (o.getSpawnRarity()){
                        case COMMON:
                            xpAdded*=1;
                            break;
                        case UNCOMMON:
                            xpAdded*=2;
                            break;
                        case RARE:
                            xpAdded*=3;
                            break;
                        case DIVINE:
                            xpAdded*=10;
                            break;
                    }
                    boolean experiboost = false;
                    for(CustomEnchant c : new CItem(p.getInventory().getItemInMainHand()).getCustomEnchants()){
                        if(c.getType().equals(CustomEnchantType.EXPERIBOOST)){
                            if(!experiboost){
                                experiboost=true;
                            }
                        }
                    }
                    if(experiboost){
                        xpAdded*=2;
                    }
                    Cavelet.cachedMPlayers.get(e.getPlayer().getUniqueId()).addMiningXP(xpAdded, e.getPlayer());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getProvidingPlugin(Cavelet.class), new Runnable() {
                        @Override
                        public void run() {
                            e.getBlock().setType(
                                    new LootDecider<Ore>()
                                            .addChance(50.0, Cavelet.oreManager.getOre("coal"))
                                            .addChance(30.0, Cavelet.oreManager.getOre("iron"))
                                            .addChance(10.0, Cavelet.oreManager.getOre("processed_coal"))
                                            .addChance(5.0, Cavelet.oreManager.getOre("processed_iron"))
                                            .addChance(4.0, Cavelet.oreManager.getOre("pure_coal"))
                                            .addChance(1.0, Cavelet.oreManager.getOre("pure_iron"))
                                            .decide()
                                    .getBlockMaterial()
                            );
                        }
                    },100L);
                }
            }
        }
    }


}
