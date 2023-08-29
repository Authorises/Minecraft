package dev.authorises.vortechfactions.items;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ItemHandler {

    public String itemName();
    public Boolean requiresPermission();
    public String permission();
    public String permissionMessage();

    void rightClickAir(PlayerInteractEvent e) throws Exception;
    void rightClickBlock(PlayerInteractEvent e) throws Exception;
    void rightClickPlayer(PlayerInteractAtEntityEvent e) throws Exception;
    void leftClickAir(PlayerInteractEvent e) throws Exception;
    void leftClickBlock(PlayerInteractEvent e) throws Exception;
    void leftClickPlayer(EntityDamageByEntityEvent e) throws Exception;
    void place(BlockPlaceEvent e) throws Exception;

}
