package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchant;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.exceptions.ItemUnenchantableException;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class EnchantGUI {

    public void update(ChestGui gui, Player p) throws InvalidItemIdException, ItemUnenchantableException, ParseException {
        gui.getPanes().removeAll(gui.getPanes());
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        ItemStack i = p.getInventory().getItemInMainHand();
        if(i==null) return;
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 6);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        StaticPane enchants = new StaticPane(8, 0, 1, 1);
        enchants.addItem(
                new GuiItem(new ItemBuilder(Material.BOOK).setName(ColorUtils.format("&7View all enchantments")).toItemStack(),
                        click -> {
                p.performCommand("enchants");
        }), 0, 0);

        OutlinePane itemPane = new OutlinePane(4, 1, 1, 1);
        itemPane.addItem(new GuiItem(i));
        OutlinePane enchantsPane = new OutlinePane(1, 3, 7, 2);
        CItem ci = new CItem(i);
        if(!ci.getBlueprint().getEnchantable()){
            throw new ItemUnenchantableException("Item cannot be enchantable");
        }
        List<CustomEnchantType> looped = new ArrayList<CustomEnchantType>();
        for(CustomEnchantType type : ci.getBlueprint().getAllowedEnchantments()){
            if(!looped.contains(type)){
                looped.add(type);
                boolean has = false;
                for(CustomEnchant ce : ci.getCustomEnchants()){
                    if(ce.getType().equals(type)) {
                        has = true;
                        if (ce.getLevel().equals(ce.getType().maxLevel)) {
                            enchantsPane.addItem(new GuiItem(new ItemBuilder(Material.ENCHANTED_BOOK)
                                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic>" + ce.getType().tier.color + ce.getType().displayName))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>" + ce.getType().tier.getColorDisplayName() + " Enchantment"))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level <#57cf79>" + ce.getLevel()))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#f24bc0><bold>MAXED OUT"))
                                    .toItemStack()
                            ));
                        }else{
                            Double upgradeCost = ce.getLevel() * ce.getType().tier.darkSoulsCost;
                            enchantsPane.addItem(new GuiItem(new ItemBuilder(Material.ENCHANTED_BOOK)
                                    .setComponentName(Cavelet.miniMessage.deserialize("<!italic>" + ce.getType().tier.color + ce.getType().displayName))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>" + ce.getType().tier.getColorDisplayName() + " Enchantment"))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Level <#57cf79>" + ce.getLevel()))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Upgrade Cost <light_purple>" + Math.round(upgradeCost)+" Dark Souls"))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to upgrade this enchantment"))
                                    .toItemStack(), click -> {
                                if ((mp.getDarkSouls()-upgradeCost)<0){
                                    p.sendMessage(ColorUtils.format("&cYou do not have enough &dDark Souls&c to upgrade this enchantment."));
                                }else{
                                    mp.setDarkSouls(mp.getDarkSouls()-upgradeCost);
                                    p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1F, 1F);
                                    ci.addEnchantment(new CustomEnchant(ce.getType(), ce.getLevel()+1));
                                    p.getInventory().setItemInMainHand(ci.getItemStack());
                                    try {
                                        update(gui, p);
                                    } catch (InvalidItemIdException | ItemUnenchantableException | ParseException e) {
                                        p.closeInventory();
                                        throw new RuntimeException(e);
                                    }
                                }
                            }));
                        }
                    }
                }
                if(!has){
                    Double purchaseCost = type.tier.darkSoulsCost;
                    enchantsPane.addItem(new GuiItem(new ItemBuilder(Material.ENCHANTED_BOOK)
                            .setComponentName(Cavelet.miniMessage.deserialize("<!italic>" + type.tier.color + type.displayName))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>" + type.tier.getColorDisplayName() + " Enchantment"))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Purchase Cost <light_purple>" + Math.round(purchaseCost)+" Dark Souls"))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                            .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Click to purchase this enchantment"))
                            .toItemStack(), click -> {
                        if ((mp.getDarkSouls()-purchaseCost)<0){
                            p.sendMessage(ColorUtils.format("&cYou do not have enough &dDark Souls&c to purchase this enchantment."));
                        }else{
                            mp.setDarkSouls(mp.getDarkSouls()-purchaseCost);
                            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1F, 1F);
                            ci.addEnchantment(new CustomEnchant(type, 1));
                            p.getInventory().setItemInMainHand(ci.getItemStack());
                            try {
                                update(gui, p);
                            } catch (InvalidItemIdException | ItemUnenchantableException e) {
                                p.closeInventory();
                                throw new RuntimeException(e);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }));
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(Cavelet.getPlugin(Cavelet.class), new Runnable() {
            @Override
            public void run() {
                p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -11, -27, -29), Bukkit.createBlockData(Material.END_PORTAL_FRAME));
            }
        },3L);

        p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -12, -28, -29), Bukkit.createBlockData(Material.SEA_LANTERN));
        p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -12, -28, -28), Bukkit.createBlockData(Material.SEA_LANTERN));
        p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -11, -28, -28), Bukkit.createBlockData(Material.SEA_LANTERN));

        gui.addPane(enchantsPane);
        gui.addPane(background);
        gui.addPane(itemPane);
        gui.addPane(enchants);
        gui.update();
    }

    public EnchantGUI(Player p){
        if(p.getInventory().getItemInMainHand()!=null) {
            try {
                ChestGui gui = new ChestGui(6, ColorUtils.format("&dEnchanting"));
                update(gui, p);
                gui.show(p);
                gui.setOnClose(close -> {
                    p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -11, -27, -29), Bukkit.createBlockData(Material.ENCHANTING_TABLE));
                    p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -12, -28, -29), Bukkit.createBlockData(Material.BOOKSHELF));
                    p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -12, -28, -28), Bukkit.createBlockData(Material.BOOKSHELF));
                    p.sendBlockChange(new Location(Bukkit.getWorlds().get(0), -11, -28, -28), Bukkit.createBlockData(Material.BOOKSHELF));
                });

            } catch (Exception e) {
                p.sendMessage(ColorUtils.format("&cError! You can not enchant that item."));
            }
        } else p.sendMessage(ColorUtils.format("&cError! You can not enchant that item."));
    }
}
