package dev.authorises.vortechfactions.shop;

import java.util.ArrayList;
import java.util.List;

public class ShopUtils {
    public static ShopCategory categoryFromString(String s){
        ShopCategory category = ShopCategory.ALL;
        for(ShopCategory cat : ShopCategory.values()){
            if(cat.name.equalsIgnoreCase(s)){
                category = cat;
            }
        }
        return category;
    }

    public static ArrayList<ShopPage> genPages(List<ShopItem> shopItems){
        List<ShopItem> itemsLeft = new ArrayList<>(shopItems);
        ArrayList<ShopPage> pages = new ArrayList<>();
        while(itemsLeft.size() > 0){
            int x = 0;
            ShopPage page = new ShopPage(new ArrayList<>());
            while (x<45){
                try {
                    ShopItem c = itemsLeft.get(0);
                    x += 1;
                    page.shopItems.add(c);
                    itemsLeft.remove(c);
                }catch (Exception e){
                    e.printStackTrace();
                    x=50;
                }
            }
            pages.add(page);
        }
        return pages;
    }
}
