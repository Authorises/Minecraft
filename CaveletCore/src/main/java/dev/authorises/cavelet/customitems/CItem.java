package dev.authorises.cavelet.customitems;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;

import dev.authorises.cavelet.forge.ForgeUpgrade;
import dev.authorises.cavelet.forge.ForgeUpgradeManager;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.LoreChunk;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CItem {
    private Boolean canEnchant;
    private ArrayList<CustomEnchant> enchantments;
    private ArrayList<ForgeUpgrade> forgeUpgrades;
    private CItemBlueprint<?> blueprint;
    public ItemStack item;
    public String displayName;
    public Integer pointsValue;

    public CItem(ItemStack itemStack) throws InvalidItemIdException{

            this.item = itemStack;
            this.blueprint = Cavelet.customItemsManager.getItemById(new NBTItem(this.item).getString("item_id"));
            if(this.blueprint == null){
                throw new InvalidItemIdException("Item does not have a valid ITEM_ID");
            }
            this.enchantments = new ArrayList<>();
            this.forgeUpgrades = new ArrayList<>();
            if(this.blueprint.getPointsValue()==null) this.pointsValue = 0;

            else{
                this.pointsValue = this.blueprint.getPointsValue();
            }

            this.loadInformation();
    }

    public CItem(CItemBlueprint<?> cItemBlueprint){
        this.blueprint = cItemBlueprint;
        this.enchantments = new ArrayList<>();
        this.forgeUpgrades = new ArrayList<>();
        this.pointsValue = 0;
        if(getBlueprint().getItem() instanceof BasicCustomItem){
            BasicCustomItem b = (BasicCustomItem) getBlueprint().getItem();
            this.item = b.getItem();
            this.displayName = Cavelet.miniMessage.serialize(b.getDisplayNameBasic());
        }
        else if(getBlueprint().getItem() instanceof ArmourCustomItem){
            ArmourCustomItem b = (ArmourCustomItem) getBlueprint().getItem();
            this.item = b.getItem();
            this.displayName = b.getRarity().color+b.getDisplayName();
            this.pointsValue = b.pointsValue;
            if(this.pointsValue==null) this.pointsValue = 0;
        }
        else if(getBlueprint().getItem() instanceof IntelligentItem){
            IntelligentItem b = (IntelligentItem) getBlueprint().getItem();
            this.item = b.getItem();
            this.displayName = b.rarity.color+b.displayName;
            this.pointsValue = b.pointsValue;
            if(this.pointsValue==null) this.pointsValue = 0;
        }
        this.loadInformation();
    }

    public CItem(CItemBlueprint<?> cItemBlueprint, int amount){
        this.blueprint = cItemBlueprint;
        this.enchantments = new ArrayList<>();
        this.forgeUpgrades = new ArrayList<>();
        this.pointsValue = 0;
        if(getBlueprint().getItem() instanceof BasicCustomItem){
            BasicCustomItem b = (BasicCustomItem) getBlueprint().getItem();
            this.item = b.getItem(amount);
            this.displayName = Cavelet.miniMessage.serialize(b.getDisplayNameBasic());
        }
        else if(getBlueprint().getItem() instanceof ArmourCustomItem){
            ArmourCustomItem b = (ArmourCustomItem) getBlueprint().getItem();
            this.item = b.getItem();
            this.displayName = b.getRarity().color+b.getDisplayName();
            this.pointsValue = b.pointsValue;
            if(this.pointsValue==null) this.pointsValue = 0;
        }
        else if(getBlueprint().getItem() instanceof IntelligentItem){
            IntelligentItem b = (IntelligentItem) getBlueprint().getItem();
            this.item = b.getItem();
            this.displayName = b.rarity.color+b.displayName;
            this.pointsValue = b.pointsValue;
            if(this.pointsValue==null) this.pointsValue = 0;
        }
        this.loadInformation();
    }

    public ArrayList<CustomEnchant> getCustomEnchants(){
        return this.enchantments;
    }

    public void addEnchantment(CustomEnchant enchant){
        this.enchantments.removeIf(loopEnchant -> loopEnchant.getType().equals(enchant.getType()));
        this.enchantments.add(enchant);
    }

    public void loadInformation(){
        NBTItem i = new NBTItem(this.item);
        if(i.hasKey("enchantments")){
            for(String s : (ArrayList<String>) i.getObject("enchantments", ArrayList.class)){
                this.enchantments.add(new CustomEnchant(s));
            }
        }
        if(i.hasKey("forge-upgrades")){
            for(String s : (ArrayList<String>) i.getObject("forge-upgrades", ArrayList.class)){
                this.forgeUpgrades.add(Cavelet.forgeUpgradeManager.getForge(s));
            }
        }
    }

    public CItemBlueprint<?> getBlueprint(){
        return this.blueprint;
    }

    public ArrayList<ForgeUpgrade> getForgeUpgrades() {
        return forgeUpgrades;
    }


    public ItemStack getItemStack(){
        if(getBlueprint().getItem() instanceof BasicCustomItem){
            ItemStack item = this.item.clone();
            String enchantsLine = "";
            if(getCustomEnchants().size()>0){
                for(CustomEnchant customEnchant : getCustomEnchants()){
                    customEnchant.applyVanillaCounterparts(item);
                    enchantsLine+=customEnchant.getLoreSafeDisplayString();
                    if((getCustomEnchants().indexOf(customEnchant)+1)<getCustomEnchants().size()){
                        enchantsLine+="<gray>, ";
                    }
                }
            }
            ItemMeta meta = item.getItemMeta();
            List<Component> lore = meta.lore();
            ArrayList<String> toAdd = new ArrayList<>(LoreChunk.chunk(enchantsLine, 110, "<!italic><bold><white>|<reset><!italic> "));
            toAdd.replaceAll(t -> t.replaceAll("%space%", " "));
            lore.removeIf(c -> Cavelet.miniMessage.serialize(c).contains("<bold><white>|"));
            if(getCustomEnchants().size()>0) {
                lore.addAll(toAdd.stream().map(string -> Cavelet.miniMessage.deserialize(string)).collect(Collectors.toList()));
            }
            meta.lore(lore);
            meta.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
            item.setItemMeta(meta);
            NBTItem nbtItem = new NBTItem(item);
            ArrayList<String> enchantStrings = new ArrayList<>();
            for(CustomEnchant e : getCustomEnchants()){
                enchantStrings.add(e.getEnchantString());
            }
            nbtItem.setObject("enchantments", enchantStrings);

            ArrayList<String> forgeUpgradeStrings = new ArrayList<>();
            for(ForgeUpgrade e : getForgeUpgrades()){
                forgeUpgradeStrings.add(e.id);
            }
            nbtItem.setObject("forge-upgrades", forgeUpgradeStrings);
            return nbtItem.getItem();
        }
        else if(getBlueprint().getItem() instanceof ArmourCustomItem){
            ItemStack item = this.item.clone();
            String enchantsLine = "";
            if(getCustomEnchants().size()>0){
                for(CustomEnchant customEnchant : getCustomEnchants()){
                    customEnchant.applyVanillaCounterparts(item);
                    enchantsLine+=customEnchant.getLoreSafeDisplayString();
                    if((getCustomEnchants().indexOf(customEnchant)+1)<getCustomEnchants().size()){
                        enchantsLine+="<gray>, ";
                    }
                }
            }
            ItemMeta meta = item.getItemMeta();
            List<Component> lore = meta.lore();
            ArrayList<String> toAdd = new ArrayList<>(LoreChunk.chunk(enchantsLine, 110, "<!italic><bold><white>|<reset><!italic> "));
            toAdd.replaceAll(t -> t.replaceAll("%space%", " "));
            lore.removeIf(c -> Cavelet.miniMessage.serialize(c).contains("<bold><white>|"));
            if(getCustomEnchants().size()>0) {
                lore.addAll(toAdd.stream().map(string -> Cavelet.miniMessage.deserialize(string)).collect(Collectors.toList()));
            }
            meta.lore(lore);
            meta.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
            item.setItemMeta(meta);
            NBTItem nbtItem = new NBTItem(item);
            ArrayList<String> enchantStrings = new ArrayList<>();
            for(CustomEnchant e : getCustomEnchants()){
                enchantStrings.add(e.getEnchantString());
            }
            nbtItem.setObject("enchantments", enchantStrings);

            ArrayList<String> forgeUpgradeStrings = new ArrayList<>();
            for(ForgeUpgrade e : getForgeUpgrades()){
                forgeUpgradeStrings.add(e.id);
            }
            nbtItem.setObject("forge-upgrades", forgeUpgradeStrings);
            return nbtItem.getItem();
        }
        else if(getBlueprint().getItem() instanceof IntelligentItem){
            ItemStack item = this.item.clone();
            String enchantsLine = "";
            if(getCustomEnchants().size()>0){
                for(CustomEnchant customEnchant : getCustomEnchants()){
                    customEnchant.applyVanillaCounterparts(item);
                    enchantsLine+=customEnchant.getLoreSafeDisplayString();
                    if((getCustomEnchants().indexOf(customEnchant)+1)<getCustomEnchants().size()){
                        enchantsLine+="<gray>, ";
                    }
                }
            }
            ItemMeta meta = item.getItemMeta();
            List<Component> lore = meta.lore();
            ArrayList<String> toAdd = new ArrayList<>(LoreChunk.chunk(enchantsLine, 110, "<!italic><bold><white>|<reset><!italic> "));
            toAdd.replaceAll(t -> t.replaceAll("%space%", " "));
            lore.removeIf(c -> Cavelet.miniMessage.serialize(c).contains("<bold><white>|"));
            if(getCustomEnchants().size()>0) {
                lore.addAll(toAdd.stream().map(string -> Cavelet.miniMessage.deserialize(string)).collect(Collectors.toList()));
            }
            meta.lore(lore);
            meta.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS);
            item.setItemMeta(meta);
            NBTItem nbtItem = new NBTItem(item);
            ArrayList<String> enchantStrings = new ArrayList<>();
            for(CustomEnchant e : getCustomEnchants()){
                enchantStrings.add(e.getEnchantString());
            }
            nbtItem.setObject("enchantments", enchantStrings);

            ArrayList<String> forgeUpgradeStrings = new ArrayList<>();
            for(ForgeUpgrade e : getForgeUpgrades()){
                forgeUpgradeStrings.add(e.id);
            }
            nbtItem.setObject("forge-upgrades", forgeUpgradeStrings);
            return nbtItem.getItem();
        }
        return null;
    }
}
