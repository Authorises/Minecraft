package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathListener implements Listener {

    public static void combatLogDeath(Player player){
        try {
            if (Cavelet.lastDamager.containsKey(player)) {
                Player damager = Cavelet.lastDamager.get(player);
                MProfile mp = Cavelet.cachedMPlayers.get(player.getUniqueId());
                if(Bukkit.getPlayer(damager.getUniqueId())!=null){
                    damager.playSound(damager.getLocation(), Sound.ITEM_TOTEM_USE, SoundCategory.MASTER, 1F, 1F);
                    MProfile ce = Cavelet.cachedMPlayers.get(damager.getUniqueId());
                    ce.setKills(ce.getKills() + 1);
                    ce.setBalance(ce.getBalance()+mp.getBalance());
                    ce.setDarkSouls(ce.getDarkSouls()+mp.getDarkSouls());
                    mp.setBalance(0.0);
                    mp.setDarkSouls(0.0);
                    mp.combatLogDead = true;
                    for(ItemStack item : player.getInventory().getContents()){
                        if(item!=null){
                            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                        }
                    }
                    damager.sendMessage(ColorUtils.format("&fYou killed &b" + damager.getName()));
                    damager.showTitle(Title.title(Component.text(ColorUtils.format("&b&lKILL")), Component.text(ColorUtils.format("&7Killed &b" + player.getName()))));
                }
                mp.setDeaths(mp.getDeaths() + 1);
            } else {

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @EventHandler
    public static void playerDied(PlayerDeathEvent e){
        try {
                e.getPlayer().getKiller().playSound(e.getPlayer().getKiller().getLocation(), Sound.ITEM_TOTEM_USE, SoundCategory.MASTER, 1F, 1F);
                MProfile mp = Cavelet.cachedMPlayers.get(e.getPlayer().getUniqueId());
                mp.setDeaths(mp.getDeaths() + 1);
                if(e.getPlayer().getKiller()!=null){
                    MProfile ce = Cavelet.cachedMPlayers.get(e.getPlayer().getKiller().getUniqueId());
                    ce.setKills(ce.getKills() + 1);
                    ce.setBalance(ce.getBalance()+mp.getBalance());
                    ce.setDarkSouls(ce.getDarkSouls()+mp.getDarkSouls());
                    mp.setBalance(0.0);
                    mp.setDarkSouls(0.0);
                    e.getPlayer().getKiller().sendMessage(ColorUtils.format("&fYou killed &b" + e.getPlayer().getName()));
                    e.getPlayer().getKiller().showTitle(Title.title(Component.text(ColorUtils.format("&b&lKILL")), Component.text(ColorUtils.format("&7Killed &b" + e.getPlayer().getName()))));
                }
                e.getPlayer().sendMessage(ColorUtils.format("&fYou were killed by &b" + e.getPlayer().getKiller().getName()));
                e.getPlayer().showTitle(Title.title(Component.text(ColorUtils.format("&c&lDEAD")), Component.text(ColorUtils.format("&7Killed by &b" + e.getPlayer().getKiller().getName()))));
                Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getProvidingPlugin(Cavelet.class), new Runnable() {
                    @Override
                    public void run() {
                        e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), 3, 54, -1));
                    }}, 3L);
        }catch (Exception ex){
            ex.printStackTrace();
            MProfile mp = Cavelet.cachedMPlayers.get(e.getPlayer().getUniqueId());
            mp.setDeaths(mp.getDeaths() + 1);
            e.getPlayer().sendMessage(ColorUtils.format("&fYou died"));
            e.getPlayer().showTitle(Title.title(Component.text(ColorUtils.format("&c&lDEAD")), Component.empty()));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getProvidingPlugin(Cavelet.class), new Runnable() {
                @Override
                public void run() {
                    e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), 3, 54, -1));
                }
            }, 3L);
        }
    }

}
