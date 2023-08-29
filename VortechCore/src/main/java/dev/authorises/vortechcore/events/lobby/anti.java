package dev.authorises.vortechcore.events.lobby;

import dev.authorises.vortechcore.utilities.ColorUtils;
import dev.authorises.vortechcore.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class anti implements Listener {


    @EventHandler
    public void e(PlayerDropItemEvent e){
        e.setCancelled(true);
        e.getPlayer().updateInventory();
    }

    @EventHandler
    public void e(EntityDamageEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void e(BlockBreakEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void e(BlockPlaceEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void e(PlayerDeathEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void e(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void e(InventoryClickEvent e){
        if(e.getInventory().getType().equals(InventoryType.PLAYER)) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void e(PlayerInteractEvent e){
        if(e.getPlayer().getGameMode()!=GameMode.CREATIVE) {
            e.setCancelled(true);
            if (e.getItem() == null) return;
            if (!e.getItem().getType().equals(Material.COMPASS)) return;
            e.getPlayer().performCommand("play");
        }
    }

    @EventHandler
    public void e(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.getInventory().clear();
        p.getInventory().setItem(4, new ItemBuilder(Material.COMPASS).setDisplayName(ColorUtils.format("&bGame Selector &f(Right Click)")).getItemStack());
        e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), -96D, 174D, -416D));
        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 2F);
    }

    @EventHandler
    public void e(PlayerMoveEvent e){
        if(e.getTo().getY()<150d && e.getPlayer().getGameMode()!= GameMode.CREATIVE){
            e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), -96D, 174D, -416D));
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2F, 2F);
        }
    }

}
