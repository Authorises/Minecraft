package dev.authorises.cavelet.customitems.managers;

import dev.authorises.cavelet.customitems.CustomMobLoot;

import java.util.ArrayList;

public class CustomMobLootManager {
    private ArrayList<CustomMobLoot> customMobLoots;

    public void init(){
        customMobLoots = new ArrayList<>();
    }

    public CustomMobLoot getByName(String name){
        CustomMobLoot l = null;
        for(CustomMobLoot loot : getCustomMobLoots()){
            if(loot.getName().equals(name)){
                return loot;
            }
        }
        return l;
    }

    public void addCustomMobLoot(CustomMobLoot l){
        this.customMobLoots.add(l);
    }

    public void removeCustomMobLoot(CustomMobLoot l){
        this.customMobLoots.remove(l);
    }

    public ArrayList<CustomMobLoot> getCustomMobLoots() {
        return customMobLoots;
    }

    public void setCustomMobLoots(ArrayList<CustomMobLoot> customMobLoots) {
        this.customMobLoots = customMobLoots;
    }
}
