package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnchantsGUI {
    private Player p;
    public ChestGui gui;
    private Boolean backButton;

    public void update(){
        gui.getPanes().removeAll(gui.getPanes());

        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 6);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        PaginatedPane enchants = new PaginatedPane(1, 1, 7, 4);
        List<GuiItem> items = new ArrayList<>();
        for(CustomEnchantType type : CustomEnchantType.values()){
            items.add(new GuiItem(
                    new ItemBuilder(Material.ENCHANTED_BOOK)
                            .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+type.tier.color+type.displayName))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+type.tier.color+type.tier.displayName+" Enchantment"))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Highest level <light_purple>"+type.maxLevel))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<#9eb5db>"+type.description))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                            .toItemStack()
            ));
        }
        enchants.populateWithGuiItems(items);

        StaticPane navigation = new StaticPane(0, 6, 9, 1);

        if(enchants.getPage()>0) {
            navigation.addItem(new GuiItem(new ItemBuilder(Material.PAPER)
                    .setName(ColorUtils.format("Previous Page")).toItemStack(), event -> {
                event.setCancelled(true);
                if (enchants.getPage() > 0) {
                    enchants.setPage(enchants.getPage() - 1);

                    gui.update();
                }
            }), 0, 0);
        }

        if (enchants.getPage() < enchants.getPages() - 1) {
            navigation.addItem(new GuiItem(new ItemBuilder(Material.PAPER)
                    .setName(ColorUtils.format("Next Page")).toItemStack(), event -> {
                event.setCancelled(true);
                if (enchants.getPage() < enchants.getPages() - 1) {
                    enchants.setPage(enchants.getPage() + 1);

                    gui.update();
                }
            }), 8, 0);
        }

        gui.addPane(background);
        gui.addPane(enchants);
        gui.addPane(navigation);

        gui.update();


    };

    public EnchantsGUI(Player p, Boolean backButton){
        this.backButton = backButton;
        this.p = p;
        gui = new ChestGui(6, ColorUtils.format("&dEnchantments"));


        gui.show(p);



        update();
    }
}
