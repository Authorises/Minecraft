package dev.authorises.cavelet.gui.skills;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.playerdata.MProfileManager;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class SkillsGUI {
    private Player p;
    private MProfile mp;
    private ChestGui gui;

    private void update(){
        gui.getPanes().removeAll(gui.getPanes());

        OutlinePane background = new OutlinePane(0, 0, 9, 3);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        OutlinePane farmingPane = new OutlinePane(1, 1, 1, 1);
        OutlinePane miningPane = new OutlinePane(4, 1, 1, 1);
        OutlinePane combatPane = new OutlinePane(7, 1, 1, 1);
        double neededFarmingXP = (500*(Math.pow(mp.getFarmingLevel(), 2)));
        String farmingBonus = "(";
        double addedHearts = Math.floor(mp.getFarmingLevel().floatValue()/5F);
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
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>You are level <yellow>"+mp.getFarmingLevel()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Every 5 levels, permanently gain <red>+1❤"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Current bonus:<red> +"+String.format("%,.0f", addedHearts)+" "+farmingBonus))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level completion: <yellow>"+String.format("%,.0f", mp.getFarmingXP().doubleValue())+"/"+String.format("%,.0f", neededFarmingXP)+" ("    +String.format("%,.2f", ((mp.getFarmingXP()/neededFarmingXP)*100))+"%)"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Maximum level: <yellow>25"))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                ;

        double neededMiningXP = (500*(Math.pow(mp.getMiningLevel(), 2)));
        double miningSellBonus = Math.floor(mp.getMiningLevel().floatValue()/5F)*10;
        ItemBuilder miningItem = new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName(ColorUtils.format("&bMining Skill"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>You are level <aqua>"+mp.getMiningLevel()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Every 5 levels, permanently gain <green>+10% sell bonus"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Current bonus:<green> +"+String.format("%,.0f", miningSellBonus)+"%"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level completion: <aqua>"+String.format("%,.0f", mp.getMiningXP().doubleValue())+"/"+String.format("%,.0f", neededMiningXP)+" ("    +String.format("%,.2f", ((mp.getMiningXP()/neededMiningXP)*100))+"%)"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Maximum level: <aqua>25"))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                ;

        double neededCombatXP = (500*(Math.pow(mp.getCombatLevel(), 2)));
        double combatSoulsBonus = Math.floor(mp.getCombatLevel().floatValue()/5F)*15;
        ItemBuilder combatItem = new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(ColorUtils.format("&cCombat Skill"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>You are level <red>"+mp.getCombatLevel()))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Every 5 levels, permanently gain <light_purple>+15% dark souls from mobs"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Current bonus:<light_purple> +"+String.format("%,.0f", combatSoulsBonus)+"%"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level completion: <red>"+String.format("%,.0f", mp.getCombatXP().doubleValue())+"/"+String.format("%,.0f", neededCombatXP)+" ("    +String.format("%,.2f", ((mp.getCombatXP()/neededCombatXP)*100))+"%)"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Maximum level: <red>25"))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                ;

        farmingPane.addItem(new GuiItem(farmingItem.toItemStack()));
        miningPane.addItem(new GuiItem(miningItem.toItemStack()));
        combatPane.addItem(new GuiItem(combatItem.toItemStack()));


        gui.addPane(background);
        gui.addPane(farmingPane);
        gui.addPane(miningPane);
        gui.addPane(combatPane);
        gui.update();
    }

    public SkillsGUI(Player p) throws Exception {
        this.p = p;
        this.mp = MProfileManager.getPlayerById(p.getUniqueId());
        this.gui = new ChestGui(3, ColorUtils.format("&dSkills"));
        update();
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.show(p);

    }


}
