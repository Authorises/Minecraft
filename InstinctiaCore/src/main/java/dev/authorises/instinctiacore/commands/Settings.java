package dev.authorises.instinctiacore.commands;

import dev.authorises.instinctiacore.objects.PlayerData;
import dev.authorises.instinctiacore.objects.PlayerDataManager;
import dev.authorises.instinctiacore.objects.Punishment;
import dev.authorises.instinctiacore.objects.PunishmentManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Settings extends Command {

    public Settings(){
        super("settings", "", "options");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) commandSender;
            CompletableFuture.supplyAsync(
                    () -> {
                        return PlayerDataManager.getPlayerById(p.getUniqueId());
                    }
            ).thenApply((data) -> {
                p.sendMessage(data.settings.toString());
                return 0;
            });
        }
    }
}
