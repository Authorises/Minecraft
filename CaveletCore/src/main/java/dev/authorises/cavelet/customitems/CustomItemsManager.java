package dev.authorises.cavelet.customitems;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItemFunctions;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItemListeners;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CustomItemsManager {

    private ArrayList<CItemBlueprint<?>> customItems;
    public HashMap<String, CItemBlueprint<?>> customItemHashMap;
    public HashMap<String, IntelligentItem> intelligentItemsHashMap;
    private IntelligentItemListeners listeners;

    public void init() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.customItems = new ArrayList<>();
        this.customItemHashMap = new HashMap<>();
        this.intelligentItemsHashMap = new HashMap<>();

        Set<Class<? extends IntelligentItem>> intelligentItems = new Reflections("dev.authorises.cavelet.")
                .getSubTypesOf(IntelligentItem.class);
        for(Class<? extends IntelligentItem> c : intelligentItems){
            IntelligentItem i = c.getConstructor().newInstance();
            CItemBlueprint<IntelligentItem> cItemBlueprint = new CItemBlueprint<IntelligentItem>();
            cItemBlueprint.setItem(i);
            cItemBlueprint.setRarity(i.rarity);
            cItemBlueprint.setId(i.id);
            cItemBlueprint.setPointsValue(i.pointsValue);
            cItemBlueprint.setStackable(i.stackable);
            cItemBlueprint.setEnchantable(i.enchantable);
            addCustomItem(cItemBlueprint);
            customItemHashMap.put(cItemBlueprint.getId(), cItemBlueprint);
            intelligentItemsHashMap.put(i.id, i);
        }

        listeners = new IntelligentItemListeners(intelligentItemsHashMap);
        Bukkit.getPluginManager().registerEvents(listeners, Cavelet.getPlugin(Cavelet.class));

    }

    public CItemBlueprint<?> getItemByItemStack(ItemStack is){
        NBTItem nbtItem = new NBTItem(is);
        if(nbtItem.hasKey("item_id")){
            try {
                CItemBlueprint searchCustomItem = getItemById(nbtItem.getString("item_id"));
                if (searchCustomItem != null) {
                    return searchCustomItem;
                }
            }catch (NullPointerException e){

            }
        }
        return null;
    }

    public CItemBlueprint<?> getItemById(String id){
        return customItemHashMap.get(id);
    }

    public void addCustomItem(CItemBlueprint<?> i){
        this.customItemHashMap.put(i.getId(), i);
        this.customItems.add(i);
    }

    public void removeCustomItem(CItemBlueprint<?> i){
        this.customItemHashMap.remove(i.getId());
        this.customItems.remove(i);
    }

    public ArrayList<CItemBlueprint<?>> getCustomItems(){
        return this.customItems;
    }

    public void setCustomItems(ArrayList<CItemBlueprint<?>> customItems) {
        this.customItems = customItems;
    }
}
