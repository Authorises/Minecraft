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

public class TripleFarmingXPEvent extends Event {

    private int secondsLeft;
    private int taskId;
    private BossBar bossBar;

    public TripleFarmingXPEvent() {
        super("Triple Farming XP", Rarity.UNCOMMON, "TRIPLE_FARMING_XP");
    }

    @Override
    public void start(){

        Component c = Cavelet.miniMessage.deserialize("<#19b6e6>Event! Triple Farming XP for 5 minutes.");

        Boosters.farmingBooster = 3.0;
        this.secondsLeft = 300;

        this.bossBar = BossBar.bossBar(Cavelet.miniMessage.deserialize("<yellow>Triple Farming XP"), 1F, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), () -> {
                secondsLeft-=1;
                if(secondsLeft<=0){
                    this.end(false);
                }

                int mins = Double.valueOf(Math.floor(secondsLeft/60)).intValue();
                int seconds = secondsLeft-(mins*60);
            String secs = String.valueOf(seconds).length()==1?"0"+seconds:""+seconds;
            Float f = Double.valueOf(((double) secondsLeft / 300)).floatValue();
            this.bossBar.progress(f);
            this.bossBar.name(Cavelet.miniMessage.deserialize("<yellow>Triple Farming XP ("+mins+":"+secs+")"));
                Bukkit.getOnlinePlayers().forEach((p) -> {
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    if (mp.getCurrentBossBar() != this.bossBar) {
                        mp.setCurrentBossBar(this.bossBar);
                    } else {
                        mp.showBossBar(p, this.bossBar);
                    }
                });

        }, 0L, 20L);



        final Title title = Title.title(Cavelet.miniMessage.deserialize("<yellow><bold>Triple Farming XP"), Cavelet.miniMessage.deserialize("<#9eb5db>All players will gain <yellow>3x Farming XP<#9eb5db>."));

        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage(c);
            p.showTitle(title);
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1F, 1F);
        });

    };

    @Override
    public void end(boolean forced){
        Bukkit.getOnlinePlayers().forEach((p) -> {
            Component m = Cavelet.miniMessage.deserialize("<#19b6e6>Even ended! Triple Farming XP event has ended.");
            MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
            if(mp.getCurrentBossBar()==this.bossBar){
                mp.setCurrentBossBar(null);
                p.hideBossBar(this.bossBar);
            }
            p.sendMessage(m);
        });
        Cavelet.eventManager.clearCurrentEvent();
        Boosters.farmingBooster = 1.0;
        Bukkit.getScheduler().cancelTask(taskId);
    };

    @Override
    public void showEventInfo(Player player){
        Component c = Cavelet.miniMessage.deserialize("<#9eb5db>Current event: Triple Farming XP (All players have a 3x Farming XP multiplier)");
        player.sendMessage(c);
    }
}
