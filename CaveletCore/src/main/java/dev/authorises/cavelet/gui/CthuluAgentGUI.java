package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.cthuluagent.TimedSale;
import dev.authorises.cavelet.customitems.ArmourCustomItem;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.json.simple.parser.ParseException;

import java.util.UUID;

public class CthuluAgentGUI {

    private Player p;
    public ChestGui gui;

    public void update() throws ParseException {
        gui.getPanes().removeAll(gui.getPanes());
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        OutlinePane background = new OutlinePane(0, 0, 9, 5);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        OutlinePane item = new OutlinePane(4, 2, 9, 5);

        TimedSale sale = Cavelet.cthuluAgentManager.currentSale;

        if(Cavelet.cthuluAgentManager.boughtBy.contains(p.getUniqueId())){
            item.addItem(new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setComponentName(Cavelet.miniMessage.deserialize("<!italic><red>You've already made a purchase!")).addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>Next sale in " + Cavelet.cthuluAgentManager.getMessage(Cavelet.cthuluAgentManager.ends))).toItemStack()));
        }else {
            if (Cavelet.cthuluAgentManager.currentStock - sale.amountPerSale >= 0) {
                CItem ci = new CItem(sale.itemSold);
                ItemStack s = ci.getItemStack();
                ItemBuilder itemBuilder = new ItemBuilder(s.getType())
                        .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#5be374>" + sale.amountPerSale + "x " + ci.displayName));
                for (Component lll : s.getItemMeta().lore()) {
                    itemBuilder = itemBuilder.addComponentLoreLine(lll);
                }
                if(s.getItemMeta() instanceof LeatherArmorMeta){
                    LeatherArmorMeta meta = (LeatherArmorMeta) s.getItemMeta();
                    Color c = meta.getColor();
                    itemBuilder = itemBuilder.setLeatherArmorColor(c);
                    itemBuilder = itemBuilder.addFlags(ItemFlag.HIDE_DYE);
                }
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                Integer amount = sale.fragmentsCost;
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Buy for <#5be374>" + amount + "x " + Rarity.COMMON.color + "Cthulu Fragment" + (amount > 1 ? "" : "s")));
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#5be374>" + Cavelet.cthuluAgentManager.currentStock + "<#9eb5db> left in stock"));
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>" + Cavelet.cthuluAgentManager.getMessage(Cavelet.cthuluAgentManager.ends) + " remaining"));
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""));
                itemBuilder = itemBuilder.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left click to purchase"));
                ItemStack b = itemBuilder.toItemStack();
                if(ci.getBlueprint().getItem() instanceof ArmourCustomItem){
                    ArmourCustomItem i = (ArmourCustomItem) ci.getBlueprint().getItem();
                    ItemMeta m = b.getItemMeta();
                    m.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armourToughness", i.armourToughness, AttributeModifier.Operation.ADD_NUMBER, i.type.slotType));
                    m.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", i.armour, AttributeModifier.Operation.ADD_NUMBER, i.type.slotType));
                    b.setItemMeta(m);
                }
                item.addItem(new GuiItem(b, click -> {
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 3F, 0.5F);
                    try {
                        Cavelet.cthuluAgentManager.attemptPurchase(p);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }));


            } else {
                item.addItem(new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setComponentName(Cavelet.miniMessage.deserialize("<!italic><red>Out of stock!")).addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><red>Next sale in " + Cavelet.cthuluAgentManager.getMessage(Cavelet.cthuluAgentManager.ends))).toItemStack()));
            }
        }

        gui.addPane(background);
        gui.addPane(item);
        gui.update();
    }

    public CthuluAgentGUI(Player p){
        try{
            this.p = p;
            ChestGui gui = new ChestGui(5, ColorUtils.format("&dCthulu Agent"));
            this.gui = gui;
            Cavelet.cthuluAgentManager.openGuis.add(this);
            gui.setOnClose(event -> {
                Cavelet.cthuluAgentManager.openGuis.remove(this);
            });
            update();
            gui.show(p);
        }catch (Exception e) {
            e.printStackTrace();
            p.sendMessage(ColorUtils.format("&cAn error occurred opening the Deep Market Interface."));
        }
    }

}
