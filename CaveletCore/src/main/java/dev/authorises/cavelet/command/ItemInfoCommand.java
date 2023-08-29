package dev.authorises.cavelet.command;

import com.google.gson.Gson;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){
            Player p = (Player) sender;
            try {
                if(args.length>=1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        for (CItemBlueprint ci : Cavelet.customItemsManager.getCustomItems()) {
                            Component c = Cavelet.miniMessage.deserialize("" +
                                    "<reset><br><white>Item ID: <aqua>" + ci.getId() +
                                    "<reset><br><white>Item Rarity: " + ci.getRarity().displayName + "<br>");
                            c = c.append(Component.text(ColorUtils.format("&bCopy CItem Data &7(Click)")).clickEvent(ClickEvent.copyToClipboard(new Gson().toJson(ci))));
                            p.sendMessage(c);
                        }
                    }
                }else {
                    ItemStack i = p.getInventory().getItemInMainHand();
                    NBTItem nbtItem = new NBTItem(i);

                    CItem ci = new CItem(i);

                    Component c = Cavelet.miniMessage.deserialize("" +
                            "<reset><br><white>Item ID: <aqua>" + ci.getBlueprint().getId() +
                            "<reset><br><white>Points Value: <aqua>" + ci.pointsValue +
                            "<reset><br><white>Item Rarity: " + ci.getBlueprint().getRarity().displayName + "<br>");
                    c = c.append(Component.text(ColorUtils.format("&bCopy CItemBlueprint Data &7(Click)")).clickEvent(ClickEvent.copyToClipboard(new Gson().toJson(ci.getBlueprint()))));
                    c = c.append(Component.text(ColorUtils.format("\n&fEnchantments:")));
                    for(CustomEnchant e : ci.getCustomEnchants()){
                        c = c.append(Component.text(ColorUtils.format("\n"+new Gson().toJson(e))));
                    }
                    if(nbtItem.hasKey("damage")){
                        c=c.append(Component.text(ColorUtils.format("\n&fDamage: &b"+nbtItem.getDouble("damage"))));
                    }
                    p.sendMessage(c);
                }
            }catch (Exception e){
                e.printStackTrace();
                p.sendMessage(e.getMessage());
            }
        }

        return true;
    }
}
