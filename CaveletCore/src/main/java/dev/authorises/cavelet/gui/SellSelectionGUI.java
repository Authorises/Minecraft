package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.newshop.SellPrice;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SellSelectionGUI {

    public void update(ChestGui gui, Player p) throws InvalidItemIdException {
        gui.getPanes().removeAll(gui.getPanes());
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());

        ItemStack i = p.getInventory().getItemInMainHand();
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 6);


        OutlinePane sellables = new OutlinePane(1,1,7,4);

        HashMap<SellPrice, Integer> amountMap = new HashMap<>();

        for(ItemStack item : p.getInventory().getContents()){
            if(item!=null) {
                NBTItem nbtItem = new NBTItem(item);
                if (nbtItem.hasKey("item_id")) {
                    try {
                        SellPrice sellPrice = SellPrice.valueOf(nbtItem.getString("item_id"));
                        if (amountMap.containsKey(sellPrice)) {
                            amountMap.put(sellPrice, amountMap.get(sellPrice) + item.getAmount());
                        } else {
                            amountMap.put(sellPrice, item.getAmount());
                        }
                    } catch (IllegalArgumentException e) {

                    }
                }
            }
        }

        if(amountMap.size()==0){
            background.addItem(new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                    .setComponentName(Cavelet.miniMessage.deserialize("<red>You do not have any items to sell in your inventory"))
                    .toItemStack()));
            background.setRepeat(true);
            background.setPriority(Pane.Priority.LOWEST);
        }else{
            background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
            background.setRepeat(true);
            background.setPriority(Pane.Priority.LOWEST);
        }

        double miningBonus = Math.floor(mp.getMiningLevel().floatValue()/5F)/10;

        amountMap.forEach((sellPrice, amount) -> {
            Double totalValue = sellPrice.priceEach*amount;

            sellables.addItem(
                    new GuiItem(
                            new ItemBuilder(new CItem(Cavelet.customItemsManager.getItemById(sellPrice.itemId)).getItemStack())
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>You have <#19b6e6>x"+String.format("%,d", amount)))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Sell Price: <#5be374>$" + String.format("%,.2f", sellPrice.priceEach*(1+miningBonus))+" e/a "+(miningBonus>=0?"<green>(+"+String.format("%.0f", miningBonus*100)+"%)":"")))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to sell"))
                                    .toItemStack()
                            , (click) -> {
                                new SellSellGUI(p, sellPrice, amount);
                             }
                    )
            );
        });

        gui.addPane(sellables);
        gui.addPane(background);
        gui.update();
    }

    public SellSelectionGUI(Player p) {
        try {
            ChestGui gui = new ChestGui(6, ColorUtils.format("&dSell Items"));
            update(gui, p);
            gui.show(p);
        } catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(ColorUtils.format("&cAn error occurred whilst opening this"));
        }
    }
}