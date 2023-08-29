package dev.authorises.cavelet.customitems;

import dev.authorises.cavelet.customenchants.CustomEnchantTier;
import dev.authorises.cavelet.customenchants.CustomEnchantType;

import java.util.ArrayList;
import java.util.List;

public class BasicCustomItemBuilder {

    private List<String> allowedForges;
    private BasicCustomItem item;
    private List<CustomEnchantType> allowedEnchants;

    public BasicCustomItemBuilder(BasicCustomItem item){
        this.item = item;

        this.allowedForges = new ArrayList<>();
        this.allowedEnchants = new ArrayList<>();
    }

    public BasicCustomItemBuilder addAllowedForges(String... forges){
        this.allowedForges.addAll(List.of(forges));
        return this;
    }

    public BasicCustomItemBuilder addAllowedEnchants(CustomEnchantType... enchants){
        this.allowedEnchants.addAll(List.of(enchants));
        return this;
    }

    public BasicCustomItem build(){
        this.item.setPossibleForges((String[]) allowedForges.stream().toArray());
        this.item.setPossibleEnchants((CustomEnchantType[]) allowedEnchants.stream().toArray());

        return this.item;
    }

}
