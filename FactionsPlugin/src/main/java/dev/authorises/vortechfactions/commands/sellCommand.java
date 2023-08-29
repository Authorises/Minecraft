package dev.authorises.vortechfactions.commands;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class sellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length>=1){
                if(args[0].equalsIgnoreCase("hand")){
                    try{
                        if(Items.materialSellValues.containsKey(p.getItemInHand().getType())){
                            Integer amount = p.getItemInHand().getAmount();
                            float sellAmount = amount * Items.materialSellValues.get(p.getItemInHand().getType());
                            VortechFactions.econ.depositPlayer(p, sellAmount);
                            p.setItemInHand(null);
                            p.sendMessage(ColorUtils.format("&6Sell!&f You sold &d"+amount+"&f items for &a$"+sellAmount+"&f!"));
                        }else{
                            if(Items.metaSellValues.containsKey(p.getItemInHand().getItemMeta())){
                                Integer amount = p.getItemInHand().getAmount();
                                Integer sellAmount = amount * Items.metaSellValues.get(p.getItemInHand().getItemMeta());
                                VortechFactions.econ.depositPlayer(p, sellAmount);
                                p.setItemInHand(null);
                                p.sendMessage(ColorUtils.format("&6Sell!&f You sold &d"+amount+"&f items for &a$"+sellAmount+"&f!"));
                            }else{
                                p.sendMessage(ColorUtils.format("&bError!&f That item cannot be sold!"));
                            }
                        }
                    }catch (NullPointerException e){
                        p.sendMessage(ColorUtils.format("&bError!&F You must be holding something to do this!"));
                    }
                }if(args[0].equalsIgnoreCase("inventory")||args[0].equalsIgnoreCase("inv")){
                    float amount = 0;
                    float sellAmount = 0;
                    for(ItemStack i : p.getInventory().getContents()){
                        try {
                            ItemMeta t = i.getItemMeta();
                            Material ty = i.getType();
                            float iAmount = i.getAmount();
                            float sellTotal = 0;
                            try {
                                if(Items.materialSellValues.containsKey(ty)){
                                    sellTotal = iAmount * Items.materialSellValues.get(ty);
                                    amount += iAmount;
                                    p.getInventory().removeItem(i);
                                }else{
                                    if (Items.metaSellValues.containsKey(t)) {
                                        sellTotal = iAmount * Items.metaSellValues.get(t);
                                        amount += iAmount;
                                        p.getInventory().removeItem(i);
                                    }
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            sellAmount += sellTotal;
                        }catch (Exception e){

                        }
                    }
                    if(amount!=0){
                        p.sendMessage(ColorUtils.format("&6Sell!&f You sold &d"+amount+"&f items for &a$"+sellAmount+"&f!"));
                        VortechFactions.econ.depositPlayer(p, sellAmount);
                    }else {
                        p.sendMessage(ColorUtils.format("&bError!&f No sellable items were found in your inventory!"));
                    }
                }if(args[0].equalsIgnoreCase("chest")){
                    if(VortechFactions.api.getFactionAt(p.getLocation()).getName()!=VortechFactions.api.getFaction(p).getName()){
                        p.sendMessage(ColorUtils.format("&bError!&F You can only do this command in your own claims."));
                    }else{
                        if(Items.metaSellValues.containsKey(p.getItemInHand().getItemMeta())){
                            Chunk chunk = p.getLocation().getChunk();
                            float sellGain = 0;
                            float sellCount = 0;
                            final int minX = chunk.getX() << 4;
                            final int minZ = chunk.getZ() << 4;
                            final int maxX = minX | 15;
                            final int maxY = chunk.getWorld().getMaxHeight();
                            final int maxZ = minZ | 15;
                            for (int x = minX; x <= maxX; ++x) {
                                for (int y = 0; y <= maxY; ++y) {
                                    for (int z = minZ; z <= maxZ; ++z) {
                                        Block block = chunk.getBlock(x, y, z);
                                        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
                                            Chest lc = (Chest) block.getState();
                                            for (ItemStack i : lc.getInventory().getContents()) {
                                                try {
                                                    if (p.getItemInHand().getItemMeta().equals(i.getItemMeta())) {
                                                        sellCount = sellCount + i.getAmount();
                                                        sellGain = sellGain + (Items.metaSellValues.get(i.getItemMeta())*i.getAmount());
                                                        lc.getInventory().remove(i);
                                                    }
                                                } catch (NullPointerException ignored) {

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (sellCount != 0) {
                                p.sendMessage(ColorUtils.format("&6Sell!&F You sold &d"+sellCount+"&f items for &a$"+sellGain));
                                VortechFactions.econ.depositPlayer(p, sellGain);
                            }else{
                                p.sendMessage(ColorUtils.format("&bError!&f There are no containers within your chunk that contain your held item."));
                            }
                        }else if(Items.materialSellValues.containsKey(p.getItemInHand().getType())){
                            Chunk chunk = p.getLocation().getChunk();
                            float sellGain = 0;
                            float sellCount = 0;
                            final int minX = chunk.getX() << 4;
                            final int minZ = chunk.getZ() << 4;
                            final int maxX = minX | 15;
                            final int maxY = chunk.getWorld().getMaxHeight();
                            final int maxZ = minZ | 15;
                            for (int x = minX; x <= maxX; ++x) {
                                for (int y = 0; y <= maxY; ++y) {
                                    for (int z = minZ; z <= maxZ; ++z) {
                                        Block block = chunk.getBlock(x, y, z);
                                        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
                                            Chest lc = (Chest) block.getState();
                                            for (ItemStack i : lc.getInventory().getContents()) {
                                                try {
                                                        if(p.getItemInHand().getType().equals(i.getType())){
                                                            sellCount = sellCount + i.getAmount();
                                                            sellGain = sellGain + (Items.materialSellValues.get(i.getType())*i.getAmount());
                                                            lc.getInventory().remove(i);
                                                        }
                                                } catch (NullPointerException ignored) {

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (sellCount != 0) {
                                p.sendMessage(ColorUtils.format("&6Sell!&F You sold &d"+sellCount+"&f items for &a$"+sellGain));
                                VortechFactions.econ.depositPlayer(p, sellGain);
                            }else{
                                p.sendMessage(ColorUtils.format("&bError!&f There are no containers within your chunk that contain your held item."));
                            }
                        }else{
                            p.sendMessage(ColorUtils.format("&bError!&F Your held item cannot be sold."));
                        }
                    }
                }
            }else{
                p.sendMessage(ColorUtils.format("&bError!&F Command Usage:&6 /sell ( hand | inventory | chest )"));
            }
        }
        return true;
    }
}
