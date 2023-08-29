package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.mining.Ore;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JoeyGUI {
    public JoeyGUI(Player p){
        Cavelet.objectiveManager.getObjective("talk_to_joey").check(p);
        ChestGui gui = new ChestGui(6, ColorUtils.format("&cJoey"));
        OutlinePane oresPane = new OutlinePane(0, 0, 9, 6);
        for(Ore o : Cavelet.oreManager.getOres()){
            CItemBlueprint<?> bp = Cavelet.customItemsManager.getItemById(o.getOreId().toUpperCase());
            BasicCustomItem b = (BasicCustomItem) bp.getItem();
            ItemStack s = new ItemBuilder(o.getItemMaterial())
                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+b.getRarity().color+b.getDisplayName()))
                    .addLoreLine(ColorUtils.format(""))
                    .addLoreLine(ColorUtils.format("&7Sell Price: &b$"+o.getSellPrice()))
                    .addLoreLine(ColorUtils.format(""))
                    .addLoreLine(ColorUtils.format("&7Left Click to sell one"))
                    .addLoreLine(ColorUtils.format("&7Right Click to sell all"))
                    .toItemStack();
            oresPane.addItem(new GuiItem(s, event -> {
                Cavelet.objectiveManager.getObjective("sell_ores").check(p);
                if(event.isLeftClick()){
                    for(ItemStack is : p.getInventory().getContents()){
                        try {
                            if(is!=null) {
                                NBTItem nbtItem = new NBTItem(is);
                                if (nbtItem.hasKey("item_id")) {
                                    Ore ore = Cavelet.oreManager.getOreFromID(nbtItem.getString("item_id").toLowerCase());
                                    if (ore != null && ore == o) {
                                        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                                        Double balanceIncrease = o.getSellPrice()*mp.getSellMultiplier();
                                        p.sendMessage(String.format("%,.2f", mp.getSellMultiplier()));
                                        p.sendMessage(ColorUtils.format("&fYou sold &a1x&f " + ore.getDisplayName() + "&f for &b$" + String.format("%,.2f", balanceIncrease)));
                                        mp.setBalance(mp.getBalance() + balanceIncrease);
                                        if (is.getAmount() > 1) {
                                            is.setAmount(is.getAmount() - 1);
                                        } else {
                                            p.getInventory().remove(is);
                                        }
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }if(event.isRightClick()){
                    for(ItemStack is : p.getInventory().getContents()){
                        try {
                            if(is!=null) {
                                NBTItem nbtItem = new NBTItem(is);
                                if (nbtItem.hasKey("item_id")) {
                                    Ore ore = Cavelet.oreManager.getOreFromID(nbtItem.getString("item_id"));
                                    if (ore != null && ore == o) {
                                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                                        int sellAmount = (o.getSellPrice() * is.getAmount());
                                        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                                        Double balanceIncrease = sellAmount*mp.getSellMultiplier();
                                        p.sendMessage(ColorUtils.format("&fYou sold &a" + is.getAmount() + "x&f " + ore.getDisplayName() + "&f for &b$" + String.format("%,.2f", balanceIncrease)));
                                        mp.setBalance(mp.getBalance() + balanceIncrease);
                                        p.getInventory().remove(is);
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                event.setCancelled(true);
            }));
        }
        gui.addPane(oresPane);
        gui.show(p);
    }
}
