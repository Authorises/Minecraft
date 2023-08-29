package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.deepmarket.DeepMarketItem;
import dev.authorises.cavelet.deepmarket.DeepMarketUtils;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class DeepMarketBuyGUI {
    private Player p;
    public ChestGui gui;
    private DeepMarketItem item;

    public void update(ChestGui gui) throws InvalidItemIdException, ParseException {
        gui.getPanes().removeAll(gui.getPanes());
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());

        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 3);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        OutlinePane options = new OutlinePane(1, 1, 7, 1);
        LinkedHashMap<Integer, Double> amountPrice = new LinkedHashMap<>();
        amountPrice.put(1, DeepMarketUtils.round(item.getPrice(), 2));
        amountPrice.put(2, DeepMarketUtils.round(item.getPrice()*2, 2));
        amountPrice.put(4, DeepMarketUtils.round(item.getPrice()*4, 2));
        amountPrice.put(8, DeepMarketUtils.round(item.getPrice()*8, 2));
        amountPrice.put(16, DeepMarketUtils.round(item.getPrice()*16, 2));
        amountPrice.put(32, DeepMarketUtils.round(item.getPrice()*32, 2));
        amountPrice.put(64, DeepMarketUtils.round(item.getPrice()*64, 2));

        for(Integer i : amountPrice.keySet()){
            options.addItem(new GuiItem(new ItemBuilder(item.getMaterial(), i)
                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Purchase x"+i))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>This will cost <#19b6e6>$"+String.format("%,.2f", amountPrice.get(i))))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left Click to purchase"))
                    .toItemStack(), click -> {
                if(click.isLeftClick()){
                    if((mp.getBalance()-amountPrice.get(i))>=0){
                        try {
                            CItem ci = new CItem(item.getBlueprint());
                            mp.setBalance(mp.getBalance()-amountPrice.get(i));
                            int x = i;
                            while(x>0) {
                                p.getInventory().addItem(new CItem(item.getBlueprint()).getItemStack());
                                x-=1;
                            }
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
                            p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You purchased <#5be374>" + i + "x " + Cavelet.miniMessage.serialize(Objects.requireNonNull(ci.getItemStack().getItemMeta().displayName())) + " <#9eb5db>for <#19b6e6>$" + String.format("%,.2f", amountPrice.get(i))));
                        } catch (Exception e) {
                            p.sendMessage(ColorUtils.format("&cAn error occurred processing your request!"));
                            throw new RuntimeException(e);
                        }
                    }else{
                        p.sendMessage(ColorUtils.format("&bYou do not have enough money to purchase this item"));
                    }
                }
            }));
        }

        gui.addPane(options);
        gui.addPane(background);
        gui.update();
    }

    public DeepMarketBuyGUI(Player p, DeepMarketItem item){
        try{
            this.p = p;
            this.item = item;
            ChestGui gui = new ChestGui(3, ColorUtils.format("&dDeep Market | Buy"));
            this.gui = gui;
            Cavelet.deepMarketManager.openBuyGuis.add(this);
            gui.setOnClose(event -> {
                Cavelet.deepMarketManager.openBuyGuis.remove(this);
                new DeepMarketGUI(p);
            });
            update(gui);
            gui.show(p);
        }catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(ColorUtils.format("&cAn error occurred opening the Deep Market Interface."));
        }
    }
}
