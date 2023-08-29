package dev.authorises.cavelet.intelligentblocks.blocks;

import com.google.gson.JsonObject;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import dev.authorises.cavelet.mining.Ore;
import dev.authorises.cavelet.mining.OreRarity;
import dev.authorises.cavelet.mining.OreSpawnRarity;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.LootDecider;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MiningNode extends IntelligentBlock {

    private String BLOCK_NAME = "MINING_NODE";
    private Integer taskId = null;

    // Empty constructor for getting name for initial gathering of intelligent blocks
    public MiningNode(){
        setBlockName(BLOCK_NAME);
    }

    // Creating block (i.e. placing a intelligent block)
    public MiningNode(Location location){
        setBlockName(BLOCK_NAME);
        setBlockUuid(UUID.randomUUID());
        setLocation(location);
        setData(new JsonObject());
    }

    // Loading block from storage
    public MiningNode(Location location, UUID blockUuid, JsonObject data){
        setBlockName(BLOCK_NAME);
        setLocation(location);
        setBlockUuid(blockUuid);
        setData(data);
    }

    @Override
    public void shiftRightClick(PlayerInteractEvent event) {

    }

    @Override
    public void rightClick(PlayerInteractEvent event) {

    }

    @Override
    public void shiftLeftClick(PlayerInteractEvent event) {

    }

    @Override
    public void leftClick(PlayerInteractEvent event) {

    }

    private Ore getRandomOre(){
        return new LootDecider<Ore>()
                        .addChance(28.565, Cavelet.oreManager.getOre("coal"))
                        .addChance(17.5, Cavelet.oreManager.getOre("iron"))
                        .addChance(12.5, Cavelet.oreManager.getOre("copper"))
                        .addChance(7.5, Cavelet.oreManager.getOre("gold"))
                        .addChance(5.5, Cavelet.oreManager.getOre("ruby"))
                        .addChance(3.5, Cavelet.oreManager.getOre("sapphire"))
                        .addChance(1.0, Cavelet.oreManager.getOre("diamond"))
                        .addChance(5, Cavelet.oreManager.getOre("processed_coal"))
                        .addChance(4, Cavelet.oreManager.getOre("processed_iron"))
                        .addChance(3, Cavelet.oreManager.getOre("processed_copper"))
                        .addChance(2, Cavelet.oreManager.getOre("processed_gold"))
                        .addChance(1, Cavelet.oreManager.getOre("processed_ruby"))
                        .addChance(0.1, Cavelet.oreManager.getOre("processed_sapphire"))
                        .addChance(0.05, Cavelet.oreManager.getOre("processed_diamond"))
                        .addChance(5, Cavelet.oreManager.getOre("pure_coal"))
                        .addChance(2, Cavelet.oreManager.getOre("pure_iron"))
                        .addChance(1, Cavelet.oreManager.getOre("pure_copper"))
                        .addChance(0.5, Cavelet.oreManager.getOre("pure_gold"))
                        .addChance(0.25, Cavelet.oreManager.getOre("pure_ruby"))
                        .addChance(0.025, Cavelet.oreManager.getOre("pure_sapphire"))
                        .addChance(0.01, Cavelet.oreManager.getOre("pure_diamond"))
                        .decide();


    }

    @Override
    public void broken(BlockBreakEvent event) {
        for (Ore o : Cavelet.oreManager.getOres()) {
            if (o.getBlockMaterial().equals(event.getBlock().getType())) {
                event.setExpToDrop(0);
                if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta() instanceof Damageable){
                    ItemMeta m = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                    Damageable d = (Damageable) m ;
                    d.setDamage(d.getDamage()+new Random().nextInt(1, 4));
                    event.getPlayer().getInventory().getItemInMainHand().setItemMeta(m);
                }
                event.getBlock().setType(Material.YELLOW_STAINED_GLASS);
                event.setCancelled(true);
                event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), ((BasicCustomItem)Cavelet.customItemsManager.getItemById(o.getOreId().toUpperCase()).getItem()).getItem());
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
                try{
                    for(CustomEnchant c : new CItem(event.getPlayer().getInventory().getItemInMainHand()).getCustomEnchants()){
                        if(c.getType().equals(CustomEnchantType.EXPERIBOOST)){
                            if(!experiboost){
                                experiboost=true;
                            }
                        }
                    }
                }catch (Exception e){

                }

                if(experiboost){
                    xpAdded*=2;
                }
                Cavelet.cachedMPlayers.get(event.getPlayer().getUniqueId()).addMiningXP(xpAdded, event.getPlayer());
                this.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getProvidingPlugin(Cavelet.class), new Runnable() {
                    @Override
                    public void run() {
                        event.getBlock().setType(getRandomOre().getBlockMaterial());
                    }
                },500L);
            }
        }
    }

    @Override
    public void shiftBroken(BlockBreakEvent event) {
        event.setDropItems(false);
        event.setExpToDrop(0);
        Cavelet.intelligentBlockManager.removeBlock(this);
        event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), new CItem(Cavelet.customItemsManager.getItemById("MINING_NODE")).getItemStack());
        if(this.taskId!=null){
            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }

    @Override
    public void placed(IntelligentBlockPlacedEvent event) throws InterruptedException {
        event.getPlaceEvent().getBlock().setType(Material.YELLOW_STAINED_GLASS);
        this.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getProvidingPlugin(Cavelet.class), new Runnable() {
            @Override
            public void run() {
                event.getPlaceEvent().getBlock().setType(getRandomOre().getBlockMaterial());
            }
        },100L);
    }

}
