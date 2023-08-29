package dev.authorises.cavelet.newshop;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class NewShopManager {

    public final List<ShopItem> shopItems = new ArrayList<>();

    public NewShopManager(){

        Set<Class<? extends ShopItem>> shopItems1 = new Reflections("dev.authorises.cavelet.newshop.*")
                .getSubTypesOf(ShopItem.class);

        for(Class<? extends ShopItem> c : shopItems1){
            try {
                shopItems.add(c.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        shopItems.sort(new Comparator<ShopItem>() {
            @Override
            public int compare(final ShopItem object1, final ShopItem object2) {
                return object1.displayName().compareTo(object2.displayName());
            }
        });
    }

}
