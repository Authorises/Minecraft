package dev.authorises.cavelet.command.acf;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.gui.CollectGUI;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@CommandAlias("balance|bal|money|wealth")
public class BalanceCommand extends BaseCommand {

    private static void darkSouls(Player sender, UUID uuid) throws Exception {
        if(Cavelet.cachedMPlayers.containsKey(uuid)){
            MProfile mp = Cavelet.cachedMPlayers.get(uuid);
            sender.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>Player <green>"+mp.getLastUsername()+"<#9eb5db> has <#FF55FF>"+String.format("%,.2f", mp.getDarkSouls())+" Dark Souls.")
                    .hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<#9eb5db>Click to view more information")))
                    .clickEvent(ClickEvent.runCommand("/view "+mp.getLastUsername())));
        }else{
            MProfile mp = new MProfile(uuid, false);
            sender.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>Player <green>"+mp.getLastUsername()+"<#9eb5db> has <#FF55FF>"+String.format("%,.2f", mp.getDarkSouls())+" Dark Souls.")
                    .hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<#9eb5db>Click to view more information")))
                    .clickEvent(ClickEvent.runCommand("/view "+mp.getLastUsername())));
        }
    }

    private static void balance(Player sender, UUID uuid) throws Exception {
        if(Cavelet.cachedMPlayers.containsKey(uuid)){
            MProfile mp = Cavelet.cachedMPlayers.get(uuid);
            sender.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>Player <green>"+mp.getLastUsername()+"<#9eb5db> has <#19b6e6>$" + String.format("%,.2f", mp.getBalance()))
                    .hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<#9eb5db>Click to view more information")))
                    .clickEvent(ClickEvent.runCommand("/view "+mp.getLastUsername())));
        }else{
            MProfile mp = new MProfile(uuid, false);
            sender.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>Player <green>"+mp.getLastUsername()+"<#9eb5db> has <#19b6e6>$" + String.format("%,.2f", mp.getBalance()))
                    .hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<#9eb5db>Click to view more information")))
                    .clickEvent(ClickEvent.runCommand("/view "+mp.getLastUsername())));
        }
    }

    @Default
    @Syntax("money/souls username")
    @Description("Display your balance")
    @CommandCompletion("@currencies @players")
    public static void viewBalance(Player player, String balanceType, @Optional OfflinePlayer target) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try{
            if(balanceType.equalsIgnoreCase("MONEY")){
                balance(player, target==null?player.getUniqueId():target.getUniqueId());
            }else if(balanceType.equalsIgnoreCase("SOULS")){
                darkSouls(player, target==null?player.getUniqueId():target.getUniqueId());
            }else{
                balance(player, target==null?player.getUniqueId():target.getUniqueId());
                darkSouls(player, target==null?player.getUniqueId():target.getUniqueId());
            }
        }catch (Exception e){
            e.printStackTrace();
            player.sendMessage(ColorUtils.format("&cError occurred running your command."));
        }
    }



}
