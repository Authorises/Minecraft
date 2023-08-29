package dev.authorises.vortechfactions.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class TntProtectionListener implements Listener {

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent event) {
        if (event.getEntityType() == EntityType.PRIMED_TNT) {
            for(int c = 0; c < event.blockList().size(); ++c) {
                Block b = (Block)event.blockList().get(c);
                Location l = b.getLocation();
                int x = l.getBlockX();
                int z = l.getBlockZ();
                int y = l.getBlockY();
                if (b.getType() == Material.NETHERRACK) {
                    event.blockList().remove(c);
                    --c;
                } else {
                    for(int i = y; i >= 0; --i) {
                        if (l.getWorld().getBlockAt(x, i, z).getType() == Material.NETHERRACK) {
                            event.blockList().remove(c);
                            --c;
                            break;
                        }
                    }
                }
            }
        }

    }

}
