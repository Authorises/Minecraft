package dev.authorises.vortechfactions.masks;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.vortechfactions.VortechFactions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class maskManager {
    public static void start(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new BukkitRunnable() {
            @Override
            public void run() {
            for(Player p : Bukkit.getOnlinePlayers()){
                Mask m = maskUtils.getMask(p);
                if(m!=null){
                    m.loopEffects(p);
                }
            }
            }
        },0,60);
    }
}
