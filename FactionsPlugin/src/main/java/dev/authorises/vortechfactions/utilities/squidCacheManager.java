package dev.authorises.vortechfactions.utilities;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.FPlayer;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.objects.ChunkType;
import dev.authorises.vortechfactions.settings.BooleanSetting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class squidCacheManager {

    public static void start(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<Faction, Float> moneyMade = new HashMap<>();
                for(StackedEntity entity: WildStackerAPI.getWildStacker().getSystemManager().getStackedEntities()){
                    if(entity.getType().equals(EntityType.SQUID)){
                        Location l = entity.getLocation();
                        if(chunkUtils.getType(l.getChunk()).equals(ChunkType.SPAWNERS)){
                            Faction f = VortechFactions.api.getFactionAt(l.getChunk());
                            int a = entity.getStackAmount()-1;
                            entity.setStackAmount(1, true);
                            float money=4*a;
                            if(!(moneyMade.containsKey(f))){
                                moneyMade.put(f, money);
                            }else {
                                moneyMade.put(f, moneyMade.get(f) + money);
                            }

                        }

                    }
                }
                for(Faction f : moneyMade.keySet()){
                    f.setBank(f.getBank()+ moneyMade.get(f));
                    if(moneyMade.get(f)!=0) {
                        for (FPlayer p : f.getOnlineMembers()) {
                            if(((BooleanSetting)VortechFactions.settingsManager.getSetting("squid-sell-visible")).getState(p.getUniqueId())) {
                                TitleUtils.sendActionBar(p.getPlayer(), ColorUtils.format("&b[ &fSquids&b ] &a+$" + moneyMade.get(f)));
                            }
                        }
                    }
                }
            }
        },0,100);
    }


}
