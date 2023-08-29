package dev.authorises.cavelet.command.acf;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.FactionRank;
import dev.authorises.cavelet.factions.FactionRankUtil;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.FactionUtil;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@CommandAlias("f|fac|faction")
public class NewFactionCommand extends BaseCommand {

    @Default
    @Subcommand("help")
    @Description("Show available factions commands")
    public void showHelp(Player player){
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
                "\n&a/f disband &7- Delete your faction" +
                ""));
    }

    @Subcommand("top|leaderboard")
    @Description("Show the factions leaderboard")
    public void showLeaderboard(Player player){
        player.performCommand("ftop");
    }

    @CommandCompletion("@factions")
    @Subcommand("show|info|who")
    @Description("Show information about a specific faction")
    public void showFaction(Player player, @Single @Optional String faction){
        if(faction!=null){

            MFaction targetFaction = Cavelet.factionManager.getFactionByName(faction);
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
    }

    @Subcommand("invite|add")
    @CommandCompletion("@players")
    @Description("Invite a player to join your faction")
    public void sendInvite(Player player, OnlinePlayer target){
            if(target!=null){
                if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                    MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                    if(faction.getMembers().get(player.getUniqueId()).weight>= FactionRank.OFFICER.weight){
                        if(faction.getMembers().size()<=10){
                            Player targetPlayer = target.getPlayer();
                            faction.getInvitedPlayers().add(targetPlayer.getUniqueId());
                            player.sendMessage(ColorUtils.format("&7Invite sent &asuccessfully"));
                            targetPlayer.sendMessage(ColorUtils.format("&7You have been invited to the faction &a"+faction.getName()+"&7, to accept the invite use &a/f join "+faction.getName()));
                        }else{
                            player.sendMessage(ColorUtils.format("&cFactions have a maximum of 10 members."));
                        }
                    }else{
                        player.sendMessage(ColorUtils.format("&cYou must be an Officer to manage members"));
                    }

                }else{
                    player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
                }
            }else{
                player.sendMessage(ColorUtils.format("&cCould not find that player. Command usage: /f invite <player username>"));
            }
    };

    @CommandCompletion("@factions")
    @Subcommand("join|accept|acceptinvite")
    @Description("Accept an invite to join a faction")
    public void acceptInvite(Player player, @Single String factionName) {
        if(factionName!=null) {
            if (Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
                player.sendMessage(ColorUtils.format("&cYou are already in a faction. You can leave with &a/f leave"));
            } else {
                MFaction faction = Cavelet.factionManager.getFactionByName(factionName);
                if (faction != null) {
                    if (faction.getInvitedPlayers().contains(player.getUniqueId())) {
                        faction.getInvitedPlayers().remove(player.getUniqueId());
                        faction.getMembers().put(player.getUniqueId(), FactionRank.RECRUIT);
                        faction.lastUsernames.put(player.getUniqueId(), player.getName());
                        faction.updateData();
                        Cavelet.factionManager.factionNames.add(faction.getName());
                        Cavelet.factionManager.playersFactions.put(player.getUniqueId(), faction);
                        faction.getOnlinePlayers().forEach(p -> {
                            p.sendMessage(ColorUtils.format("&7A new player has joined the faction: &a" + player.getName()));
                        });
                        player.sendMessage(ColorUtils.format("&7You have joined the faction &a" + faction.getName()));
                    } else {
                        player.sendMessage(ColorUtils.format("&cYou do not have an invite to that faction."));
                    }
                } else {
                    player.sendMessage(ColorUtils.format("&cA faction was not found with that name"));
                }
            }
        }else{
            player.sendMessage(ColorUtils.format("&CCommand usage: /f join <faction>"));
        }
    }

    @Subcommand("create|make")
    @Description("Create a new faction")
    public void createFaction(Player player, @Single String name){
        if(name!=null){
            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                player.sendMessage(ColorUtils.format("&cYou are already in a faction. You can leave with &a/f leave"));
            }else {
                if(name.length()<3 || name.length()>10 || !StringUtils.isAlphanumeric(name)){
                    player.sendMessage(ColorUtils.format("&cA faction name must be between 3 and 10 characters long and only contain letters and numbers."));
                }else{
                    if(Cavelet.factionManager.getFactionByName(name)==null) {
                        MFaction newFaction = new MFaction(name, player);
                        Cavelet.factionManager.factionNames.add(newFaction.getName());
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
    }

    @Subcommand("leave|quit")
    @Description("Leave your current faction")
    public void leaveFaction(Player player){
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
    }

    @Subcommand("description")
    @Description("Set your faction's description.")
    public void setDescription(Player player, String description){
        if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
            if (description!=null) {
                if (description.length() <= 80) {
                    MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                    if(faction.getMembers().get(player.getUniqueId()).weight>= FactionRank.TRUSTED.weight){
                        faction.setDescription(Cavelet.miniMessage.stripTags(description));
                        faction.updateData();
                        faction.getOnlinePlayers().forEach(p->{
                            p.sendMessage(ColorUtils.format("&7Player &a"+player.getName()+"&7 has changed the faction description to: &a&o"+faction.getDescription()));
                        });
                    }else{
                        player.sendMessage(ColorUtils.format("&cYou must be Trusted or above to do that"));
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
    }

    @Subcommand("rename")
    @Description("Change the name of your faction")
    public void setName(Player player, String name){
        if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
            if (name!=null) {
                if(name.length()<3 || name.length()>10 || !StringUtils.isAlphanumeric(name)) {
                    player.sendMessage(ColorUtils.format("&cA faction name must be between 3 and 10 characters long and only contain letters and numbers."));
                }else {
                    if(Cavelet.factionManager.factionNames.contains(name)){
                        player.sendMessage(ColorUtils.format("&cA faction with that name already exists."));
                        return;
                    }
                    MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                    if(faction.getMembers().get(player.getUniqueId()).weight>= FactionRank.OFFICER.weight){
                        faction.setName(name);
                        faction.updateData();
                        faction.getOnlinePlayers().forEach(p->{
                            p.sendMessage(ColorUtils.format("&7Player &a"+player.getName()+"&7 has renamed the faction to: &a"+faction.getName()));
                        });
                    }else{
                        player.sendMessage(ColorUtils.format("&cYou must be Officer or above to do that"));
                    }
                }
            } else {
                player.sendMessage(ColorUtils.format("&cCommand usage: /f rename <name>"));
            }
        }else{
            player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
        }
    }

    @Subcommand("disband")
    @Description("Delete your faction")
    public void disband(Player player){
        if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
                    MFaction faction = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                    if(faction.getMembers().get(player.getUniqueId()).weight>= FactionRank.OWNER.weight){

                        CompletableFuture.runAsync(() -> {
                            Cavelet.factionsData.deleteOne(new Document("_id", faction.getId().toString()));
                        }).thenRun(() -> {
                            Cavelet.factionManager.factionNames.remove(faction.getName());
                            Cavelet.factionManager.loadedFactions.remove(faction.getId());
                        }).thenRun(() -> {
                            faction.getOnlinePlayers().forEach((p) -> {
                                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, SoundCategory.MASTER, 1F, 1F);
                                p.sendMessage(ColorUtils.format("&7You have been removed from &a"+faction.getName()+"&7 as it has been disbanded."));
                                p.sendMessage(ColorUtils.format("&7The disband will be announced in 5 seconds."));
                                Cavelet.factionManager.playersFactions.remove(p.getUniqueId());
                            });
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }).thenRun(() -> {
                            Bukkit.getOnlinePlayers().forEach((p) -> {
                                p.sendMessage(ColorUtils.format("&7Faction &a"+faction.getName()+"&7 has been disbanded."));
                            });
                        });




                    }else{
                        player.sendMessage(ColorUtils.format("&cOnly the owner of a faction can disband it"));
                    }
        }else{
            player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
        }
    }

    @CommandCompletion("@factionmembers")
    @Subcommand("demote")
    @Description("Demote a player in your faction")
    public void demotePlayer(Player player, OfflinePlayer target){
        if(target!=null) {
            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
                MFaction fac = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                if (fac.lastUsernames.containsKey(target.getUniqueId())) {
                    if(target.getUniqueId()!=player.getUniqueId()) {
                        if (fac.getMembers().get(player.getUniqueId()).weight > fac.getMembers().get(target.getUniqueId()).weight) {
                            FactionRank oldrank = fac.getMembers().get(target.getUniqueId());
                            if(oldrank==FactionRank.RECRUIT){
                                player.sendMessage(ColorUtils.format("&cThat player can not be demoted anymore! They are already the lowest rank."));
                            }else{
                                FactionRank newRank = FactionRankUtil.fromWeight(oldrank.weight-1);
                                String name = fac.lastUsernames.get(target.getUniqueId());
                                if(newRank!=null) {
                                    fac.getMembers().put(target.getUniqueId(), newRank);
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
            player.sendMessage(ColorUtils.format("&cCould not find that player. Command usage: /f demote <player username>"));
        }
    }

    @CommandCompletion("@factionmembers")
    @Subcommand("promote")
    @Description("Promote a player in your faction")
    public void promotePlayer(Player player, OfflinePlayer target){
        if(target!=null) {
            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())) {
                MFaction fac = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                if (fac.lastUsernames.containsKey(target.getUniqueId())) {
                    if(target.getUniqueId()!=player.getUniqueId()) {
                        if (fac.getMembers().get(player.getUniqueId()).weight > fac.getMembers().get(target.getUniqueId()).weight) {
                            FactionRank oldrank = fac.getMembers().get(target.getUniqueId());
                            FactionRank newRank = FactionRankUtil.fromWeight(oldrank.weight+1);
                            if (fac.getMembers().get(player.getUniqueId()).weight > newRank.weight) {
                                String name = fac.lastUsernames.get(target.getUniqueId());
                                if (newRank != null) {
                                    fac.getMembers().put(target.getUniqueId(), newRank);
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
            player.sendMessage(ColorUtils.format("&&cCould not find that player. Command usage: /f promote <player username>"));
        }
    }

    @CommandCompletion("@factionmembers")
    @Subcommand("kick")
    @Description("Kick a player from your faction")
    public void kickPlayer(Player player, OfflinePlayer target){
        if(target!=null){
            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                MFaction fac = Cavelet.factionManager.playersFactions.get(player.getUniqueId());

                if(fac.lastUsernames.containsKey(target.getUniqueId())){
                    if(target.getUniqueId()!=player.getUniqueId()){
                        if(fac.getMembers().get(player.getUniqueId()).weight>fac.getMembers().get(target.getUniqueId()).weight){
                            if(Bukkit.getPlayer(target.getUniqueId())!=null){
                                Bukkit.getPlayer(target.getUniqueId()).sendMessage(ColorUtils.format("&7You have been kicked from &a"+fac.getName()+"&7 by &a"+player.getName()));
                            }
                            fac.getMembers().remove(target.getUniqueId());
                            fac.lastUsernames.remove(target.getUniqueId());
                            Cavelet.factionManager.playersFactions.remove(target.getUniqueId());
                            fac.updateData();
                            String finalTargetName = target.getName();
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
            player.sendMessage(ColorUtils.format("&cCould not find that player. Command usage: /f kick <player username>"));
        }
    }

    @CommandCompletion("@factionmembers")
    @Subcommand("owner")
    @Description("Transfer ownership of your faction to another player")
    public void setOwner(Player player, OfflinePlayer target){
        if(target!=null){
            if(Cavelet.factionManager.playersFactions.containsKey(player.getUniqueId())){
                MFaction fac = Cavelet.factionManager.playersFactions.get(player.getUniqueId());
                if(fac.lastUsernames.containsKey(target.getUniqueId())){
                    if(target.getUniqueId()!=player.getUniqueId()){
                        if(fac.getMembers().get(player.getUniqueId())==FactionRank.OWNER){
                            if(Bukkit.getPlayer(target.getUniqueId())!=null){
                                Bukkit.getPlayer(target.getUniqueId()).sendMessage(ColorUtils.format("&7Faction owner &a"+fac.getName()+"&7 has made you the new owner of "+fac.getName()));
                            }
                            fac.getMembers().put(player.getUniqueId(), FactionRank.OFFICER);
                            fac.getMembers().put(target.getUniqueId(), FactionRank.OWNER);
                            fac.setOwner(target.getUniqueId());
                            fac.updateData();
                            String finalTargetName = target.getName();
                            fac.getOnlinePlayers().forEach(p -> {
                                p.sendMessage(ColorUtils.format("&7Player &a"+player.getName()+"&7 has given ownership of the faction to &a"+ finalTargetName +"&7."));
                            });
                        }else{
                            player.sendMessage(ColorUtils.format("&cYou are not the owner of your faction."));
                        }
                    }else{
                        player.sendMessage(ColorUtils.format("&cYou cannot transfer ownership to yourself"));
                    }
                }else{
                    player.sendMessage(ColorUtils.format("&cThat player is not in your faction"));
                }
            }else{
                player.sendMessage(ColorUtils.format("&cYou are not in a faction"));
            }
        }else{
            player.sendMessage(ColorUtils.format("&cCould not find that player. Command usage: /f owner <player username>"));
        }
    }

    @Subcommand("forcerecalc")
    @Description("Forcibly recalculate the faction top leaderboard")
    @CommandPermission("faction.forcerecalculate")
    public void recalculateFtop(Player player){
        player.sendMessage("Recalculating FTOP.");
        Cavelet.factionManager.factionTop.calculateWorth();
        player.sendMessage("Done");
    }

    @Subcommand("debugftop")
    @Description("Forcibly fill the faction top leaderboard with debug values")
    @CommandPermission("faction.debugftop")
    public void debugFtop(Player player){
        Cavelet.factionManager.factionTop.leaderBoard.clear();
        for(int i = 0;i<25;i++){
            MFaction fac = new MFaction();
            fac.lastUpdatedPoints = new Random().nextInt(2550000);
            Cavelet.factionManager.factionTop.leaderBoard.add(fac);
        }
        player.sendMessage("Done");
    }
}
