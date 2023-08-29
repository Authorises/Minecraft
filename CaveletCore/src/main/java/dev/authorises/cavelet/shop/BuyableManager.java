package dev.authorises.cavelet.shop;

import java.util.ArrayList;

public class BuyableManager {
    private ArrayList<Buyable> buyables;

    public BuyableManager() {
        this.buyables = new ArrayList<>();
    }

    public ArrayList<Buyable> getBuyables() {
        return buyables;
    }

    public void setBuyables(ArrayList<Buyable> buyables) {
        this.buyables = buyables;
    }

    public void addBuyable(Buyable b){
        this.buyables.add(b);
    }

    public void removeBuyable(Buyable b){
        this.buyables.remove(b);
    }
}
