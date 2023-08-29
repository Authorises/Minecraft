package dev.authorises.vortechfactions.utilities;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.FPlayer;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.settings.BooleanSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class cactusCacheManager {

    public static void start(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new BukkitRunnable() {
            @Override
            public void run() {
                Integer x = 0;
                for(String i : VortechFactions.cactusCache.keySet()){
                    Faction f = VortechFactions.api.getFactionById(i);
                    x+=VortechFactions.cactusCache.get(i);
                    float money = Items.materialSellValues.get(Material.CACTUS)*VortechFactions.cactusCache.get(i);
                    f.setBank(f.getBank()+ money);
                    for(FPlayer p : f.getOnlineMembers()){
                        if(((BooleanSetting)VortechFactions.settingsManager.getSetting("cactus-sell-visible")).getState(p.getUniqueId())) {
                            TitleUtils.sendActionBar(p.getPlayer(), ColorUtils.format("&b[ &fCactus&b ] &a+$" + money + "&f(&b+" + VortechFactions.cactusCache.get(i) + "&f)"));
                        }
                    }
                    VortechFactions.cactusCache.remove(i);
                }
            }
        },0,100);
    }

}
