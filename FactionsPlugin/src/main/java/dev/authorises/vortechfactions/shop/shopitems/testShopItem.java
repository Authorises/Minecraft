package dev.authorises.vortechfactions.shop.shopitems;

import dev.authorises.vortechfactions.shop.ShopCategory;
import dev.authorises.vortechfactions.shop.ShopItem;
import dev.authorises.vortechfactions.shop.ShopItemType;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class testShopItem implements ShopItem {

    @Override
    public @NotNull String displayName() {
        return "&bTest&fItem";
    }

    @Override
    public @NotNull Float priceEach() {
        return 100F;
    }

    @Override
    public @NotNull ShopItemType type() {
        return ShopItemType.COMMAND;
    }

    @Override
    public @NotNull ItemStack getDispayItem() {
        return new ItemStack(Material.IRON_AXE);
    }

    @Override
    public boolean canPurchaseMany() {
        return true;
    }

    @Override
    public @NotNull Integer maxPurchaseMany() {
        return 100;
    }

    @Override
    public @NotNull Material getMaterial() {
        return null;
    }

    @Override
    public void purchase(Player p, int amt) {
        p.sendMessage(ColorUtils.format("&dAmount purchased: &f"+amt));
    }

    @Override
    public @NotNull ArrayList<ShopCategory> getCategories() {
        ArrayList<ShopCategory> categories = new ArrayList<>();
        categories.add(ShopCategory.MISC);
        return categories;
    }
}
