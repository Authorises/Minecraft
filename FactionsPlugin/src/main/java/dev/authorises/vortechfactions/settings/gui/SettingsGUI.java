package dev.authorises.vortechfactions.settings.gui;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.settings.*;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.simpleItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.figt.loafmenus.LoafMenu;
import us.figt.loafmenus.LoafMenuItem;
import us.figt.loafmenus.MenuRowSize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsGUI extends LoafMenu {
    private final Integer page;
    private final SettingsCategory category;

    public SettingsGUI(Player p, Integer page, SettingsCategory category){
        super(VortechFactions.loafMenuRegistrar, ColorUtils.format("&cFactions Settings &f|&c Page "+(page)+" &f| "+category.color+category.name), MenuRowSize.SIX, p);
        this.page = page;
        this.category = category;
    }

    @Override
    public LoafMenuItem[] getMenuItems(){
        LoafMenuItem[] array = newLoafMenuItemArray();
        LoafMenuItem backItem = new LoafMenuItem(simpleItemBuilder.build(Material.PAPER, "&cPrevious Page"), (clicker, clickInformation) -> {
            this.close();
            new SettingsGUI(clicker, this.page-1, this.category).open();
            return true;
        });
        LoafMenuItem nextItem = new LoafMenuItem(simpleItemBuilder.build(Material.PAPER, "&cNext Page"), (clicker, clickInformation) -> {
            this.close();
            new SettingsGUI(clicker, this.page+1, this.category).open();
            return true;
        });
        List<Setting> settings = new ArrayList<>();
        if(this.category!=SettingsCategory.ALL) {
            for (Setting lit : VortechFactions.settingsManager.getSettings()) {
                boolean works = false;
                for (SettingsCategory cat : lit.getCategories()) {
                    if (cat.equals(this.category)) {
                        works = true;
                    }
                }
                if (works) {
                    settings.add(lit);
                }
            }
        }else{
            settings.addAll(VortechFactions.settingsManager.getSettings());
        }
        if(settings.size()<=45){
            int x = 0;
            for(Setting it : settings){
                try {
                    if(it instanceof BooleanSetting) {
                        BooleanSetting setting = (BooleanSetting) it;
                        LoafMenuItem litem = new LoafMenuItem(setting.getItem(getPlayer().getUniqueId()), (clicker, clickInformation) -> {
                            if(ClickTypeFinder.getClickType(clickInformation.getType()).equals(ClickSide.LEFT)){
                                try {
                                    setting.setState(getPlayer().getUniqueId(), true);
                                    new SettingsGUI(getPlayer(), this.page, this.category).open();
                                    return true;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return true;
                                }
                            }
                            if(ClickTypeFinder.getClickType(clickInformation.getType()).equals(ClickSide.RIGHT)){
                                try {
                                    setting.setState(getPlayer().getUniqueId(), false);
                                    new SettingsGUI(getPlayer(), this.page, this.category).open();
                                    return true;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return true;
                                }
                            }
                            return true;
                        });
                        array[x]=litem;
                        x+=1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            try {
                ArrayList<SettingsPage> pages = SettingsUtils.generatePages(settings, getPlayer(), this.page, this.category);
                array = pages.get(this.page-1).format(array);
                if(page>1){
                    array[45] = backItem;
                }
                if(pages.size()>this.page){
                    array[53] = nextItem;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        int d = 46;
        for(SettingsCategory cat : SettingsCategory.values()){
            LoafMenuItem catItem = new LoafMenuItem(simpleItemBuilder.build(cat.displayMaterial, "&fCategory: "+cat.color+cat.name), (clicker, clickInformation) -> {
                this.close();
                new SettingsGUI(clicker, 1, cat).open();
                return true;
            });
            array[d] = catItem;
            d+=1;
        }
        replaceAll(array, Material.AIR, new ItemStack(Material.STAINED_GLASS_PANE));
        return array;
    }
}
