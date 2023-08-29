package dev.authorises.cavelet.utils;

import dev.authorises.cavelet.customitems.ArmourCustomItem;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import net.kyori.adventure.text.Component;

public class ChatUtil {

    public static Component getItemComponent(CItemBlueprint<?> blueprint){
        if(blueprint.getItem() instanceof BasicCustomItem){
            BasicCustomItem b = (BasicCustomItem) blueprint.getItem();
            return b.getDisplayNameFull();
        }
        else if(blueprint.getItem() instanceof ArmourCustomItem){
            ArmourCustomItem b = (ArmourCustomItem) blueprint.getItem();
            return b.getDisplayNameFull();
        }
        else if(blueprint.getItem() instanceof IntelligentItem){
            IntelligentItem b = (IntelligentItem) blueprint.getItem();
            return b.getDisplayNameFull();
        }
        return null;
    }

}
