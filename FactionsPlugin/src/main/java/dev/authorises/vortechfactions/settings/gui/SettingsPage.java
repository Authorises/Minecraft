package dev.authorises.vortechfactions.settings.gui;

import dev.authorises.vortechfactions.settings.*;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import us.figt.loafmenus.LoafMenuItem;

import java.io.IOException;
import java.util.List;

public class SettingsPage {
    public List<Setting> settings;
    public Player player;
    public Integer page;
    public SettingsCategory category;

    public SettingsPage(List<Setting> settings, Player p, Integer page, SettingsCategory category){
        this.settings = settings;
        this.player = p;
        this.page = page;
        this.category = category;
    }

    public LoafMenuItem[] format(LoafMenuItem[] prevItems) throws InstantiationException, IllegalAccessException {
        int x = 0;
        for(Setting a : this.settings){
            Setting it = a;
            if(it instanceof BooleanSetting){
                BooleanSetting setting = (BooleanSetting) it;
                LoafMenuItem litem = new LoafMenuItem(setting.getItem(player.getUniqueId()), (clicker, clickInformation) -> {
                    if(ClickTypeFinder.getClickType(clickInformation.getType()).equals(ClickSide.LEFT)){
                        try {
                            setting.setState(player.getUniqueId(), true);
                            new SettingsGUI(player, this.page, this.category).open();
                            player.sendMessage(ColorUtils.format("&f(&b!&F) Setting: &b"+setting.getDisplayName()+"&f set to &aEnabled"));
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return true;
                        }
                    }
                    if(ClickTypeFinder.getClickType(clickInformation.getType()).equals(ClickSide.RIGHT)){
                        try {
                            setting.setState(player.getUniqueId(), false);
                            new SettingsGUI(player, this.page, this.category).open();
                            player.sendMessage(ColorUtils.format("&f(&b!&F) Setting: &b"+setting.getDisplayName()+"&f set to &cDisabled"));
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return true;
                        }
                    }
                    return true;
                });
                prevItems[x]=litem;
                x+=1;
            }
        }
        return prevItems;
    }
}
