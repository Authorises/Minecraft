package dev.authorises.cavelet.customitems;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.utils.ItemBuilder;
import dev.authorises.cavelet.utils.LoreChunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.UUID;

public class BasicCustomItem {
    private Rarity rarity;
    private String id;
    private String displayName;
    private Material itemMaterial;
    private String description;
    public CustomEnchantType[] possibleEnchants;
    public String[] possibleForges;
    public boolean stackable;
    public boolean enchantable;
    public Integer pointsValue;

    public BasicCustomItem(Rarity rarity, String id, String displayName, Material itemMaterial, String description, boolean stackable, boolean enchantable, CustomEnchantType... possibleEnchantments){
        this.rarity = rarity;
        this.id = id;
        this.displayName = displayName;
        this.itemMaterial = itemMaterial;
        this.description = description;
        this.stackable = stackable;
        this.enchantable = enchantable;
        this.possibleEnchants = possibleEnchantments;
    }

    public BasicCustomItem setPossibleForges(String... possibleForges){
        this.possibleForges = possibleForges;
        return this;
    }

    public BasicCustomItem setPossibleEnchants(CustomEnchantType... possibleEnchants){
        this.possibleEnchants = possibleEnchants;
        return this;
    }

    public String[] getPossibleForges(){
        return possibleForges;
    }

    public Component getDisplayNameFull(){
        ItemStack i = getItem();
        Component c = getDisplayNameBasic();
        Component lore = Component.empty();
        lore = lore.append(getDisplayNameBasic());
        lore = lore.append(Component.newline());
        for(Component s : i.getItemMeta().lore()){
            lore = lore.append(s);
            lore = lore.append(Component.newline());
        }
        c=c.hoverEvent(HoverEvent.showText(lore));
        return c;
    }

    public Component getDisplayNameBasic(){
        return Cavelet.miniMessage.deserialize(getRarity().color+getDisplayName());
    }

    public ItemStack getItem(){
        ItemBuilder is = new ItemBuilder(getItemMaterial())
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+getRarity().color+getDisplayName()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+getRarity().displayName+" Item"));
        if(getDescription()!=""){
            is=is.addLoreLine("");
            List<String> descriptionChunks = LoreChunk.chunk(getDescription(), 60, "<!italic><#9eb5db>");
            for(String s : descriptionChunks){
                is=is.addComponentLoreLine(Cavelet.miniMessage.deserialize(s));
            }
        }
        NBTItem nbtItem = new NBTItem(is.toItemStack());
        nbtItem.setString("item_id", getId());
        if(!stackable){
            nbtItem.setUUID("random", UUID.randomUUID());
        }
        return nbtItem.getItem();
    }

    public ItemStack getItem(int amount){
        ItemBuilder is = new ItemBuilder(getItemMaterial(), amount)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+getRarity().color+getDisplayName()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+getRarity().displayName+" Item"));
        if(getDescription()!=""){
            is=is.addLoreLine("");
            List<String> descriptionChunks = LoreChunk.chunk(getDescription(), 60, "<!italic><#9eb5db>");
            for(String s : descriptionChunks){
                is=is.addComponentLoreLine(Cavelet.miniMessage.deserialize(s));
            }
        }
        NBTItem nbtItem = new NBTItem(is.toItemStack());
        nbtItem.setString("item_id", getId());
        if(!stackable){
            nbtItem.setUUID("random", UUID.randomUUID());
        }
        return nbtItem.getItem();
    }

    public JSONObject toJson(){
        JSONObject o = new JSONObject();
        o.put("display-name", displayName);
        o.put("item-material", itemMaterial);
        o.put("description", description);
        o.put("stackable", stackable);
        return o;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public String getDescription() {
        return description;
    }

    public boolean isStackable() {
        return stackable;
    }
}
