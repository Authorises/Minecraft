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
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilForgeGUI {

    public void update(ChestGui gui, Player p) throws InvalidItemIdException{
        gui.getPanes().removeAll(gui.getPanes());
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());

        ItemStack i = p.getInventory().getItemInMainHand();
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 6);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);
        OutlinePane itemPane = new OutlinePane(4, 1, 1, 1);
        itemPane.addItem(new GuiItem(i));

        OutlinePane upgradesPane = new OutlinePane(1, 3, 7, 2);
        NBTItem nbtItem = new NBTItem(i);
        if(nbtItem.hasKey("item_id")) {
            CItemBlueprint ci = Cavelet.customItemsManager.getItemById(nbtItem.getString("item_id"));
            ItemMeta m = i.getItemMeta();
            if (m instanceof Damageable) {
                Damageable d = (Damageable) m;
                double soulsCost = 200;
                switch (ci.getRarity()){
                    case COMMON -> soulsCost = 20;
                    case UNCOMMON -> soulsCost = 40;
                    case RARE -> soulsCost = 70;
                    case DIVINE -> soulsCost = 150;
                }
                if (d.getDamage() > 0) {
                    double finalSoulsCost = soulsCost;
                    upgradesPane.addItem(new GuiItem(new ItemBuilder(Material.PAPER)
                            .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#30cccf>Repair Item"))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Cost: <#FF55FF>"+String.format("%,.0f", soulsCost)+" Dark Souls"))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left Click to repair"))
                            .toItemStack(), click -> {
                        if((mp.getDarkSouls() - finalSoulsCost)>=0){
                            ((Damageable) m).setDamage(0);
                            i.setItemMeta(m);
                            mp.setDarkSouls(mp.getDarkSouls()-finalSoulsCost);
                        }else{
                            p.sendMessage(ColorUtils.format("&cYou do not have enough &dDark Souls&c to fix this item."));
                        }
                        try {
                            update(gui, p);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));
                }
                CItem citem = new CItem(i);

                for(ForgeUpgrade u : Cavelet.forgeUpgradeManager.getForgeUpgrades().values()){
                    if(!citem.getForgeUpgrades().contains(u)) {
                        if (ci.getAllowedForgeUpgrades() != null) {
                            if (ci.getAllowedForgeUpgrades().contains(u.id)) {
                                if (mp.getCombatLevel() >= u.requiredCombatSkill && mp.getMiningLevel() >= u.requiredCombatSkill && mp.getCombatLevel() >= u.requiredCombatSkill) {
                                    ItemBuilder b = new ItemBuilder(u.material)
                                            .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#30cccf>Forge Upgrade: <#19b6e6>" + u.getName()))
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Requirements:"));
                                    if (u.getRequiredBalance() >= 0D) {
                                        b = b.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>- <#19b6e6>$" + String.format("%,.2f", u.getRequiredBalance())));
                                    }
                                    if (u.getRequiredDarkSouls() >= 0D) {
                                        b = b.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>- <light_purple>" + String.format("%,.0f", u.getRequiredDarkSouls()) + " Dark Souls"));
                                    }
                                    if (u.getRequiredItems().size() >= 0) {
                                        for (CItemBlueprint<?> lrci : u.getRequiredItems().keySet()) {
                                            Double amount = u.getRequiredItems().get(lrci).doubleValue();
                                            if (lrci.getItem() instanceof BasicCustomItem) {
                                                b = b.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>- <#5be374>" + String.format("%,.0f", amount) + "x ").append(((BasicCustomItem) lrci.getItem()).getDisplayNameBasic()));
                                            }
                                            if (lrci.getItem() instanceof ArmourCustomItem) {
                                                b = b.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>- <#5be374>" + String.format("%,.0f", amount) + "x ").append(((ArmourCustomItem) lrci.getItem()).getDisplayNameBasic()));
                                            }
                                        }
                                    }
                                    b=b
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Added Enchants:"));
                                    for(CustomEnchantType t : u.getAddedEnchants().keySet()){
                                        Integer level = u.getAddedEnchants().get(t);
                                        b=b.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>- "+new CustomEnchant(t, level).getDisplayString()));
                                    }
                                    b = b
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left Click to apply"))
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>All of the requirements will be removed upon forge. "))
                                    ;

                                    upgradesPane.addItem(new GuiItem(b.toItemStack(), click -> {
                                        try {
                                            if (u.apply(citem, p, mp)) {
                                                p.getInventory().setItemInMainHand(citem.getItemStack());
                                                update(gui, p);
                                                p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.MASTER, 0.3F, 0.3F);
                                                p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You applied forge: <#19b6e6>"+u.getName()));
                                            } else {
                                                p.sendMessage(Cavelet.miniMessage.deserialize("<red>You do not have the requirements to apply this forge."));
                                                p.playSound(p.getLocation(), Sound.ENTITY_SHEEP_DEATH, SoundCategory.MASTER, 0.2F, 0.2F);
                                                update(gui, p);
                                            }
                                        } catch (InvalidItemIdException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }));
                                } else {
                                    ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                                            .setComponentName(Cavelet.miniMessage.deserialize("<!italic><red>Locked Forge Upgrade"))
                                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                                    if(u.requiredFarmingSkill>0){
                                        builder=builder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Requires <yellow>Farming Level " + u.requiredFarmingSkill));
                                    }
                                    if(u.requiredMiningSkill>0){
                                        builder=builder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Requires <aqua>Mining Level " + u.requiredMiningSkill));
                                    }
                                    if(u.requiredCombatSkill>0){
                                        builder=builder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Requires <red>Combat Level " + u.requiredCombatSkill));
                                    }
                                    upgradesPane.addItem(new GuiItem(builder.toItemStack(), click -> click.setCancelled(true)));
                                }


                            }
                        }
                    }
                }
            }
        }

        gui.addPane(upgradesPane);
        gui.addPane(background);
        gui.addPane(itemPane);
        gui.update();
    }

    public AnvilForgeGUI(Player p){
        try{
            ChestGui gui = new ChestGui(6, ColorUtils.format("&dForgery"));
            update(gui, p);
            gui.show(p);
        }catch (Exception e) {
            p.sendMessage(ColorUtils.format("&cError! You must be holding the item you upgrade/fix."));
        }
    }
}
