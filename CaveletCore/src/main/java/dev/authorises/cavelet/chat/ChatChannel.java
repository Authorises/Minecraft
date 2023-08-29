package dev.authorises.cavelet.chat;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.FactionRank;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.function.Function;
import java.util.function.Predicate;

public enum ChatChannel {
    PUBLIC(
            "Public",
            "<#5894f5>",
            Material.GLASS,
            (player) -> "",
            (player) -> true),
    FACTION(
            "Faction",
            "<green>",
            Material.IRON_INGOT,
            (player) -> "<green>[FACTION] ",
            (player) -> Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())),
    TRUSTED(
            "Trusted",
            "<#2a9d8f>",
            Material.GOLD_INGOT,
            (player) -> "<#2a9d8f>[TRUSTED] ",
            (player) -> Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId()) && Cavelet.factionManager.playersFactions.get(player.getUniqueId()).getMembers().get(player.getUniqueId()).weight >= FactionRank.TRUSTED.weight),
    OFFICER(
            "Officer",
            "<#f4a261>",
            Material.DIAMOND,
            (player) -> "<#f4a261>[OFFICER] ",
            (player) -> Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId()) && Cavelet.factionManager.playersFactions.get(player.getUniqueId()).getMembers().get(player.getUniqueId()).weight >= FactionRank.OFFICER.weight),
    STAFF(
            "Staff",
            "<#FF69B4>",
            Material.BEACON,
            (player) -> "<#FF69B4>[STAFF] ",
            (player) -> player.hasPermission("staff"));

    public final String name;
    public final String colour;
    public final Material item;
    public final Predicate<Player> hasAccess;
    public final Function<Player, String> getMessagePrefix;

    ChatChannel(String name, String colour, Material item, Function<Player, String> getMessagePrefix, Predicate<Player> hasAccess){
        this.name = name;
        this.colour = colour;
        this.item = item;
        this.getMessagePrefix = getMessagePrefix;
        this.hasAccess = hasAccess;
    }
}
