package dev.authorises.cavelet.forge;

import java.util.HashMap;

public class ForgeUpgradeManager {

    private HashMap<String, ForgeUpgrade> forgeUpgrades;

    public HashMap<String, ForgeUpgrade> getForgeUpgrades() {
        return forgeUpgrades;
    }

    public ForgeUpgradeManager(){
        this.forgeUpgrades = new HashMap<>();
    }

    public ForgeUpgrade getForge(String id){
        return forgeUpgrades.get(id);
    }

    public void addUpgrade(ForgeUpgrade f){
        this.forgeUpgrades.put(f.id, f);
    }

}
