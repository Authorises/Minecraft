package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.newshop.ShopCategory;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.InventoryUtil;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CollectGUI {

    private MProfile profile;
    private Player player;
    private ChestGui gui;

    private void update(int page){
        gui.getPanes().removeAll(gui.getPanes());
        OutlinePane background = new OutlinePane(0, 0, 9, 6);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        PaginatedPane claims = new PaginatedPane(1, 1, 7, 4);
        ArrayList<GuiItem> guiItems = new ArrayList<>();
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        for(String s : profile.claims){
            CItemBlueprint<?> blueprint = Cavelet.customItemsManager.getItemById(s);
            CItem cItem = new CItem(blueprint);

            ItemBuilder builder = new ItemBuilder(cItem.getItemStack())
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left click to collect this item"));

            guiItems.add(new GuiItem(builder.toItemStack(), click -> {
                if(player.getInventory().firstEmpty()!=-1){
                    player.getInventory().addItem(cItem.getItemStack());
                    profile.claims.remove(s);
                    if(profile.claims.size()>0){
                        update(0);
                    }else{
                        player.closeInventory();
                    }
                }else{
                    player.sendMessage(ColorUtils.format("&cYour inventory is full!"));
                }
            }));
        }
        claims.populateWithGuiItems(guiItems);

        OutlinePane claimAll = new OutlinePane(4, 5, 1, 1);
        if(profile.claims.size()>=2){
            claimAll.addItem(new GuiItem(new ItemBuilder(Material.CAULDRON)
                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Collect all"))
                    .toItemStack(), click -> {
                if(InventoryUtil.freeSlots(player)>=1){
                    for (int i = Math.min(InventoryUtil.freeSlots(player), profile.claims.size());i>0;i-=1){
                        String s = profile.claims.get(0);
                        CItem cItem = new CItem(Cavelet.customItemsManager.getItemById(s));
                        player.getInventory().addItem(cItem.getItemStack());
                        profile.claims.remove(s);
                    }
                    if(profile.claims.size()>0){
                        update(0);
                    }else{
                        player.closeInventory();
                    }
                }else{
                    player.sendMessage(ColorUtils.format("&cYou do not have enough inventory space to claim all rewards."));
                }
            }));
        }

        if(profile.claims.size()>=1){
            claims.setPage(page);
        }


        OutlinePane nextPage = new OutlinePane(8, 5, 1, 1);
        nextPage.addItem(new GuiItem(new ItemBuilder(Material.ARROW)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Next page"))
                .toItemStack(), click -> {
            claims.setPage(claims.getPage()+1);
            update(claims.getPage());

        }));

        OutlinePane previousPage = new OutlinePane(0, 5, 1, 1);
        previousPage.addItem(new GuiItem(new ItemBuilder(Material.ARROW)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Previous page"))
                .toItemStack(), click -> {
            claims.setPage(claims.getPage()-1);
            update(claims.getPage());
        }));

        if (claims.getPage() < claims.getPages()-1){
            gui.addPane(nextPage);
        }

        if(claims.getPage()>0){
            gui.addPane(previousPage);
        }

        gui.addPane(background);
        gui.addPane(claims);
        gui.addPane(claimAll);
        gui.update();
    }

    public CollectGUI(Player player){
        this.player = player;
        this.profile = Cavelet.cachedMPlayers.get(player.getUniqueId());
        gui = new ChestGui(6, ColorUtils.format("&dCollect rewards"));
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        update(0);

        gui.show(player);
    }

}
