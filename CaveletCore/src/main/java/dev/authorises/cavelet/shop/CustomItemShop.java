package dev.authorises.cavelet.shop;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.ArmourCustomItem;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.UUID;

public class CustomItemShop {
    private CItemBlueprint<?> item;
    private Double buyPrice;

    public CustomItemShop(CItemBlueprint<?> item, Double buyPrice) {
        this.item = item;
        this.buyPrice = buyPrice;
    }

    public ItemStack getShopItem(){
        if(getItem().getItem() instanceof BasicCustomItem){
            BasicCustomItem b = (BasicCustomItem) getItem().getItem();
            ItemBuilder bu = new ItemBuilder(b.getItem().getType())
                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+b.getRarity().color+b.getDisplayName()))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+b.getRarity().displayName+" Item"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Costs <#19b6e6>$"+String.format("%,.0f", getBuyPrice())))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left Click to purchase"));
            return bu.toItemStack();
        }
        else if(getItem().getItem() instanceof ArmourCustomItem){
            ArmourCustomItem b = (ArmourCustomItem) getItem().getItem();
            Color c = Color.decode(b.getArmourColor());
            ItemBuilder bu = new ItemBuilder(b.getItem().getType())
                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+b.getRarity().color+b.getDisplayName()))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+b.getRarity().displayName+" Item"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Costs <#19b6e6>$"+String.format("%,.0f", getBuyPrice())))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .setLeatherArmorColor(org.bukkit.Color.fromRGB(c.getRed(), c.getGreen(), c.getBlue()))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left Click to purchase"));
            ItemStack s = bu.toItemStack();
            ItemMeta sMeta = s.getItemMeta();
            sMeta.addItemFlags(ItemFlag.HIDE_DYE);
            sMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armourToughness", b.getArmourToughness(), AttributeModifier.Operation.ADD_NUMBER, b.getType().slotType));
            sMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", b.getArmour(), AttributeModifier.Operation.ADD_NUMBER, b.getType().slotType));
            s.setItemMeta(sMeta);
            return s;
        }
        else if(getItem().getItem() instanceof IntelligentItem){
            IntelligentItem b = (IntelligentItem) getItem().getItem();
            ItemBuilder bu = new ItemBuilder(b.getItem().getType())
                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+b.rarity.color+b.displayName))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+b.rarity.displayName+" Item"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Costs <#19b6e6>$"+String.format("%,.0f", getBuyPrice())))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left Click to purchase"));
            ItemStack s = bu.toItemStack();
            return s;
        }
        return null;
    }

    public void purchase(Player p){
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        if((mp.getBalance()-getBuyPrice())>=0){

            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
            if(getItem().getItem() instanceof BasicCustomItem){
                BasicCustomItem b = (BasicCustomItem) getItem().getItem();
                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You purchased <#5be374>1x ").append(b.getDisplayNameFull()).append(Cavelet.miniMessage.deserialize(" <#9eb5db>for <#19b6e6>$" + String.format("%,.2f", buyPrice))));
                mp.setBalance(mp.getBalance()-getBuyPrice());
                p.getInventory().addItem(b.getItem());
            }
            else if(getItem().getItem() instanceof ArmourCustomItem){
                ArmourCustomItem b = (ArmourCustomItem) getItem().getItem();
                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You purchased <#5be374>1x ").append(b.getDisplayNameFull()).append(Cavelet.miniMessage.deserialize(" <#9eb5db>for <#19b6e6>$" + String.format("%,.2f", buyPrice))));
                mp.setBalance(mp.getBalance()-getBuyPrice());
                p.getInventory().addItem(b.getItem());
            }
            else if(getItem().getItem() instanceof IntelligentItem){
                IntelligentItem b = (IntelligentItem) getItem().getItem();
                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You purchased <#5be374>1x ").append(b.getDisplayNameFull()).append(Cavelet.miniMessage.deserialize(" <#9eb5db>for <#19b6e6>$" + String.format("%,.2f", buyPrice))));
                mp.setBalance(mp.getBalance()-getBuyPrice());
                p.getInventory().addItem(b.getItem());
            }
        }else{
            p.sendMessage(ColorUtils.format("&cYou do not have enough money to purchase that item."));
        }

    }

    public CItemBlueprint<?> getItem() {
        return item;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }
}
