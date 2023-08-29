package dev.authorises.cavelet.customenchants;

import dev.authorises.cavelet.utils.NumberFormat;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CustomEnchant {
    private CustomEnchantType type;
    private Integer level;

    public CustomEnchant(String enchantString){
        try {
            JSONObject o = (JSONObject) new JSONParser().parse(enchantString);
            this.type = CustomEnchantType.valueOf((String) o.get("type"));
            this.level = ((Long) o.get("level")).intValue();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public CustomEnchant(CustomEnchantType type, Integer level){
        this.type = type;
        this.level = level;
    }

    public void applyVanillaCounterparts(ItemStack s){
        for(Enchantment e : type.vanillaCounterparts){
            if(s.getEnchantments().containsKey(e)){
                if(s.getEnchantments().get(e)<level){
                    s.removeEnchantment(e);
                    s.addUnsafeEnchantment(e, level);
                }
            }else{
                s.addUnsafeEnchantment(e, level);
            }
        }
    }

    public String getDisplayString(){
        if(getLevel()==1){
            return getType().tier.color + getType().displayName;
        }
        return getType().tier.color + getType().displayName+" "+NumberFormat.romanFormat(getLevel());
    }

    public String getLoreSafeDisplayString(){
        if(getLevel()==1){
            return getType().tier.color + getType().loreSafeDisplayName();
        }
        return getType().tier.color + getType().loreSafeDisplayName()+"%space%"+NumberFormat.romanFormat(getLevel());
    }

    public String getEnchantString(){
        JSONObject o = new JSONObject();
        o.put("type", getType().toString());
        o.put("level", getLevel());
        return o.toJSONString();
    }

    public CustomEnchantType getType(){
        return this.type;
    }

    public Integer getLevel(){
        return this.level;
    }
}
