package dev.authorises.cavelet.command.tabcompleter;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemTabCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> options = new ArrayList<>();
        switch(args.length){
            case 1:{
                // Entering the item name
                String sofar = args[0].toUpperCase();
                for(CItemBlueprint<?> blueprint : Cavelet.customItemsManager.getCustomItems()){
                    if(blueprint.getId().toUpperCase().startsWith(sofar) || blueprint.getId().toUpperCase().contains(sofar)) {
                        options.add(blueprint.getId().toUpperCase());
                    }
                }
            }
        }
        return options;
    }
}
