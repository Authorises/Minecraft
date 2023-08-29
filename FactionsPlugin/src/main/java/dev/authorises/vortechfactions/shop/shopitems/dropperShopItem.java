package dev.authorises.vortechfactions.shop.shopitems;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.shop.ShopCategory;
import dev.authorises.vortechfactions.shop.ShopItem;
import dev.authorises.vortechfactions.shop.ShopItemType;
import dev.authorises.vortechfactions.utilities.simpleItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class dropperShopItem implements ShopItem {
    @Override
    public @NotNull String displayName() {
        return "&bDropper";
    }

    @Override
    public @NotNull Float priceEach() {
        return Items.materialBuyValues.get(getMaterial());
    }

    @Override
    public @NotNull ShopItemType type() {
        return ShopItemType.ITEM;
    }

    @Override
    public @NotNull ItemStack getDispayItem() {
        return simpleItemBuilder.build(getMaterial(), "&bDropper", "", "&bPrice e/a: &f$"+priceEach());
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
        return Material.DROPPER;
    }

    @Override
    public void purchase(Player p, int amt) {
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
        VortechFactions.econ.withdrawPlayer(p, amt*priceEach());
        while (amt > 0){
            p.getInventory().addItem(new ItemStack(getMaterial()));
            amt-=1;
        }
    }

    @Override
    public @NotNull ArrayList<ShopCategory> getCategories() {
        ArrayList<ShopCategory> categories = new ArrayList<>();
        categories.add(ShopCategory.RAID);
        return categories;
    }
}
