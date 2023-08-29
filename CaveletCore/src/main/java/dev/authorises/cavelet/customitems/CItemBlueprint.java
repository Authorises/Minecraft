package dev.authorises.cavelet.customitems;

import dev.authorises.cavelet.customenchants.CustomEnchantType;
import org.json.simple.JSONObject;

import java.util.List;

public class CItemBlueprint<T> {
    private T item;
    private String id;
    private Rarity rarity;
    private List<CustomEnchantType> allowedEnchantments;
    private List<String> allowedForgeUpgrades;
    private Boolean enchantable;
    private Integer pointsValue;
    private Boolean stackable;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public Boolean getEnchantable() {
        return enchantable;
    }

    public void setEnchantable(Boolean enchantable) {
        this.enchantable = enchantable;
    }

    public Boolean getStackable() {
        return stackable;
    }

    public void setStackable(Boolean stackable) {
        this.stackable = stackable;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public JSONObject toJson(){
        JSONObject o = new JSONObject();
        o.put("id", getId());
        o.put("rarity", getRarity().id);
        return o;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public List<CustomEnchantType> getAllowedEnchantments() {
        return allowedEnchantments;
    }

    public void setAllowedEnchantments(List<CustomEnchantType> allowedEnchantments) {
        this.allowedEnchantments = allowedEnchantments;
    }

    public List<String> getAllowedForgeUpgrades() {
        return allowedForgeUpgrades;
    }

    public void setAllowedForgeUpgrades(List<String> allowedForgeUpgrades) {
        this.allowedForgeUpgrades = allowedForgeUpgrades;
    }

    public Integer getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(Integer pointsValue) {
        this.pointsValue = pointsValue;
    }
}
