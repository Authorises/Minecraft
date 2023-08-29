package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
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
import java.util.Objects;

public class DeepMarketGUI {

    private Player p;
    public ChestGui gui;

    public void update(ChestGui gui) throws InvalidItemIdException, ParseException {
        gui.getPanes().removeAll(gui.getPanes());
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());

        ItemStack i = p.getInventory().getItemInMainHand();
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 6);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);
        ArrayList<GuiItem> items = new ArrayList<>();
        for(DeepMarketItem item : Cavelet.deepMarketManager.marketItems.values()){
            CItem ci = new CItem(item.getBlueprint());

            items.add(new GuiItem(new ItemBuilder(item.getMaterial())
                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+Cavelet.miniMessage.serialize(Objects.requireNonNull(ci.item.getItemMeta().displayName()))))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Current Price: <#19b6e6>$"+ String.format("%,.2f", item.getPrice())))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Change in 1m: "+DeepMarketUtils.calculateUpDown(item.getPrice1m(), item.getPrice())))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Change in 10m: "+DeepMarketUtils.calculateUpDown(item.getPrice10m(), item.getPrice())))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left click to buy"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Right click to sell"))
                    .toItemStack(), click -> {
                if(click.isLeftClick()){
                    click.getWhoClicked().closeInventory();
                    new DeepMarketBuyGUI(p, item);
                }else if(click.isRightClick()){
                    int count = 0;
                    for(ItemStack s : p.getInventory().getContents()){
                        if(s!=null && s.getType()!=Material.AIR) {
                            NBTItem nbtItem = new NBTItem(s);
                            if (nbtItem.hasKey("item_id")) {
                                if (nbtItem.getString("item_id").toUpperCase().equals(item.getBlueprint().getId())) {
                                    count += s.getAmount();
                                    p.getInventory().remove(s);
                                }
                            }
                        }
                    }
                    if(count!=0) {
                        Double toGive = count * item.getPrice();
                        mp.setBalance(mp.getBalance() + toGive);
                        p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You sold <#5be374>" + count + "x " + Cavelet.miniMessage.serialize(Objects.requireNonNull(ci.item.getItemMeta().displayName())) + " <#9eb5db>for <#19b6e6>$" + String.format("%,.2f", toGive)));
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
                    }
                }
            }));
        }
        PaginatedPane itemsPane = new PaginatedPane(1, 1, 7, 4);
        itemsPane.populateWithGuiItems(items);

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        navigation.addItem(new GuiItem(new ItemBuilder(Material.PAPER)
                .setName(ColorUtils.format("Previous Page")).toItemStack(), event -> {
            event.setCancelled(true);
            if (itemsPane.getPage() > 0) {
                itemsPane.setPage(itemsPane.getPage() - 1);
                gui.update();
            }
        }), 0, 0);

        navigation.addItem(new GuiItem(new ItemBuilder(Material.PAPER)
                .setName(ColorUtils.format("Next Page")).toItemStack(), event -> {
            event.setCancelled(true);
            if (itemsPane.getPage() < itemsPane.getPages() - 1) {
                itemsPane.setPage(itemsPane.getPage() + 1);
                gui.update();
            }
        }), 8, 0);

        gui.addPane(navigation);
        gui.addPane(itemsPane);
        gui.addPane(background);
        gui.update();
    }

    public DeepMarketGUI(Player p){
        try{
            this.p = p;
            ChestGui gui = new ChestGui(6, ColorUtils.format("&dDeep Market"));
            this.gui = gui;
            Cavelet.deepMarketManager.openGuis.add(this);
            gui.setOnClose(event -> {
                Cavelet.deepMarketManager.openGuis.remove(this);
            });
            update(gui);
            gui.show(p);
        }catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(ColorUtils.format("&cAn error occurred opening the Deep Market Interface."));
        }
    }
}
