package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.shop.*;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class AmirGUI {

    public AmirGUI(Player p){
        ChestGui gui = new ChestGui(6, ColorUtils.format("&cAmir"));
        Integer x = 0;
        PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);
        ArrayList<GuiItem> guiItems = new ArrayList<>();
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        for(CustomItemShop shop : Cavelet.amirShop.getShops()){
            guiItems.add(new GuiItem(shop.getShopItem(), click -> {
               shop.purchase(p);
            }));
        }
        /**
        for(Buyable b : Cavelet.buyableManager.getBuyables()){
            if(b instanceof ItemStackShop){
                ItemStackShop s = (ItemStackShop) b;
                boolean add = true;
                if(s.getType().equals(ShopType.TEMPORARY)){
                    if(System.currentTimeMillis()>=s.getMillisecondsExpires()){
                        add=false;
                    }
                }
                if(add) {
                    if (Arrays.stream(((ItemStackShop) b).getBuyableFroms()).toList().contains(BuyableFrom.AMIR)) {
                        guiItems.add(new GuiItem(s.getShopItem(), event -> {
                            event.setCancelled(true);
                            if (event.isLeftClick()) {
                                s.purchase(p);
                            }
                        }));
                    }
                }
            }
        }
         */
        pages.populateWithGuiItems(guiItems);
        gui.addPane(pages);
        OutlinePane background = new OutlinePane(0, 5, 9, 1);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), click ->click.setCancelled(true)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);
        gui.addPane(background);
        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(new ItemBuilder(Material.PAPER)
                .setName(ColorUtils.format("Previous Page")).toItemStack(), event -> {
            event.setCancelled(true);
            if (pages.getPage() > 0) {
                pages.setPage(pages.getPage() - 1);

                gui.update();
            }
        }), 0, 0);

        navigation.addItem(new GuiItem(new ItemBuilder(Material.PAPER)
                .setName(ColorUtils.format("Next Page")).toItemStack(), event -> {
            event.setCancelled(true);
            if (pages.getPage() < pages.getPages() - 1) {
                pages.setPage(pages.getPage() + 1);

                gui.update();
            }
        }), 8, 0);

        navigation.addItem(new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .setName(ColorUtils.format("&cClose Menu")).toItemStack(), event ->
                event.getWhoClicked().closeInventory()), 4, 0);

        gui.addPane(navigation);

        gui.show(p);
    }

}
