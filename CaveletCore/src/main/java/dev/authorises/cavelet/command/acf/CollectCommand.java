package dev.authorises.cavelet.command.acf;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.eventsystem.EventManager;
import dev.authorises.cavelet.gui.CollectGUI;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;

@CommandAlias("collect|claim|claimrewards|rewards|confirm|receive|claims|collectrewards")
public class CollectCommand extends BaseCommand {

    @Default
    public void claimGui(Player player){
        new CollectGUI(player);
    }

    @Subcommand("give")
    @Description("Give a player a collectable reward")
    @CommandPermission("claims.add")
    @CommandCompletion("@players @items")
    public static void addClaim(Player player, OfflinePlayer target, String item) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try{
            if(Cavelet.cachedMPlayers.containsKey(target.getUniqueId())){
                MProfile mp = Cavelet.cachedMPlayers.get(target.getUniqueId());
                mp.addClaim(Bukkit.getPlayer(target.getUniqueId()), Cavelet.customItemsManager.getItemById(item));
                player.sendMessage(ColorUtils.format("&a(Online) Reward added!"));
            }else{
                player.sendMessage(ColorUtils.format("&C(Offline) &aAdding reward..."));
                CompletableFuture.runAsync(() -> {
                    try {
                        MProfile mp = new MProfile(target.getUniqueId(), false);
                        mp.addClaim(Cavelet.customItemsManager.getItemById(item));
                        mp.save();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).thenRun(() -> {
                    player.sendMessage(ColorUtils.format("&C(Offline) &aReward added!"));
                });
            }
        }catch (Exception e){
            e.printStackTrace();
            player.sendMessage(ColorUtils.format("&cError occurred running your command."));
        }
    }



}
