package dev.authorises.vortechfactions.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class simpleItemBuilder {
    public static ItemStack build(Material material, String title, String... text){
        ItemStack it = new ItemStack(material);
        ItemMeta itm = it.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        for(String s : text){
            lore.add(ColorUtils.format(s));
        }
        itm.setLore(lore);
        itm.setDisplayName(ColorUtils.format(title));
        it.setItemMeta(itm);
        return it;
    }
}
