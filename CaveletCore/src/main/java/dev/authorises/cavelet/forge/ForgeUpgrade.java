package dev.authorises.cavelet.forge;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.playerdata.MProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForgeUpgrade {
    private HashMap<CItemBlueprint<?>, Integer> requiredItems;
    private Double requiredBalance;
    private Double requiredDarkSouls;
    public Integer requiredFarmingSkill;
    public Integer requiredMiningSkill;
    public Integer requiredCombatSkill;
    public Material material;
    private HashMap<CustomEnchantType, Integer> addedEnchants;
    private String name;
    public String id;

    public ForgeUpgrade(Material material, HashMap<CItemBlueprint<?>, Integer> requiredItems, Double requiredBalance, Double requiredDarkSouls, HashMap<CustomEnchantType, Integer> addedEnchants, String name, String id,Integer requiredFarmingSkill, Integer requiredMiningSkill, Integer requiredCombatSkill) {
        this.material = material;
        this.requiredItems = requiredItems;
        this.requiredBalance = requiredBalance;
        this.requiredDarkSouls = requiredDarkSouls;
        this.addedEnchants = addedEnchants;
        this.requiredFarmingSkill = requiredFarmingSkill;
        this.requiredMiningSkill = requiredMiningSkill;
        this.requiredCombatSkill = requiredCombatSkill;
        this.name = name;
        this.id = id;
    }

    public boolean apply(CItem ci, Player p, MProfile mp) throws InvalidItemIdException {

        // if player has the requirements, removes them and returns true
        // if player doesn't have them all, removes nothing and returns false

        HashMap<ItemStack, Integer> newAmounts = new HashMap<>();
        for(CItemBlueprint<?> bp : requiredItems.keySet()){
            int left = requiredItems.get(bp);
            for(ItemStack s : p.getInventory().getContents()){
                if(s!=null) {
                    if (left > 0) {
                        NBTItem i = new NBTItem(s);
                        if (i.hasKey("item_id")) {
                            if (i.getString("item_id").equals(bp.getId())) {
                                if (left - s.getAmount() > 0) {
                                    left -= s.getAmount();
                                    newAmounts.put(s, 0);
                                } else {
                                    int x = s.getAmount() - left;
                                    left = 0;
                                    newAmounts.put(s, x);
                                }
                            }
                        }
                    }
                }

            }

            if(left>0){
                return false;
            }
        }

        if(!(mp.getBalance()>=requiredBalance)){
            return false;
        }

        if(!(mp.getDarkSouls()>=requiredDarkSouls)){
            return false;
        }

        newAmounts.forEach((a,b) -> {
            if(b>0){
                a.setAmount(b);
            }else{
                p.getInventory().remove(a);
            }
        });

        for(CustomEnchantType t : addedEnchants.keySet()){
            ci.addEnchantment(new CustomEnchant(t, addedEnchants.get(t)));
        }
        ci.getForgeUpgrades().add(this);

        return true;

        /**

         Old version

        List<ItemStack> toRemove = new ArrayList<>();
        HashMap<ItemStack, Integer> toSetAmount = new HashMap<>();
        for(CItemBlueprint<?> bp : requiredItems.keySet()){
            int left = requiredItems.get(bp);
            List<ItemStack> tempRemove = new ArrayList<>();
            HashMap<ItemStack, Integer> tempToSetAmount = new HashMap<>();
            for(ItemStack s : p.getInventory().getContents()){
                if(s!=null){
                    CItem lci = new CItem(s);
                    NBTItem n = new NBTItem(s);
                    if(n.hasKey("item_id")) {
                        if (n.getString("item_id").equals(bp.getId())) {
                            if (left > 0) {
                                if (left - s.getAmount() > 0) {
                                    System.out.println("1");
                                    tempRemove.add(s);
                                    left -= s.getAmount();
                                } else if (left - s.getAmount() < 0) {
                                    System.out.println("2");
                                    tempToSetAmount.put(s, s.getAmount() - left);
                                    left = 0;
                                } else {
                                    System.out.println("3");
                                }
                            }
                        }
                    }

                }
            }
            if(left>0){
                return false;
            }
            toRemove.addAll(tempRemove);
            toSetAmount.putAll(tempToSetAmount);
        }

        if(!(mp.getBalance()>=requiredBalance)){
            return false;
        }

        if(!(mp.getDarkSouls()>=requiredDarkSouls)){
            return false;
        }

        mp.setBalance(mp.getBalance()-requiredBalance);
        mp.setDarkSouls(mp.getDarkSouls()-requiredDarkSouls);
        toRemove.forEach(p.getInventory()::removeItem);
        toSetAmount.forEach(ItemStack::setAmount);

        for(CustomEnchantType t : addedEnchants.keySet()){
            ci.addEnchantment(new CustomEnchant(t, addedEnchants.get(t)));
        }
        ci.getForgeUpgrades().add(this);

        return true;
         */
    }

    public Double getRequiredBalance() {
        return requiredBalance;
    }

    public void setRequiredBalance(Double requiredBalance) {
        this.requiredBalance = requiredBalance;
    }

    public Double getRequiredDarkSouls() {
        return requiredDarkSouls;
    }

    public void setRequiredDarkSouls(Double requiredDarkSouls) {
        this.requiredDarkSouls = requiredDarkSouls;
    }

    public HashMap<CustomEnchantType, Integer> getAddedEnchants() {
        return addedEnchants;
    }

    public void setAddedEnchants(HashMap<CustomEnchantType, Integer> addedEnchants) {
        this.addedEnchants = addedEnchants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<CItemBlueprint<?>, Integer> getRequiredItems() {
        return requiredItems;
    }

    public void setRequiredItems(HashMap<CItemBlueprint<?>, Integer> requiredItems) {
        this.requiredItems = requiredItems;
    }
}
