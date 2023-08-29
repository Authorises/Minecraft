package dev.authorises.vortechfactions.shop;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.TitleUtils;
import dev.authorises.vortechfactions.utilities.simpleItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.figt.loafmenus.LoafMenu;
import us.figt.loafmenus.LoafMenuItem;
import us.figt.loafmenus.MenuRowSize;

import java.util.ArrayList;
import java.util.List;


public class shopManager extends LoafMenu {
    private final Integer page;
    private final ShopCategory category;
    public shopManager(Player p, Integer page, ShopCategory category){
        super(VortechFactions.loafMenuRegistrar, ColorUtils.format("&cFactions Shop &f|&c Page "+(page)+" &f| "+category.color+category.name), MenuRowSize.SIX, p);
        this.category = category;
        this.page = page;
    }

    @Override
    public LoafMenuItem[] getMenuItems(){
        LoafMenuItem[] array = newLoafMenuItemArray();
        LoafMenuItem backItem = new LoafMenuItem(simpleItemBuilder.build(Material.PAPER, "&cPrevious Page"), (clicker, clickInformation) -> {
            this.close();
            new shopManager(clicker, this.page-1, this.category).open();
            return true;
        });
        LoafMenuItem nextItem = new LoafMenuItem(simpleItemBuilder.build(Material.PAPER, "&cNext Page"), (clicker, clickInformation) -> {
            this.close();
            new shopManager(clicker, this.page+1, this.category).open();
            return true;
        });
        List<ShopItem> items = new ArrayList<>();
        if(this.category!=ShopCategory.ALL) {
            for (ShopItem lit : VortechFactions.shopItems) {
                boolean works = false;
                for (ShopCategory cat : lit.getCategories()) {
                    if (cat.equals(this.category)) {
                        works = true;
                    }
                }
                if (works) {
                    items.add(lit);
                }
            }
        }else{
            items.addAll(VortechFactions.shopItems);
        }
        //for()
        if(items.size()<=45){
            int x = 0;
            for(ShopItem it : items){
                try {
                    LoafMenuItem litem = new LoafMenuItem(it.getDispayItem(), (clicker, clickInformation) -> {
                        if(it.canPurchaseMany()){
                            new AnvilGUI.Builder()
                                    .onComplete((player, text) -> {
                                        try{
                                            int amt = Integer.parseInt(text);
                                            if(amt>it.maxPurchaseMany() || amt<1){
                                                return AnvilGUI.Response.text("Over the maximum");
                                            }else{
                                                if(it.priceEach()*amt>VortechFactions.econ.getBalance(player)){
                                                    clicker.sendMessage(ColorUtils.format("&f(&b!&f) You cannot afford to buy that many of that item!"));
                                                    TitleUtils.sendTitle(player, 10, 10, 10, "&c&nPurchase Failed", "");
                                                }else {
                                                    it.purchase(clicker, amt);
                                                    clicker.sendMessage(ColorUtils.format("&f(&b!&f) You purchased &b&n" + amt + "&f items."));
                                                    TitleUtils.sendTitle(player, 10, 10, 10, "&bPurchased", "&f&nPurchased " + amt + " items");
                                                }
                                                return AnvilGUI.Response.close();
                                            }
                                        }catch (NumberFormatException e){
                                            return AnvilGUI.Response.text("Invalid number");
                                        }
                                    })
                                    .text("Enter amount.")
                                    .itemLeft(new ItemStack(Material.GOLD_HELMET))
                                    .itemRight(simpleItemBuilder.build(Material.GOLD_HELMET,"&bInformation", "&dMaximum purchase amount ( Per purchase ): &f"+it.maxPurchaseMany(), "", "&f&nClick to return to previous menu."))
                                    .onRightInputClick(player -> player.performCommand("shop"))
                                    .plugin(VortechFactions.getProvidingPlugin(VortechFactions.class))
                                    .open(clicker);
                        }else {
                            it.purchase(clicker, 1);
                        }
                       return true;
                    });
                    array[x]=litem;
                    x+=1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            try {
                ArrayList<ShopPage> pages = ShopUtils.genPages(items);
                array = pages.get(this.page-1).format(array);
                if(page>1){
                    array[45] = backItem;
                }
                if(pages.size()>this.page){
                    array[53] = nextItem;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        int d = 46;
        for(ShopCategory cat : ShopCategory.values()){
            LoafMenuItem catItem = new LoafMenuItem(simpleItemBuilder.build(cat.item, "&fCategory: "+cat.color+cat.name), (clicker, clickInformation) -> {
                this.close();
                new shopManager(clicker, 1, cat).open();
                return true;
            });
            array[d] = catItem;
            d+=1;
        }
        replaceAll(array, Material.AIR, new ItemStack(Material.STAINED_GLASS_PANE));
        return array;
    }
}
