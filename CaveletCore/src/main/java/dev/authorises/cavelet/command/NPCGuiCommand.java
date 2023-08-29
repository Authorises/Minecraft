package dev.authorises.cavelet.command;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.gui.*;
import dev.authorises.cavelet.newshop.ShopCategory;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NPCGuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            Player p = (Player) sender;
            switch (args[0]) {
                case "joey":{
                    new JoeyGUI(p);
                    break;
                }
                case "cthulu_agent":{
                    new CthuluAgentGUI(p);
                    break;
                }
                case "david":{
                    new DavidGUI(p);;
                    break;
                }
                case "will":{
                    new WillGUI(p);
                    break;
                }
                case "shop":{
                    new ShopGUI(p, ShopCategory.ALL);
                    break;
                }
                case "sell":{
                    new SellSelectionGUI(p);
                    break;
                }
                case "amir":{
                    if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                        if (mp.getFarmingLevel() >= 2) {
                            new AmirGUI(p);
                        } else {
                            p.sendMessage(ColorUtils.format("&eAmir -> &fHey, I aint giving these prices to anyone. Come back when you are &eFarming Level 2&f."));
                        }
                    }else{
                        new AmirGUI(p);
                    }
                    break;
                }
            }
        }catch (Exception e){

        }
        return true;
    }
}
