package dev.authorises.cavelet.shop;

import java.util.ArrayList;

public class CustomItemShopManager {
    private ArrayList<CustomItemShop> shops;

    public CustomItemShopManager(){
        this.shops = new ArrayList<>();
    }

    public ArrayList<CustomItemShop> getShops(){
        return this.shops;
    }

    public void addShop(CustomItemShop shop){
        shops.add(shop);
    }

    public void removeShop(CustomItemShop shop){
        shops.remove(shop);
    }
}
