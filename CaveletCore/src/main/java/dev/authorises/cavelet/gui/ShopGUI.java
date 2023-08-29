package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.newshop.ShopCategory;
import dev.authorises.cavelet.newshop.ShopItem;
import dev.authorises.cavelet.newshop.ShopItemType;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.InventoryUtil;
import dev.authorises.cavelet.utils.ItemBuilder;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class ShopGUI {

    private ChestGui gui;
    private Player player;
    private MProfile mProfile;

    private void update(ShopCategory category){
        gui.getPanes().removeAll(gui.getPanes());
        PaginatedPane itemsPane = new PaginatedPane(0, 0, 9, 5);

        List<ShopItem> items = Cavelet.newShopManager.shopItems;
        List<GuiItem> guiItems = new ArrayList<>();
        items.forEach((item) -> {
            if(item.getCategories().contains(category) || category==ShopCategory.ALL){
                guiItems.add(new GuiItem(item.getDispayItem(), click -> {
                    if(item.canPurchaseMany()){

                        new ShopBuyGUI(player, item, category);
                    }else{
                        if(mProfile.getBalance()-item.priceEach()>=0){
                            try {
                                double slots = 1;
                                double cost = item.priceEach();
                                if(cost<=mProfile.getBalance()){
                                    if(InventoryUtil.freeSlots(player)>=slots){
                                        player.showTitle(Title.title(Cavelet.miniMessage.deserialize("<green>Purchase successful"), Cavelet.miniMessage.deserialize("<#9eb5db>You have received your items")));
                                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                                        item.purchase(player, 1);
                                    }else{
                                       player.sendMessage(ColorUtils.format("&cYou do not have enough inventory space"));
                                       player.showTitle(Title.title(Cavelet.miniMessage.deserialize("<red>Purchase failed"), Cavelet.miniMessage.deserialize("<#9eb5db>Not enough inventory space")));
                                       player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                                    }
                                }else{
                                   player.sendMessage(ColorUtils.format("&cYou cannot afford toplayerurchase that"));
                                   player.showTitle(Title.title(Cavelet.miniMessage.deserialize("<red>Purchase failed"), Cavelet.miniMessage.deserialize("<#9eb5db>Insufficient funds")));
                                   player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                                }
                            } catch (Exception e) {
                               player.sendMessage(ColorUtils.format("&cAn error occurredplayerrocessing your request!"));
                                throw new RuntimeException(e);
                            }
                        }else{
                           player.sendMessage(ColorUtils.format("&bYou do not have enough money toplayerurchase this item"));
                        }
                    }
                }));
            }
        });

        itemsPane.populateWithGuiItems(guiItems);

        OutlinePane previousPage = new OutlinePane(0, 5, 1, 1);
        previousPage.addItem(new GuiItem(new ItemBuilder(Material.ARROW)
                .setComponentName(Cavelet.miniMessage.deserialize("<#9eb5db>Previous page"))
                .toItemStack(), click -> {
            itemsPane.setPage(itemsPane.getPage()-1);
        }));


        OutlinePane nextPage = new OutlinePane(8, 5, 1, 1);
        nextPage.addItem(new GuiItem(new ItemBuilder(Material.ARROW)
                .setComponentName(Cavelet.miniMessage.deserialize("<#9eb5db>Next page"))
                .toItemStack(), click -> {
            itemsPane.setPage(itemsPane.getPage()+1);
        }));

        OutlinePane categories = new OutlinePane(1, 5, 7, 1);
        for(ShopCategory lCat : ShopCategory.values()){
            categories.addItem(new GuiItem(new ItemBuilder(lCat.item)
                    .setName(ColorUtils.format(lCat.color+lCat.name))
                    .toItemStack(), click -> {
                click.setCancelled(true);
                update(lCat);
            }));
        }

        gui.addPane(itemsPane);
        gui.addPane(categories);

        if (itemsPane.getPage() < itemsPane.getPages()-1){
            gui.addPane(nextPage);
        }

        if(itemsPane.getPage()>0){
            gui.addPane(previousPage);
        }

        gui.setTitle(ColorUtils.format("&dShop &8| "+category.name));
        gui.setOnGlobalClick((click)->{
            click.setCancelled(true);
        });
        gui.update();
    }

    public ShopGUI(Player player, ShopCategory category){
        this.player = player;
        this.mProfile = Cavelet.cachedMPlayers.get(player.getUniqueId());
        this.gui = new ChestGui(6, "&dShop");
        update(category);
        gui.show(player);
    }



}
