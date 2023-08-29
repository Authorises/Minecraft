package dev.authorises.vortechfactions.shop.shopitems;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.shop.ShopCategory;
import dev.authorises.vortechfactions.shop.ShopItem;
import dev.authorises.vortechfactions.shop.ShopItemType;
import dev.authorises.vortechfactions.utilities.simpleItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class rabbitSpawnerShopItem implements ShopItem {
    @Override
    public @NotNull String displayName() {
        return "&bRabbit Spawner";
    }

    @Override
    public @NotNull Float priceEach() {
        return 1500000F;
    }

    @Override
    public @NotNull ShopItemType type() {
        return ShopItemType.ITEM;
    }

    @Override
    public @NotNull ItemStack getDispayItem() {
        return simpleItemBuilder.build(getMaterial(), "&bRabbit Spawner", "", "&bPrice e/a: &f$"+priceEach());
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
        return Material.MOB_SPAWNER;
    }

    @Override
    public void purchase(Player p, int amt) {
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
        VortechFactions.econ.withdrawPlayer(p, amt*priceEach());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ws give -s "+p.getName()+" spawner rabbit "+amt);
    }

    @Override
    public @NotNull ArrayList<ShopCategory> getCategories() {
        ArrayList<ShopCategory> categories = new ArrayList<>();
        categories.add(ShopCategory.SPAWNER);
        return categories;
    }
}
