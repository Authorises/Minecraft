package dev.authorises.vortechcore.events;

import dev.authorises.vortechcore.VortechCore;
import dev.authorises.vortechcore.scoreboard.lobbyMainScoreboard;
import dev.authorises.vortechcore.utilities.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Duration;

public class playerJoin implements Listener {

    @EventHandler
    public void v(PlayerJoinEvent e) throws Exception {
        Player p = e.getPlayer();
        User user = VortechCore.luckPerms.getPlayerAdapter(Player.class).getUser(p);
        VortechCore.displayNames.put(p.getUniqueId(), ColorUtils.format(user.getCachedData().getMetaData().getPrefix()+p.getName()));
        p.setDisplayName(ColorUtils.format(user.getCachedData().getMetaData().getPrefix()+p.getName()));
        p.showTitle(Title.title(Component.text(ColorUtils.format("")), Component.text(ColorUtils.format("&bJoined Server"))));
        if(VortechCore.sb!=null) {
            lobbyMainScoreboard.playerJoin(e.getPlayer());
        }
    }
}
