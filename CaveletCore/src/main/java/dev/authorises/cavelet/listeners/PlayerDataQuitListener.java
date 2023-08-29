package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.scoreboard.MainScoreboard;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerDataQuitListener implements Listener {

    @EventHandler
    public void playerQuitListener(PlayerQuitEvent e) throws IOException {
        e.quitMessage(Component.empty());
        MProfile mp = Cavelet.cachedMPlayers.get(e.getPlayer().getUniqueId());
        if(mp.combatLogLeft!=-1){
            PlayerDeathListener.combatLogDeath(e.getPlayer());
        }

        if(Cavelet.factionManager.playersFactions.containsKey(e.getPlayer().getUniqueId())) {
            MFaction faction = Cavelet.factionManager.playersFactions.get(e.getPlayer().getUniqueId());
            faction.getOnlinePlayers().forEach((p) -> {
                if(p.getUniqueId()!=e.getPlayer().getUniqueId()){
                    p.sendMessage(Cavelet.miniMessage.deserialize("<red>Faction Member offline: <white>"+e.getPlayer().getName()));
                }
            });
        }

        CompletableFuture.runAsync(() -> {
            Cavelet.cachedMPlayers.remove(e.getPlayer().getUniqueId());
            mp.save(e.getPlayer());
            MainScoreboard.playerLeave(e.getPlayer());
        }).thenRun(() -> {
            Cavelet.chatManager.playerQuits(e.getPlayer());
        });

    }


}
