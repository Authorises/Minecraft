package dev.authorises.vortechcore.events;

import dev.authorises.vortechcore.VortechCore;
import dev.authorises.vortechcore.utilities.ColorUtils;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatEvent implements Listener {

    @EventHandler
    private void c(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);
        User user = VortechCore.luckPerms.getPlayerAdapter(Player.class).getUser(p);
        String suffix = user.getCachedData().getMetaData().getSuffix();
        Bukkit.getLogger().info(p.getName() + " ( " + p.getUniqueId() + " ), chat: '" + e.getMessage() + "'");
        for (Player lp : Bukkit.getOnlinePlayers()) {
            lp.sendMessage(ColorUtils.format(VortechCore.displayNames.get(p.getUniqueId()) +  ": " + suffix + e.getMessage()));
        }
    }
}