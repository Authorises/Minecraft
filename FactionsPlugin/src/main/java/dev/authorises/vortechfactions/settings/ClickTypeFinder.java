package dev.authorises.vortechfactions.settings;

import org.bukkit.event.inventory.ClickType;

public class ClickTypeFinder {

    public static ClickSide getClickType(ClickType type){
        if(type.isRightClick()){
            return ClickSide.RIGHT;
        }
        return ClickSide.LEFT;
    }


}
