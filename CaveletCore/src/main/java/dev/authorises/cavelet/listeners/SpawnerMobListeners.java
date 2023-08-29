package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.InventoryUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.json.simple.parser.ParseException;

import javax.crypto.spec.PSource;
import java.util.Random;

public class SpawnerMobListeners implements Listener {

    private final Random random = new Random();

    private Double multiplier(MProfile mp){
        return 1+((Math.floor(mp.getCombatLevel().floatValue()/5F)*15)/100);
    }

    @EventHandler
    public void entityDies(EntityDeathEvent e) throws ParseException {
        Location location = e.getEntity().getLocation();
        switch (e.getEntity().getType()){
            case PIG:{
                e.getDrops().clear();
                e.setDroppedExp(0);
                if(e.getEntity().getKiller()!=null){
                    Player p = e.getEntity().getKiller();
                    p.getInventory().addItem(new ItemStack(Material.COOKED_PORKCHOP));
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    int soulsToAdd =  random.nextInt(1,3);
                    soulsToAdd = (int) Math.floor(soulsToAdd*multiplier(mp));
                    mp.addCombatXP(10, p, soulsToAdd);
                    mp.setDarkSouls(mp.getDarkSouls()+soulsToAdd);
                }
                break;
            }
            case SHEEP:{
                e.getDrops().clear();
                e.setDroppedExp(0);
                if(e.getEntity().getKiller()!=null){
                    Player p = e.getEntity().getKiller();
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    int soulsToAdd = random.nextInt(3,6);
                    soulsToAdd = (int) Math.floor(soulsToAdd*multiplier(mp));
                    mp.addCombatXP(50, p, soulsToAdd);
                    mp.setDarkSouls(mp.getDarkSouls()+soulsToAdd);
                }
                e.getDrops().add(new CItem(Cavelet.customItemsManager.getItemById("SHEEP_ESSENCE"), 1).getItemStack());
                break;
            }
            case SKELETON:{
                e.getDrops().clear();
                e.setDroppedExp(0);
                if(e.getEntity().getKiller()!=null){
                    Player p = e.getEntity().getKiller();
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    int soulsToAdd = random.nextInt(6,12);
                    soulsToAdd = (int) Math.floor(soulsToAdd*multiplier(mp));
                    mp.addCombatXP(100, p, soulsToAdd);
                    mp.setDarkSouls(mp.getDarkSouls()+soulsToAdd);
                }
                e.getDrops().add(new CItem(Cavelet.customItemsManager.getItemById("SKELETAL_ESSENCE"), 1).getItemStack());
                break;
            }
            case VINDICATOR:{
                e.getDrops().clear();
                e.setDroppedExp(0);
                if(e.getEntity().getKiller()!=null){
                    Player p = e.getEntity().getKiller();
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    int soulsToAdd = random.nextInt(12,30);
                    soulsToAdd = (int) Math.floor(soulsToAdd*multiplier(mp));
                    mp.addCombatXP(180, p, soulsToAdd);
                    mp.setDarkSouls(mp.getDarkSouls()+soulsToAdd);
                }
                int i = random.nextInt(1, 21);
                if(i==20){
                    e.getDrops().add(new CItem(Cavelet.customItemsManager.getItemById("CTHULU_FRAGMENT"), 1).getItemStack());
                }else{
                    e.getDrops().add(new CItem(Cavelet.customItemsManager.getItemById("VINDICATOR_ESSENCE"), 1).getItemStack());
                }
                break;
            }
        }

    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final ItemStack is = event.getItemInHand();

        if (is.getType() == Material.SPAWNER || is.getType() == Material.LEGACY_MOB_SPAWNER) {
            final BlockState blockState = event.getBlockPlaced().getState();
            if (blockState instanceof CreatureSpawner) {
                final CreatureSpawner spawner = (CreatureSpawner) blockState;
                final EntityType type = InventoryUtil.getEntityType(event.getItemInHand());
                if (type != null) {
                    spawner.setSpawnedType(type);
                    spawner.update();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        Block b = event.getBlock();

        if (b.getType() == Material.SPAWNER) {
            event.setDropItems(false);
            event.setExpToDrop(0);
            //event.setCancelled(true);
            //event.getBlock().setType(Material.AIR, true);
            BlockState blockState = b.getState();
            if (blockState instanceof CreatureSpawner) {
                final CreatureSpawner spawner = (CreatureSpawner) blockState;
                ItemStack item = new ItemStack(Material.SPAWNER);
                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                meta.displayName(Cavelet.miniMessage.deserialize("<!italic><lang:"+spawner.getSpawnedType().translationKey()+"> Spawner"));
                CreatureSpawner spawnerMeta = (CreatureSpawner) meta.getBlockState();
                spawnerMeta.setSpawnedType(spawner.getSpawnedType());
                meta.setBlockState(spawnerMeta);
                item.setItemMeta(meta);

                event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
            }
        }
    }
}
