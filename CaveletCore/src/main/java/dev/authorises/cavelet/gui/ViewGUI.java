package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ViewGUI {
    public ViewGUI(Player p, MProfile target){
        boolean online = Cavelet.cachedMPlayers.containsKey(target.getUuid());
        ChestGui gui = new ChestGui(1, ColorUtils.format("&d"+target.getLastUsername()+"'s Information"));
        gui.setOnGlobalClick(click -> click.setCancelled(true));
        OutlinePane viewPane = new OutlinePane(0, 0, 9, 1);
        viewPane.addItem(new GuiItem(
                new ItemBuilder(Material.PLAYER_HEAD, 1)
                        .setSkullOwner(target.getLastUsername())
                        .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>"+target.getLastUsername()))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+(online?"<green>• Online":"<red>• Offline")))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Balance: <#19b6e6>$" + String.format("%,.2f", target.getBalance())))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Dark Souls: <#FF55FF>"+String.format("%,.2f", target.getDarkSouls())))
                        .toItemStack()
        ));

        double neededFarmingXP = (500*(Math.pow(target.getFarmingLevel(), 2)));
        String farmingBonus = "(";
        double addedHearts = Math.floor(target.getFarmingLevel().floatValue()/5F);
        for(int i=0;i<addedHearts;i++){
            farmingBonus+="❤";
        }
        if(farmingBonus=="("){
            farmingBonus="";
        }else{
            farmingBonus+=")";
        }
        ItemBuilder farmingItem = new ItemBuilder(Material.DIAMOND_HOE)
                .setName(ColorUtils.format("&eFarming Skill"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level <yellow>"+target.getFarmingLevel()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Every 5 levels, permanently gain <red>+1❤"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Current bonus:<red> +"+String.format("%,.0f", addedHearts)+" "+farmingBonus))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level completion: <yellow>"+String.format("%,.0f", target.getFarmingXP().doubleValue())+"/"+String.format("%,.0f", neededFarmingXP)+" ("    +String.format("%,.2f", ((target.getFarmingXP()/neededFarmingXP)*100))+"%)"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Maximum level: <yellow>25"))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                ;

        double neededMiningXP = (500*(Math.pow(target.getMiningLevel(), 2)));
        double miningSellBonus = Math.floor(target.getMiningLevel().floatValue()/5F)*10;
        ItemBuilder miningItem = new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName(ColorUtils.format("&bMining Skill"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level <aqua>"+target.getMiningLevel()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Every 5 levels, permanently gain <green>+10% sell bonus"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Current bonus:<green> +"+String.format("%,.0f", miningSellBonus)+"%"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level completion: <aqua>"+String.format("%,.0f", target.getMiningXP().doubleValue())+"/"+String.format("%,.0f", neededMiningXP)+" ("    +String.format("%,.2f", ((target.getMiningXP()/neededMiningXP)*100))+"%)"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Maximum level: <aqua>25"))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                ;

        double neededCombatXP = (500*(Math.pow(target.getCombatLevel(), 2)));
        double combatSoulsBonus = Math.floor(target.getCombatLevel().floatValue()/5F)*15;
        ItemBuilder combatItem = new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(ColorUtils.format("&cCombat Skill"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level <red>"+target.getCombatLevel()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Every 5 levels, permanently gain <light_purple>+15% dark souls from mobs"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Current bonus:<light_purple> +"+String.format("%,.0f", combatSoulsBonus)+"%"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level completion: <red>"+String.format("%,.0f", target.getCombatXP().doubleValue())+"/"+String.format("%,.0f", neededCombatXP)+" ("    +String.format("%,.2f", ((target.getCombatXP()/neededCombatXP)*100))+"%)"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Maximum level: <red>25"))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                ;

        viewPane.addItem(new GuiItem(farmingItem.toItemStack()));
        viewPane.addItem(new GuiItem(miningItem.toItemStack()));
        viewPane.addItem(new GuiItem(combatItem.toItemStack()));


        gui.addPane(viewPane);
        gui.show(p);
    }
}
