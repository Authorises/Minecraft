package dev.authorises.vortechfactions.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface ShopItem {

    @NotNull
    String displayName();

    @NotNull
    Float priceEach();

    @NotNull
    ShopItemType type();

    @NotNull
    ItemStack getDispayItem();

    boolean canPurchaseMany();

    @NotNull
    Integer maxPurchaseMany();

    @NotNull
    Material getMaterial();

    @NotNull
    ArrayList<ShopCategory> getCategories();

    void purchase(Player p, int amt);
}
