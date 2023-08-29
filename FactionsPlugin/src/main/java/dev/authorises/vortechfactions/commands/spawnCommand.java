package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class spawnCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;

            if(VortechFactions.combatLogger.attemptRestricted(p)) {
                Location l = p.getLocation();
                p.sendMessage(ColorUtils.format("&f(&b!&f) Teleporting you to &aspawn&f in &b5s.&7 Don't move."));
                p.sendTitle(ColorUtils.format("&b&nTeleporting to spawn"), ColorUtils.format("&7You will be teleported in 5s"));
                Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                    @Override
                    public void run() {
                        p.sendTitle(ColorUtils.format("&b&nTeleporting to spawn"), ColorUtils.format("&7You will be teleported in 4s"));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                            @Override
                            public void run() {
                                p.sendTitle(ColorUtils.format("&b&nTeleporting to spawn"), ColorUtils.format("&7You will be teleported in 3s"));
                                Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                                    @Override
                                    public void run() {
                                        p.sendTitle(ColorUtils.format("&b&nTeleporting to spawn"), ColorUtils.format("&7You will be teleported in 2s"));
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                                            @Override
                                            public void run() {
                                                p.sendTitle(ColorUtils.format("&b&nTeleporting to spawn"), ColorUtils.format("&7You will be teleported in 1s"));
                                                Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        p.sendTitle(ColorUtils.format("&b&nTeleporting to spawn"), ColorUtils.format("&aTeleporting"));
                                                        if (p.getLocation().equals(l)) {
                                                            p.teleport(new Location(Bukkit.getWorlds().get(0), -1, 88, -1));
                                                        } else {
                                                            p.sendMessage(ColorUtils.format("&cTeleport Failed.&7 You moved!"));
                                                            p.sendTitle(ColorUtils.format("&c&nTeleport Failed"), ColorUtils.format("&7You moved."));
                                                        }
                                                    }
                                                }, 20L);
                                            }
                                        }, 20L);
                                    }
                                }, 20L);
                            }
                        }, 20L);
                    }
                }, 20L);
            }
        }
        return true;
    }

}
