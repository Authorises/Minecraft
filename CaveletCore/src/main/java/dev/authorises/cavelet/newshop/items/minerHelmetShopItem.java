package dev.authorises.cavelet.newshop.items;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.newshop.ShopCategory;
import dev.authorises.cavelet.newshop.ShopItem;
import dev.authorises.cavelet.newshop.ShopItemType;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class minerHelmetShopItem implements ShopItem {

    @Override
    public @NotNull String displayName() {
        return "&bMiner Helmet";
    }

    @Override
    public @NotNull double priceEach() {
        return 400D;
    }

    @Override
    public @NotNull ShopItemType type() {
        return ShopItemType.ITEM;
    }

    @Override
    public @NotNull ItemStack getDispayItem() {
        return new ItemBuilder(new CItem(Cavelet.customItemsManager.getItemById("MINER_HELMET")).getItemStack())
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Miner Helmet"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Price e/a: <#19b6e6>$"+priceEach()))
                .toItemStack();
    }

    @Override
    public boolean canPurchaseMany() {
        return true;
    }

    @Override
    public @NotNull Integer maxPurchaseMany() {
        return 2304;
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.LEATHER_HELMET;
    }

    @Override
    public int maxSlotSize(){
        return 1;
    }

    @Override
    public void purchase(Player p, double amt) {
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        mp.setBalance(mp.getBalance()-(amt*priceEach()));

        CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("MINER_HELMET"));

        while (amt > 0){
            p.getInventory().addItem(cItem.getItemStack());
            amt-=1;
        }
    }

    @Override
    public @NotNull ArrayList<ShopCategory> getCategories() {
        ArrayList<ShopCategory> categories = new ArrayList<>();
        categories.add(ShopCategory.COMBAT);

        return categories;
    }

}
