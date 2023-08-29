package dev.authorises.cavelet.eventsystem.events.cthulu;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.eventsystem.Boosters;
import dev.authorises.cavelet.eventsystem.Event;
import dev.authorises.cavelet.playerdata.MProfile;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class DoubleMiningXPEvent extends Event {

    private int secondsLeft;
    private int taskId;
    private BossBar bossBar;

    public DoubleMiningXPEvent() {
        super("Double Mining XP", Rarity.UNCOMMON, "DOUBLE_MINING_XP");
    }

    @Override
    public void start(){

        Component c = Cavelet.miniMessage.deserialize("<#19b6e6>Event! Double Mining XP for 10 minutes.");

        Boosters.miningBooster = 2.0;
        this.secondsLeft = 600;

        this.bossBar = BossBar.bossBar(Cavelet.miniMessage.deserialize("<aqua>Double Mining XP"), 1F, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), () -> {
                secondsLeft-=1;
                if(secondsLeft<=0){
                    this.end(false);
                }

                int mins = Double.valueOf(Math.floor(secondsLeft/60)).intValue();
                int seconds = secondsLeft-(mins*60);
            String secs = String.valueOf(seconds).length()==1?"0"+seconds:""+seconds;
            Float f = Double.valueOf(((double) secondsLeft / 600)).floatValue();
            this.bossBar.progress(f);
            this.bossBar.name(Cavelet.miniMessage.deserialize("<aqua>Double Mining XP ("+mins+":"+secs+")"));
                Bukkit.getOnlinePlayers().forEach((p) -> {
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    if (mp.getCurrentBossBar() != this.bossBar) {
                        mp.setCurrentBossBar(this.bossBar);
                    } else {
                        mp.showBossBar(p, this.bossBar);
                    }
                });

        }, 0L, 20L);


        final Title title = Title.title(Cavelet.miniMessage.deserialize("<aqua><bold>Double Mining XP"), Cavelet.miniMessage.deserialize("<#9eb5db>All players will gain <aqua>2x Mining XP<#9eb5db>."));

        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage(c);
            p.showTitle(title);
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1F, 1F);
        });

    };

    @Override
    public void end(boolean forced){
        Bukkit.getOnlinePlayers().forEach((p) -> {
            Component m = Cavelet.miniMessage.deserialize("<#19b6e6>Even ended! Double Mining XP event has ended.");
            MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
            if(mp.getCurrentBossBar()==this.bossBar){
                mp.setCurrentBossBar(null);
                p.hideBossBar(this.bossBar);
            }
            p.sendMessage(m);
        });
        Cavelet.eventManager.clearCurrentEvent();
        Boosters.miningBooster = 1.0;
        Bukkit.getScheduler().cancelTask(taskId);
    };

    @Override
    public void showEventInfo(Player player){
        Component c = Cavelet.miniMessage.deserialize("<#9eb5db>Current event: Double Mining XP (All players have a 2x mining XP multiplier)");
        player.sendMessage(c);
    }
}
