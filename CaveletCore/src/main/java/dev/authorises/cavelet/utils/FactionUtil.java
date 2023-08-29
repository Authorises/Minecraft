package dev.authorises.cavelet.utils;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.FactionTop;
import dev.authorises.cavelet.factions.MFaction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class FactionUtil {
    public static void showFactionText(Player p, MFaction faction){

        long ago = System.currentTimeMillis()-faction.getDateCreated();

        List<UUID> online = faction.getOnlinePlayers().stream().map((Player::getUniqueId)).toList();

        TextComponent members = Component.text("");

        for(UUID uuid : faction.getMembers().keySet()){
            if (online.contains(uuid)) {
                if(uuid == p.getUniqueId()){
                    members = members.append(Cavelet.miniMessage.deserialize("<bold><green>"+faction.lastUsernames.get(uuid)+"<#9eb5db> <reset>")
                            .hoverEvent(
                                    HoverEvent.showText(Cavelet.miniMessage.deserialize("<bold><green>"+faction.lastUsernames.get(uuid)+"<reset>\n" +
                                            "<" + faction.getMembers().get(uuid).colour+">"+faction.getMembers().get(uuid).display + "<reset>\n" +
                                            "\n" +
                                            "<#9eb5db>Click to view player information")
                                    ))       .clickEvent(
                                    ClickEvent.runCommand("/view "+faction.lastUsernames.get(uuid))
                            )
                    );
                }else{
                    members = members.append(Cavelet.miniMessage.deserialize("<green>"+faction.lastUsernames.get(uuid)+"<#9eb5db> <reset>")
                            .hoverEvent(
                                    HoverEvent.showText(Cavelet.miniMessage.deserialize("<green>"+faction.lastUsernames.get(uuid)+"<reset>\n" +
                                            "<" + faction.getMembers().get(uuid).colour+">"+faction.getMembers().get(uuid).display + "<reset>\n" +
                                            "\n" +
                                            "<#9eb5db>Click to view player information")
                                    ))       .clickEvent(
                                    ClickEvent.runCommand("/view "+faction.lastUsernames.get(uuid))
                            )
                    );
                }
            }else{
                members = members.append(Cavelet.miniMessage.deserialize("<gray>"+faction.lastUsernames.get(uuid)+"<#9eb5db> <reset>")
                        .hoverEvent(
                                HoverEvent.showText(Cavelet.miniMessage.deserialize("<gray>"+faction.lastUsernames.get(uuid)+"<reset>\n" +
                                        "<" + faction.getMembers().get(uuid).colour+">"+faction.getMembers().get(uuid).display + "<reset>\n" +
                                        "\n" +
                                        "<#9eb5db>Click to view player information")
                                ))       .clickEvent(
                                ClickEvent.runCommand("/view "+faction.lastUsernames.get(uuid))
                        )
                );
            }
        }



        Integer rank = Cavelet.factionManager.factionTop.leaderBoard.contains(faction)?Cavelet.factionManager.factionTop.leaderBoard.indexOf(faction)+1:-1;

        TextComponent message = Component.text("").append(Cavelet.miniMessage.deserialize("<#9eb5db>-------------------------------------\n" +
                "<reset><#9eb5db>Faction: <green>" + faction.getName()+"\n"+
                "<reset><#9eb5db>Created: <green>" + NumberFormat.getDurationBreakdown(ago)+" ago\n"+
                (rank!=-1?"<reset><#9eb5db>Leaderboard Rank: "+ FactionTop.getColorFor(rank)+"#"+rank +" <#9eb5db>("+String.format("%,.0f", faction.lastUpdatedPoints.doubleValue())+" points)\n":"")+
                "<reset><#9eb5db>Description: <green><italic>" + faction.getDescription()+"\n"+
                "<reset><#9eb5db>Members: <reset>").append(members).append(
                Cavelet.miniMessage.deserialize("\n<#9eb5db>-------------------------------------"))) ;
        p.sendMessage(message);
    }
}
