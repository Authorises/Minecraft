package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.exceptions.PlayerNotFoundException;
import dev.authorises.cavelet.factions.*;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.playerdata.MProfileManager;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.FactionUtil;
import dev.authorises.cavelet.utils.NumberFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FactionCommand implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(args.length>=1){
                switch (args[0]){
                    case "help":{
                        player.sendMessage(ColorUtils.format("" +
                                "&7Factions commands:" +
                                "\n&a/f help &7- Shows all faction commands" +
                                "\n&a/f top &7- Shows the factions leaderboard" +
                                "\n&a/f show &7- Shows information about factions" +
                                "\n&a/f create &7- Creates a new faction" +
                                "\n&a/f invite &7- Invites members to your faction" +
                                "\n&a/f join &7 - Join a faction you have an invite to" +
                                "\n&a/f kick &7- Kick out a member from your faction" +
                                "\n&a/f leave &7- Leave your current faction" +
                                "\n&a/f promote &7- Promote a player in your faction" +
                                "\n&a/f demote &7- Demote a player in your faction" +
                                ""));
                        break;
                    }
                    case "top":{
                        player.performCommand("ftop");
                        break;
                    }
                    case "show":{

                        if(args.length>=2){

                            MFaction targetFaction = Cavelet.factionManager.getFactionByName(args[1]);
                            if(targetFaction!=null){
                                FactionUtil.showFactionText(player, targetFaction);
                            }else{
                                player.sendMessage(ColorUtils.format("&cA faction could not be found with that name. To find out what faction a player is in use /view <username>"));
                            }
                        }else{
                            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                                FactionUtil.showFactionText(player, Cavelet.factionManager.playersFactions.get(player.getUniqueId()));
                            }else{
                                player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                            }
                        }

                        break;
                    }
                    case "invite":{
                        if(args.length>=2){
                            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                                MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                                if(faction.getMembers().get(player.getUniqueId()).weight>= FactionRank.OFFICER.weight){
                                    Player target = Bukkit.getPlayer(args[1]);
                                    if(target!=null){
                                        faction.getInvitedPlayers().add(target.getUniqueId());
                                        player.sendMessage(ColorUtils.format("&7Invite sent &asuccessfully"));
                                        target.sendMessage(ColorUtils.format("&7You have been invited to the faction &a"+faction.getName()+"&7, to accept the invite use &a/f join "+faction.getName()));
                                    }else{
                                        player.sendMessage(ColorUtils.format("&cA player was not found with that username"));
                                    }
                                }else{
                                    player.sendMessage(ColorUtils.format("&cYou must be an Officer to manage members"));
                                }
                            }else{
                                player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                            }
                        }else{
                            player.sendMessage(ColorUtils.format("&cCommand usage: /f invite <username>"));
                        }

                        break;
                    }
                    case "join":{
                        if(args.length>=2){
                            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                                player.sendMessage(ColorUtils.format("&cYou are already in a faction. You can leave with &a/f leave"));
                            }else{
                                MFaction faction = Cavelet.factionManager.getFactionByName(args[1]);
                                if(faction!=null){
                                    if(faction.getInvitedPlayers().contains(player.getUniqueId())){
                                        faction.getInvitedPlayers().remove(player.getUniqueId());
                                        faction.getMembers().put(player.getUniqueId(), FactionRank.RECRUIT);
                                        faction.lastUsernames.put(player.getUniqueId(), player.getName());
                                        faction.updateData();
                                        Cavelet.factionManager.playersFactions.put(player.getUniqueId(), faction);
                                        faction.getOnlinePlayers().forEach(p -> {
                                            p.sendMessage(ColorUtils.format("&7A new player has joined the faction: &a"+player.getName()));
                                        });
                                        player.sendMessage(ColorUtils.format("&7You have joined the faction &a"+faction.getName()));
                                    }else{
                                        player.sendMessage(ColorUtils.format("&cYou do not have an invite to that faction."));
                                    }
                                }else{
                                    player.sendMessage(ColorUtils.format("&cA faction was not found with that name"));
                                }
                            }
                        }else{
                            player.sendMessage(ColorUtils.format("&cCommand usage: /f join <faction>"));
                        }
                        break;
                    }
                    case "create":{
                        if(args.length>=2){
                            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                                player.sendMessage(ColorUtils.format("&cYou are already in a faction. You can leave with &a/f leave"));
                            }else {
                                String name = args[1];
                                if(name.length()<2 || name.length()>10 && StringUtils.isAlphanumeric(name)){
                                    player.sendMessage(ColorUtils.format("&cA faction name must be between 2 and 10 characters long and only contain letters and numbers."));
                                }else{
                                    if(Cavelet.factionManager.getFactionByName(name)==null) {
                                        MFaction newFaction = new MFaction(name, player);
                                        Cavelet.factionManager.loadedFactions.put(newFaction.getId(), newFaction);
                                        Cavelet.factionManager.playersFactions.put(player.getUniqueId(), newFaction);
                                        player.sendMessage(ColorUtils.format("&7Your faction has been created. Invite other players with &a/f invite <username>"));
                                    }else{
                                        player.sendMessage(ColorUtils.format("&cA faction with that name already exists."));
                                    }
                                }
                            }
                        }else{
                            player.sendMessage(ColorUtils.format("&cCommand usage: /f create <name>"));
                        }
                        break;
                    }
                    case "leave":{
                        if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                            MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                            if(!(faction.getOwner().equals(player.getUniqueId()))){
                                faction.getMembers().remove(player.getUniqueId());
                                faction.lastUsernames.remove(player.getUniqueId());
                                faction.updateData();
                                Cavelet.factionManager.playersFactions.remove(player.getUniqueId());
                                player.sendMessage(ColorUtils.format("&7You have left the faction &a"+faction.getName()));
                                faction.getOnlinePlayers().forEach(p->{
                                    p.sendMessage(ColorUtils.format("&7Player &a"+player.getName()+"&7 has left the faction"));
                                });
                            }else{
                                player.sendMessage(ColorUtils.format("&cYou can not leave as the owner. Give someone else ownership with &a/f owner <username>&c or disband the faction &a/f disband"));
                            }
                        }else{
                            player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                        }
                        break;
                    }
                    case "description":{
                        if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
                            if (args.length >= 2) {
                                String[] words = args.clone();
                                words[0]="";
                                StringBuilder description = new StringBuilder();
                                for (String word : words) {
                                    description.append(word).append(" ");
                                }
                                if (description.length() <= 80) {
                                    MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                                    if(faction.getMembers().get(player.getUniqueId()).weight>= FactionRank.TRUSTED.weight){
                                        faction.setDescription(Cavelet.miniMessage.stripTags(description.toString()));
                                        faction.updateData();
                                        faction.getOnlinePlayers().forEach(p->{
                                            p.sendMessage(ColorUtils.format("&7Player &a"+player.getName()+"&7 has changed the faction description to: &a&o"+faction.getDescription()));
                                        });
                                    }
                                } else {
                                    player.sendMessage(ColorUtils.format("&cFaction descriptions cannot be longer than 80 characters."));
                                }
                            } else {
                                player.sendMessage(ColorUtils.format("&cCommand usage: /f description <description>"));
                            }
                        }else{
                            player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                        }
                        break;
                    }

                    case "demote":{
                        if(args.length>=2) {
                            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
                                MFaction fac = Cavelet.factionManager.playersFactions.get(player.getUniqueId());

                                boolean found = false;
                                UUID targetId = null;
                                String targetName = null;

                                for (UUID id : fac.lastUsernames.keySet()) {
                                    String us = fac.lastUsernames.get(id);
                                    if (us.equalsIgnoreCase(args[1])) {
                                        found = true;
                                        targetId = id;
                                        targetName = us;
                                    }
                                }
                                if (found) {
                                    if(targetId!=player.getUniqueId()) {
                                        if (fac.getMembers().get(player.getUniqueId()).weight > fac.getMembers().get(targetId).weight) {
                                            FactionRank oldrank = fac.getMembers().get(targetId);
                                            if(oldrank==FactionRank.RECRUIT){
                                                player.sendMessage(ColorUtils.format("&cThat player can not be demoted anymore! They are already the lowest rank."));
                                            }else{
                                                FactionRank newRank = FactionRankUtil.fromWeight(oldrank.weight-1);
                                                String name = fac.lastUsernames.get(targetId);
                                                if(newRank!=null) {
                                                    fac.getMembers().put(targetId, newRank);
                                                    fac.getOnlinePlayers().forEach((facMember) -> {
                                                        facMember.sendMessage(Cavelet.miniMessage.deserialize("<gray>Player <green>" + name + "<gray> has been demoted to<" + newRank.colour + "> " + newRank.display));
                                                    });
                                                }else {
                                                    player.sendMessage(ColorUtils.format("&cAn error occurred processing your request"));
                                                }
                                            }

                                        }else{
                                            player.sendMessage(ColorUtils.format("&cYou can not demote that player"));
                                        }
                                    }else{
                                        player.sendMessage(ColorUtils.format("&cYou can not demote that player"));
                                    }
                                }else{
                                    player.sendMessage(ColorUtils.format("&cThat player is not in your faction"));
                                }
                            }else{
                                player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                            }
                        }else{
                            player.sendMessage(ColorUtils.format("&cCommand usage: /f demote <player>"));
                        }
                        break;
                    }
                    case "promote":{
                        if(args.length>=2) {
                            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
                                MFaction fac = Cavelet.factionManager.playersFactions.get(player.getUniqueId());

                                boolean found = false;
                                UUID targetId = null;
                                String targetName = null;

                                for (UUID id : fac.lastUsernames.keySet()) {
                                    String us = fac.lastUsernames.get(id);
                                    if (us.equalsIgnoreCase(args[1])) {
                                        found = true;
                                        targetId = id;
                                        targetName = us;
                                    }
                                }
                                if (found) {
                                    if(targetId!=player.getUniqueId()) {
                                        if (fac.getMembers().get(player.getUniqueId()).weight > fac.getMembers().get(targetId).weight) {
                                            FactionRank oldrank = fac.getMembers().get(targetId);
                                                FactionRank newRank = FactionRankUtil.fromWeight(oldrank.weight+1);
                                                if (fac.getMembers().get(player.getUniqueId()).weight > newRank.weight) {
                                                    String name = fac.lastUsernames.get(targetId);
                                                    if (newRank != null) {
                                                        fac.getMembers().put(targetId, newRank);
                                                        fac.getOnlinePlayers().forEach((facMember) -> {
                                                            facMember.sendMessage(Cavelet.miniMessage.deserialize("<gray>Player <green>" + name + "<gray> has been promoted to<" + newRank.colour + "> " + newRank.display));
                                                        });
                                                    } else {
                                                        player.sendMessage(ColorUtils.format("&cAn error occurred processing your request"));
                                                    }
                                                }else {
                                                    player.sendMessage(ColorUtils.format("&cYou cannot promote a player to the same rank as you"));
                                                }
                                        }else{
                                            player.sendMessage(ColorUtils.format("&cYou can not promote that player"));
                                        }
                                    }else{
                                        player.sendMessage(ColorUtils.format("&cYou can not promote that player"));
                                    }
                                }else{
                                    player.sendMessage(ColorUtils.format("&cThat player is not in your faction"));
                                }
                            }else{
                                player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                            }
                        }else{
                            player.sendMessage(ColorUtils.format("&cCommand usage: /f kick <player>"));
                        }
                        break;
                    }
                    case "kick":{
                        if(args.length>=2){
                                if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                                    MFaction fac = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                                    
                                    boolean found = false;
                                    UUID targetId = null;
                                    String targetName = null;
                                    
                                    for(UUID id : fac.lastUsernames.keySet()){
                                        String us = fac.lastUsernames.get(id);
                                        if(us.equalsIgnoreCase(args[1])){
                                            found = true;
                                            targetId = id;
                                            targetName = us;
                                        }
                                    }
                                    
                                    if(found){
                                        if(targetId!=player.getUniqueId()){
                                            if(fac.getMembers().get(player.getUniqueId()).weight>fac.getMembers().get(targetId).weight){
                                                if(Bukkit.getPlayer(targetId)!=null){
                                                    Bukkit.getPlayer(targetId).sendMessage(ColorUtils.format("&7You have been kicked from &a"+fac.getName()+"&7 by &a"+player.getName()));
                                                }
                                                fac.getMembers().remove(targetId);
                                                fac.lastUsernames.remove(targetId);
                                                Cavelet.factionManager.playersFactions.remove(targetId);
                                                fac.updateData();
                                                String finalTargetName = targetName;
                                                fac.getOnlinePlayers().forEach(p -> {
                                                    p.sendMessage(ColorUtils.format("&7Player &a"+player.getName()+"&7 has kicked player &a"+ finalTargetName +"&7 from the faction"));
                                                });
                                            }else{
                                                player.sendMessage(ColorUtils.format("&cYou can not kick that player from your faction"));
                                            }
                                        }else{
                                            player.sendMessage(ColorUtils.format("&cYou can not kick that player from your faction"));
                                        }
                                    }else{
                                        player.sendMessage(ColorUtils.format("&cThat player is not in your faction"));
                                    }
                                }else{
                                    player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                                }
                        }else{
                            player.sendMessage(ColorUtils.format("&cCommand usage: /f kick <player>"));
                        }
                        break;
                    }
                    case "forcerecalc":{
                        if(player.hasPermission("faction.forcerecalculate")){
                            Cavelet.factionManager.factionTop.calculateWorth();
                        }
                        break;
                    }
                    case "debugftop":{
                        if(player.hasPermission("faction.debugftop")){
                            Cavelet.factionManager.factionTop.leaderBoard.clear();
                            for(int i = 0;i<25;i++){
                                MFaction fac = new MFaction();
                                fac.lastUpdatedPoints = new Random().nextInt(2550000);
                                Cavelet.factionManager.factionTop.leaderBoard.add(fac);
                            }
                            player.sendMessage("Done");
                            //Cavelet.factionManager.factionTop.calculateWorth();
                        }
                        break;
                    }
                }
            }else{
                player.performCommand("f help");
            }

        }else{
            Bukkit.getLogger().severe("Only players can execute the faction command");
        }
        return true;
    }

}
