package dev.authorises.vortechfactions.settings;

import dev.authorises.vortechfactions.settings.gui.SettingsPage;
import dev.authorises.vortechfactions.shop.ShopCategory;
import dev.authorises.vortechfactions.shop.ShopItem;
import dev.authorises.vortechfactions.shop.ShopPage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SettingsUtils {

    public static SettingsCategory categoryFromString(String s){
        SettingsCategory category = SettingsCategory.ALL;
        for(SettingsCategory cat : SettingsCategory.values()){
            if(cat.name.equalsIgnoreCase(s)){
                category = cat;
            }
        }
        return category;
    }

    public static ArrayList<SettingsPage> generatePages(List<Setting> settings, Player p, Integer page, SettingsCategory category){
            List<Setting> settingsLeft = new ArrayList<>(settings);
            ArrayList<SettingsPage> pages = new ArrayList<>();
            while(settingsLeft.size() > 0){
                int x = 0;
                SettingsPage settingsPage = new SettingsPage(new ArrayList<>(), p, page, category);
                while (x<45){
                    try {
                        Setting c = settingsLeft.get(0);
                        x += 1;
                        settingsPage.settings.add(c);
                        settingsLeft.remove(c);
                    }catch (Exception e){
                        e.printStackTrace();
                        x=50;
                    }
                }
                pages.add(settingsPage);
            }
            return pages;
    }
}
