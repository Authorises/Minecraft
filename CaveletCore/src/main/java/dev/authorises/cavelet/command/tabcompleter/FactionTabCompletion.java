package dev.authorises.cavelet.command.tabcompleter;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.FactionRank;
import dev.authorises.cavelet.factions.MFaction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FactionTabCompletion implements TabCompleter {



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(commandSender instanceof Player){
            Player p = (Player) commandSender;

            List<String> options = new ArrayList<>();

            if(args.length==0){

                List<String> rargs = new ArrayList<>();

                rargs.add("help");
                rargs.add("top");
                rargs.add("show");

                if(Cavelet.factionManager.playersFactions.containsKey(p.getUniqueId())) {
                    // In a faction

                    rargs.add("leave");
                    rargs.add("kick");

                    MFaction faction = Cavelet.factionManager.playersFactions.get(p.getUniqueId());
                    if (faction.getMembers().get(p.getUniqueId()).weight >= FactionRank.OFFICER.weight) {
                        rargs.add("invite");
                    }
                }else{
                    rargs.add("join");
                    rargs.add("create");
                }

                String sofar = args[0].toUpperCase();
                rargs.forEach(e -> {
                    if(e.toUpperCase().startsWith(sofar) || e.toUpperCase().contains(sofar)) {
                        options.add(e);
                    }
                });


            }if(args.length == 1){



            }


            return options;
        }




        return null;
    }




}
