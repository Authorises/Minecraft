package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class MigrateOldDataCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(Cavelet.dataFileConfig.contains("playerdata."+p.getUniqueId().toString()+".last-username")){
                long l = System.currentTimeMillis();
                p.sendMessage(ColorUtils.format("&aMigrating your data..."));
                Cavelet.playerData.updateOne(new Document("_id", p.getUniqueId().toString()), combine(
                        set("last-username", Cavelet.dataFileConfig.getString("playerdata."+p.getUniqueId()+".last-username")),
                        set("balance", Cavelet.dataFileConfig.getDouble("playerdata."+p.getUniqueId()+".balance")),
                        set("dark-souls", Cavelet.dataFileConfig.getDouble("playerdata."+p.getUniqueId()+".dark-souls")),
                        set("kills", Cavelet.dataFileConfig.getDouble("playerdata."+p.getUniqueId()+".kills")),
                        set("deaths", Cavelet.dataFileConfig.getDouble("playerdata."+p.getUniqueId()+".deaths")),
                        set("skills", new Document("farming-level", Cavelet.dataFileConfig.getInt("playerdata."+p.getUniqueId()+".farming-level"))
                                .append("farming-xp", Cavelet.dataFileConfig.getInt("playerdata."+p.getUniqueId()+".farming-xp"))
                                .append("mining-level", Cavelet.dataFileConfig.getInt("playerdata."+p.getUniqueId()+".mining-level"))
                                .append("mining-xp", Cavelet.dataFileConfig.getInt("playerdata."+p.getUniqueId()+".mining-xp"))
                                .append("combat-level", Cavelet.dataFileConfig.getInt("playerdata."+p.getUniqueId()+".combat-level"))
                                .append("combat-xp", Cavelet.dataFileConfig.getInt("playerdata."+p.getUniqueId()+".combat-xp"))),
                        set("completed-objectives", Cavelet.dataFileConfig.get("playerdata."+p.getUniqueId()+".completed-objectives"))));
                Cavelet.dataFileConfig.set("playerdata."+p.getUniqueId(), null);
                try {
                    Cavelet.dataFileConfig.save(Cavelet.dataFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                p.sendMessage(ColorUtils.format("&aDone! Took "+(System.currentTimeMillis()-l)+"ms"));
                Cavelet.cachedMPlayers.remove(p.getUniqueId());
                try {
                    Cavelet.cachedMPlayers.put(p.getUniqueId(), new MProfile(p));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                p.sendMessage(ColorUtils.format("&cYou have no old data to migrate from"));
            }
        }
        return true;
    }
}
