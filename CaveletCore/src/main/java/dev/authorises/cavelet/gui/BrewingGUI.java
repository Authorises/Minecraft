package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.ArmourCustomItem;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.forge.ForgeUpgrade;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ChatUtil;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.InventoryUtil;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Struct;
import java.util.HashMap;

public class BrewingGUI {

    private enum Brewable {
        STRENGTH("STRENGTH_POTION", 0,0,10, 2, 3, 0, 0.0, 0.0),
        SPEED("SPEED_POTION", 0,10,0, 0, 3, 2, 0.0, 0.0),
        FIRE_RESISTANCE("FIRE_RESISTANCE_POTION", 0,10,0, 0, 2, 0, 0.0, 0.0);

        public String itemid;
        public int miningSkill;
        public int farmingSkill;
        public int combatSkill;
        public int dust;
        public int crops;
        public int rocks;
        public Double money;
        public Double souls;
        private HashMap<CItemBlueprint<?>, Integer> requiredItems;

        Brewable(String itemId, int miningSkill, int farmingSkill, int combatSkill, int dust, int crops, int rocks, Double money, Double souls){
            this.itemid = itemId;
            this.miningSkill = miningSkill;
            this.farmingSkill = farmingSkill;
            this.combatSkill = combatSkill;
            this.dust = dust;
            this.crops = crops;
            this.rocks = rocks;
            this.money = money;
            this.souls = souls;
            this.requiredItems = new HashMap<CItemBlueprint<?>, Integer>();
            if(this.dust!=0){
                this.requiredItems.put(Cavelet.customItemsManager.getItemById("DUST"), this.dust);
            }
            if(this.crops!=0){
                this.requiredItems.put(Cavelet.customItemsManager.getItemById("COMPACTED_CROPS"), this.crops);
            }
            if(this.rocks!=0){
                this.requiredItems.put(Cavelet.customItemsManager.getItemById("BEJEWELLED_ROCK"), this.rocks);
            }
        }

        public String brew(Player p, MProfile mp) throws InvalidItemIdException {

            // if player has the requirements, removes them and returns true
            // if player doesn't have them all, removes nothing and returns false

            if(!(mp.getBalance()>=money)){
                return "<red>You do not have enough money to brew this";
            }

            if(!(mp.getDarkSouls()>=souls)){
                return "<red>You do not have enough <light_purple>dark souls<red> to brew this";
            }

            if(!(mp.getFarmingLevel()>=farmingSkill)){
                return "<red>You must be farming level "+farmingSkill+" to brew that (/skills)";
            }
            if(!(mp.getMiningLevel()>=miningSkill)){
                return "<red>You must be mining level "+miningSkill+" to brew that (/skills)";
            }
            if(!(mp.getCombatLevel()>=combatSkill)){
                return "<red>You must be combat level "+combatSkill+" to brew that (/skills)";
            }

            if(!(InventoryUtil.freeSlots(p)>=1)){
                return "<red>You do not have enough inventory space to brew this";
            }

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
                    return "<red>You do not have the required items to brew this";
                }
            }

            newAmounts.forEach((a,b) -> {
                if(b>0){
                    a.setAmount(b);
                }else{
                    p.getInventory().remove(a);
                }
            });
            p.playSound(p.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, SoundCategory.MASTER, 1F, 1F);
            p.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById(itemid)).getItemStack());
            return "";

        }

    }

    public void update(ChestGui gui, Player p) throws InvalidItemIdException {
        gui.getPanes().removeAll(gui.getPanes());
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());

        ItemStack i = p.getInventory().getItemInMainHand();
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 6);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        OutlinePane brewables = new OutlinePane(1,1,7,4);

        for(Brewable brewable : Brewable.values()){
            ItemBuilder itemBuilder = new ItemBuilder(new CItem(Cavelet.customItemsManager.getItemById(brewable.itemid)).getItemStack())
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Requirements:"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""));


            if(brewable.dust>0){
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db> - "+brewable.dust+"x ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("DUST"))));
            }
            if(brewable.crops>0){
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db> - "+brewable.crops+"x ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("COMPACTED_CROPS"))));
            }
            if(brewable.rocks>0){
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db> - "+brewable.rocks+"x ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("BEJEWELLED_ROCK"))));
            }

            boolean canBrew = true;

            if(!(mp.getCombatLevel()>=brewable.combatSkill)){
                canBrew=false;
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>Requires Combat Level "+brewable.combatSkill));
            }
            if(!(mp.getMiningLevel()>=brewable.miningSkill)){
                canBrew=false;
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>Requires Mining Level "+brewable.miningSkill));
            }
            if(!(mp.getFarmingLevel()>=brewable.farmingSkill)){
                canBrew=false;
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>Requires Farming Level "+brewable.farmingSkill));
            }
            if(!(mp.getBalance()>=brewable.money)){
                canBrew=false;
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>Requires $"+String.format("%,.0f", brewable.money)));
            }
            if(!(mp.getDarkSouls()>=brewable.souls)){
                canBrew=false;
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                itemBuilder=itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>Requires "+String.format("%,.0f", brewable.souls)+" dark souls"));
            }

            if(canBrew) {
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to brew this potion"));
            }

            brewables.addItem(new GuiItem(itemBuilder.toItemStack(), click -> {
                try {
                    String s = brewable.brew(p, mp);
                    if(s!=""){
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                        p.sendMessage(Cavelet.miniMessage.deserialize(s));
                    }
                } catch (InvalidItemIdException e) {
                    p.sendMessage(ColorUtils.format("&cInternal error occured whilst processing brew request"));
                    throw new RuntimeException(e);
                }
            }));
        }

        gui.addPane(brewables);
        gui.addPane(background);
        gui.update();
    }

    public BrewingGUI(Player p) {
        try {
            ChestGui gui = new ChestGui(6, ColorUtils.format("&dAlchemy Node"));
            update(gui, p);
            gui.show(p);
        } catch (Exception e) {
            p.sendMessage(ColorUtils.format("&cAn error occurred whilst the Alchemy Node was in use."));
        }
    }
}