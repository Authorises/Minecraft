package dev.authorises.vortechfactions.commands;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.getFactionDisplay;
import dev.authorises.vortechfactions.utilities.homeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class homeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){

            Player p = (Player) sender;

            if(VortechFactions.combatLogger.attemptRestricted(p)) {
                if (!(homeUtils.hasHome(p))) {
                    p.sendMessage(ColorUtils.format("&f(&b!&f) You currently &cdon't&f have a home. You can set one with /sethome."));
                    return true;
                }
                Location home = homeUtils.getHome(p);
                if (home == null) {
                    p.sendMessage(ColorUtils.format("&f(&b!&f) You currently &cdon't&f have a home. You can set one with /sethome. &7(Error)"));
                    return true;
                }
                Faction factionat = VortechFactions.api.getFactionAt(home.getChunk());
                if (factionat.isWilderness() || factionat == VortechFactions.api.getFaction(p)) {
                    Location l = p.getLocation();
                    p.sendMessage(ColorUtils.format("&f(&b!&f) Teleporting you to &bhome&f in &b5s.&7 Don't move."));
                    p.sendTitle(ColorUtils.format("&b&nTeleporting to home"), ColorUtils.format("&7You will be teleported in 5s"));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                        @Override
                        public void run() {
                            p.sendTitle(ColorUtils.format("&b&nTeleporting to home"), ColorUtils.format("&7You will be teleported in 4s"));
                            Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                                @Override
                                public void run() {
                                    p.sendTitle(ColorUtils.format("&b&nTeleporting to home"), ColorUtils.format("&7You will be teleported in 3s"));
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                                        @Override
                                        public void run() {
                                            p.sendTitle(ColorUtils.format("&b&nTeleporting to home"), ColorUtils.format("&7You will be teleported in 2s"));
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                                                @Override
                                                public void run() {
                                                    p.sendTitle(ColorUtils.format("&b&nTeleporting to home"), ColorUtils.format("&7You will be teleported in 1s"));
                                                    Bukkit.getScheduler().scheduleSyncDelayedTask(VortechFactions.getProvidingPlugin(VortechFactions.class), new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            p.sendTitle(ColorUtils.format("&b&nTeleporting to home"), ColorUtils.format("&aTeleporting"));
                                                            if (p.getLocation().equals(l)) {
                                                                p.teleport(home);
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
                } else {
                    p.sendMessage(ColorUtils.format("&cError!&f You cannot set your home in " + getFactionDisplay.get(p, VortechFactions.api.getFactionAt(home.getChunk()))));
                }
            }
        }
        return true;
    }
}
