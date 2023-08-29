package dev.authorises.cavelet.customitems;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.utils.ItemBuilder;
import dev.authorises.cavelet.utils.LoreChunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class ArmourCustomItem {
    private Rarity rarity;
    private String id;
    private String displayName;
    private String armourColor;
    private String description;
    public Double armour;
    public Double armourToughness;
    public ArmourType type;
    public CustomEnchantType[] allowedEnchantTypes;
    public String[] possibleForges;
    public Integer pointsValue;
    public boolean stackable; // kind of irrelevant here but may as well keep it for weird items that can be helmets
    public boolean enchantable;

    public ArmourCustomItem(Rarity rarity, String id, String displayName, String armourColor, String description, Double armour, Double armourToughness, Double maxHealth, boolean stackable, ArmourType type, boolean enchantable, Integer pointsValue, CustomEnchantType... allowedEnchantments){
        this.rarity = rarity;
        this.id = id;
        this.displayName = displayName;
        this.armourColor = armourColor;
        this.description = description;
        this.armour = armour;
        this.armourToughness = armourToughness;
        this.stackable = stackable;
        this.type = type;
        this.enchantable = enchantable;
        this.allowedEnchantTypes = allowedEnchantments;
        this.pointsValue = pointsValue;
    }

    public ArmourCustomItem setPossibleForges(String... possibleForges){
        this.possibleForges = possibleForges;
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
        Material m = null;
        switch (type){
            case BOOTS -> m = Material.LEATHER_BOOTS;
            case LEGGINGS -> m = Material.LEATHER_LEGGINGS;
            case CHESTPLATE -> m = Material.LEATHER_CHESTPLATE;
            case HELMET -> m = Material.LEATHER_HELMET;
        }
        Color c = Color.decode(armourColor);
        ItemBuilder is = new ItemBuilder(m)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+getRarity().color+getDisplayName()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+getRarity().displayName+" Item"))
                .setLeatherArmorColor(org.bukkit.Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()));
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
        ItemStack s = nbtItem.getItem();
        ItemMeta sMeta = s.getItemMeta();
        sMeta.addItemFlags(ItemFlag.HIDE_DYE);
        sMeta.setUnbreakable(true);
        sMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armourToughness", armourToughness, AttributeModifier.Operation.ADD_NUMBER, type.slotType));
        sMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", armour, AttributeModifier.Operation.ADD_NUMBER, type.slotType));
        s.setItemMeta(sMeta);
        return s;
    }


    public ArmourType getType() {
        return type;
    }


    public Double getArmour() {
        return armour;
    }

    public Double getArmourToughness() {
        return armourToughness;
    }


    public String getArmourColor() {
        return armourColor;
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

    public String getDescription() {
        return description;
    }

    public boolean isStackable() {
        return stackable;
    }
}
