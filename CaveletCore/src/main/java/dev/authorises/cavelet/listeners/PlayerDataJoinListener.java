package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.scoreboard.MainScoreboard;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class PlayerDataJoinListener implements Listener {

    @EventHandler
    public void playerJoinListener(PlayerJoinEvent e) throws Exception {
        Player p = e.getPlayer();
        e.joinMessage(Component.empty());

        if(Cavelet.factionManager.playersFactions.containsKey(p.getUniqueId())) {
            MFaction faction = Cavelet.factionManager.playersFactions.get(p.getUniqueId());
            faction.getOnlinePlayers().forEach((player) -> {
                if(player.getUniqueId()!=p.getUniqueId()){
                    player.sendMessage(Cavelet.miniMessage.deserialize("<green>Faction Member online <white>"+p.getName()));
                }
            });
        }

        try {
            MProfile mp = new MProfile(p);
            mp.loadInventory(p);
            mp.combatLogLeft = -1;
            Cavelet.cachedMPlayers.put(p.getUniqueId(), mp);
            MainScoreboard.playerJoin(p);

            if(Cavelet.factionManager.playersFactions.containsKey(p.getUniqueId())){
                Cavelet.factionManager.playersFactions.get(p.getUniqueId()).lastUsernames.put(p.getUniqueId(), p.getName());
            }

            Cavelet.chatManager.playerJoins(e.getPlayer());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
