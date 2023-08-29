package dev.authorises.cavelet.customenchants;

import java.awt.*;
import java.util.ArrayList;

public class CustomEnchantLists {

    public ArrayList<CustomEnchantType> armourEnchantments;

    public CustomEnchantLists(){
        this.armourEnchantments = new ArrayList<>();
        this.armourEnchantments.add(CustomEnchantType.PROTECTION);
        this.armourEnchantments.add(CustomEnchantType.FIRE_PROTECTION);
        this.armourEnchantments.add(CustomEnchantType.BLAST_PROTECTION);
    }

}
