package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.deepmarket.DeepMarketItem;
import dev.authorises.cavelet.deepmarket.DeepMarketUtils;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.newshop.ShopCategory;
import dev.authorises.cavelet.newshop.ShopItem;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.InventoryUtil;
import dev.authorises.cavelet.utils.ItemBuilder;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;

import java.util.LinkedHashMap;
import java.util.Objects;

public class ShopBuyGUI {

    private Player p;
    public ChestGui gui;
    private ShopItem item;

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
        amountPrice.put(1, DeepMarketUtils.round(item.priceEach(), 2));
        amountPrice.put(2, DeepMarketUtils.round(item.priceEach()*2, 2));
        amountPrice.put(4, DeepMarketUtils.round(item.priceEach()*4, 2));
        amountPrice.put(8, DeepMarketUtils.round(item.priceEach()*8, 2));
        amountPrice.put(16, DeepMarketUtils.round(item.priceEach()*16, 2));
        amountPrice.put(32, DeepMarketUtils.round(item.priceEach()*32, 2));
        amountPrice.put(64, DeepMarketUtils.round(item.priceEach()*64, 2));

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
                            double slots = Integer.valueOf(i/item.maxSlotSize()).doubleValue();
                            double cost = amountPrice.get(i);
                            if(cost<=mp.getBalance()){
                                if(InventoryUtil.freeSlots(p)>=slots){
                                    p.showTitle(Title.title(Cavelet.miniMessage.deserialize("<green>Purchase successful"), Cavelet.miniMessage.deserialize("<#9eb5db>You have received your items")));
                                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                                    item.purchase(p, i);
                                }else{
                                    p.sendMessage(ColorUtils.format("&cYou do not have enough inventory space"));
                                    p.showTitle(Title.title(Cavelet.miniMessage.deserialize("<red>Purchase failed"), Cavelet.miniMessage.deserialize("<#9eb5db>Not enough inventory space")));
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                                }
                            }else{
                                p.sendMessage(ColorUtils.format("&cYou cannot afford to purchase that"));
                                p.showTitle(Title.title(Cavelet.miniMessage.deserialize("<red>Purchase failed"), Cavelet.miniMessage.deserialize("<#9eb5db>Insufficient funds")));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                            }
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

    public ShopBuyGUI(Player p, ShopItem item, ShopCategory category){
        try{
            this.p = p;
            this.item = item;
            ChestGui gui = new ChestGui(3, ColorUtils.format("&dShop Market | Buy"));
            this.gui = gui;
            update(gui);
            gui.show(p);
            gui.setOnClose((close) -> {
                new ShopGUI(p, category);
            });
        }catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(ColorUtils.format("&cAn error occurred."));
        }
    }

}
