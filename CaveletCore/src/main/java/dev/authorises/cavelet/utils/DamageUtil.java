package dev.authorises.cavelet.utils;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DamageUtil {

    public static ItemStack updateDamageLore(ItemStack s){
        try {
            ItemStack is = s.asOne();
            NBTItem i = new NBTItem(s);
            ItemMeta sMeta = s.getItemMeta();
            List<Component> lore = sMeta.lore();

            int x = 0;
            for(Component c : lore){
                if(Cavelet.miniMessage.serialize(c).contains("<#9eb5db>Item Health:")){
                    lore.set(x, Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Item Health: <#19b6e6>"+(i.getDouble("max_health")-i.getDouble("damage"))));
                }
                x+=1;
            }

            sMeta.lore(lore);
            is.setItemMeta(sMeta);
            return is;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
