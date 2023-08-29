package dev.authorises.vortechfactions.shop;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.TitleUtils;
import dev.authorises.vortechfactions.utilities.simpleItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.figt.loafmenus.LoafMenuItem;

import java.util.List;

public class ShopPage {
    public List<ShopItem> shopItems;

    public ShopPage(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public LoafMenuItem[] format(LoafMenuItem[] prevItems) throws InstantiationException, IllegalAccessException {
        int x = 0;
        for(ShopItem a : this.shopItems){
            ShopItem it = a;
            LoafMenuItem litem = new LoafMenuItem(it.getDispayItem(), (clicker, clickInformation) -> {
                if(it.canPurchaseMany()){
                    new AnvilGUI.Builder()
                            .onComplete((player, text) -> {
                                try{
                                    int amt = Integer.parseInt(text);
                                    if(amt>it.maxPurchaseMany() || amt <1){
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
            prevItems[x]=litem;
            x+=1;
        }
        return prevItems;
    }
}