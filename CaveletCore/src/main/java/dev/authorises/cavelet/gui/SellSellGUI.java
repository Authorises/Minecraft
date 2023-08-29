package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.deepmarket.DeepMarketItem;
import dev.authorises.cavelet.deepmarket.DeepMarketUtils;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.newshop.SellPrice;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ChatUtil;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;
import org.json.simple.parser.ParseException;
import scala.Int;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.LoggingPermission;

public class SellSellGUI {
    private Player p;
    public ChestGui gui;
    private SellPrice item;
    private Integer stackSize;
    private Integer playersAmount;
    private Material material;

    private boolean remove(String itemId, int requiredAmount){
        HashMap<ItemStack, Integer> newAmounts = new HashMap<>();
        int left = requiredAmount;
        for(ItemStack s : p.getInventory().getContents()){
            if(s!=null) {
                if (left > 0) {
                    NBTItem i = new NBTItem(s);
                    if (i.hasKey("item_id")) {
                        if (i.getString("item_id").equals(itemId)) {
                            if (left - s.getAmount() > 0) {
                                left -= s.getAmount();
                                newAmounts.put(s, 0);
                            } else {
                                int x = s.getAmount() - left;
                                left = 0;
                                newAmounts.put(s, x);
                            }
                        }
                    }
                }
            }

        }

        if(left>0){
            return false;
        }

        newAmounts.forEach((a,b) -> {
            if(b>0){
                a.setAmount(b);
            }else{
                p.getInventory().remove(a);
            }
        });

        return true;

    }

    public void update(ChestGui gui) throws InvalidItemIdException, ParseException {
        gui.getPanes().removeAll(gui.getPanes());
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());

        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 3);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        OutlinePane options = new OutlinePane(1, 1, 7, 1);

        double miningSellBonus = Math.floor(mp.getMiningLevel().floatValue()/5F)/10;

        double multiplyBonus = miningSellBonus+1;

        String bonusMessage = miningSellBonus >= 0 ? " <green>(+" + String.format("%.0f", miningSellBonus * 100) + "%)" : "";
        options.addItem(new GuiItem(new ItemBuilder(this.material, 1)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Sell <#19b6e6>1x<#9eb5db> for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*1)+ bonusMessage))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to sell"))
                .toItemStack(), (click) -> {

            if(remove(item.itemId, 1)){
                mp.setBalance(mp.getBalance()+(this.item.priceEach*multiplyBonus*1));
                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You sold <#19b6e6>1x ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById(item.itemId)).append(Cavelet.miniMessage.deserialize(" <#9eb5db>for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*1)))));
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
            }else{
                p.sendMessage(ColorUtils.format("&cYou do not have as many items as you want to sell."));
            }
        }));

        options.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

        options.addItem(new GuiItem(new ItemBuilder(this.material, 8)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Sell <#19b6e6>8x<#9eb5db> for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*8)+ bonusMessage))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to sell"))
                .toItemStack(), (click) -> {

            if(remove(item.itemId, 8)){
                mp.setBalance(mp.getBalance()+(this.item.priceEach*multiplyBonus*8));
                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You sold <#19b6e6>8x ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById(item.itemId)).append(Cavelet.miniMessage.deserialize(" <#9eb5db>for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*8)))));
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
            }else{
                p.sendMessage(ColorUtils.format("&cYou do not have as many items as you want to sell."));
            }
        }));

        options.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

        options.addItem(new GuiItem(new ItemBuilder(this.material, stackSize)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Sell <#19b6e6>"+stackSize+"x<#9eb5db> for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*stackSize)+ bonusMessage))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to sell"))
                .toItemStack(), (click) -> {

            if(remove(item.itemId, stackSize)){
                mp.setBalance(mp.getBalance()+(this.item.priceEach*multiplyBonus*stackSize));
                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You sold <#19b6e6>"+stackSize+"x ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById(item.itemId)).append(Cavelet.miniMessage.deserialize(" <#9eb5db>for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*stackSize)))));
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
            }else{
                p.sendMessage(ColorUtils.format("&cYou do not have as many items as you want to sell."));
            }
        }));

        options.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

        options.addItem(new GuiItem(new ItemBuilder(Material.CAULDRON, 1)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Sell <#19b6e6>"+playersAmount+"x<#9eb5db> for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*playersAmount)+ bonusMessage))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to sell"))
                .toItemStack(), (click) -> {

            if(remove(item.itemId, playersAmount)){
                mp.setBalance(mp.getBalance()+(this.item.priceEach*multiplyBonus*playersAmount));
                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You sold <#19b6e6>"+playersAmount+"x ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById(item.itemId)).append(Cavelet.miniMessage.deserialize(" <#9eb5db>for <#5be374>$" + String.format("%,.2f", this.item.priceEach*multiplyBonus*playersAmount)))));
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
            }else{
                p.sendMessage(ColorUtils.format("&cYou do not have as many items as you want to sell."));
            }
        }));

        gui.addPane(options);
        gui.addPane(background);
        gui.update();
    }

    public SellSellGUI(Player p, SellPrice item, Integer playersAmount){
        try{
            this.p = p;
            this.item = item;
            CItem cItem = new CItem(Cavelet.customItemsManager.getItemById(item.itemId));
            this.material = cItem.getItemStack().getType();
            this.stackSize = cItem.getBlueprint().getStackable()?cItem.getItemStack().getType().getMaxStackSize():1;
            ChestGui gui = new ChestGui(3, ColorUtils.format("&dSell Items"));
            this.gui = gui;
            this.playersAmount = playersAmount;

            gui.setOnClose(event -> {
                new SellSelectionGUI(p);
            });

            update(gui);
            gui.show(p);
        }catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(ColorUtils.format("&cAn error occurred opening the Specific Item Sell gui."));
        }
    }
}
