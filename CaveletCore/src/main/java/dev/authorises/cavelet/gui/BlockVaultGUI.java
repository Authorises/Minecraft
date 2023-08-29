package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.google.gson.JsonObject;
import com.mongodb.client.result.UpdateResult;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import dev.authorises.cavelet.intelligentblocks.blocks.Vault;
import dev.authorises.cavelet.utils.BukkitSerialization;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;
import scala.Int;

import java.util.concurrent.CompletableFuture;

public class BlockVaultGUI {

    private Player p;
    private ChestGui gui;
    private Vault vault;

    private GuiItem getSlotItem(Integer slotNumber){
        String slot = vault.getData().get("slot"+slotNumber).getAsString();
        if(slot.equals("EMPTY")){
            ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE, 1);
            builder = builder.setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Empty Slot"));
            builder = builder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>To place an item in this slot, right click it in your inventory"))
                    .addComponentLoreLine(Cavelet.miniMessage.deserialize(""));

            return new GuiItem(builder.toItemStack(), click -> click.setCancelled(true));
        }else{
            try {

                ItemStack itemStack = BukkitSerialization.itemStackFromBase64(slot);

                ItemBuilder builder = new ItemBuilder(itemStack)
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>---------------------------"))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Slot #"+slotNumber))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Left Click the item to remove it from the Vault"))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>This item is worth {x} Leaderboard Points"))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""));

                return new GuiItem(builder.toItemStack(), click -> {
                    click.setCancelled(true);
                    if(p.getInventory().firstEmpty()!=-1){
                        JsonObject oldData = vault.getData().deepCopy();
                        JsonObject newData = vault.getData();
                        newData.addProperty("slot"+slotNumber, "EMPTY");
                        vault.setData(newData);

                        long s = System.currentTimeMillis();

                        vault.save().thenAccept((UpdateResult result) -> {
                            long diff = System.currentTimeMillis()-s;
                            p.sendMessage(ColorUtils.format("&aUPDATE TOOK "+diff+"ms"));
                            if(result.wasAcknowledged()){
                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getPlugin(Cavelet.class), this::update, 4);
                                p.getInventory().addItem(itemStack);
                            }else{
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                                p.sendMessage(ColorUtils.format("&cAn error occurred saving your action."));
                                vault.setData(oldData);
                            }
                        });
                        p.getInventory().addItem(itemStack);
                    }else{
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                        p.sendMessage(ColorUtils.format("&cYou do not have enough inventory space to pick up this item."));
                    }
                });
            }catch (Exception e){
                ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE, 1);
                builder = builder.setComponentName(Cavelet.miniMessage.deserialize("<!italic><red>Error loading item"));
                builder = builder.addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Please contact a member of the staff team for further assistance."))
                        .addComponentLoreLine(Cavelet.miniMessage.deserialize(""));

                return new GuiItem(builder.toItemStack(), click -> click.setCancelled(true));
            }
        }
    }

    private void update() {

        p.sendMessage(vault.getData().toString());

        gui.getPanes().removeAll(gui.getPanes());

        OutlinePane background = new OutlinePane(0, 0, 9, 3);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setPriority(Pane.Priority.LOWEST);

        OutlinePane slot1Pane = new OutlinePane(1, 1, 1, 1);
        slot1Pane.addItem(getSlotItem(1));

        OutlinePane slot2Pane = new OutlinePane(3, 1, 1, 1);
        slot2Pane.addItem(getSlotItem(2));

        OutlinePane slot3Pane = new OutlinePane(5, 1, 1, 1);
        slot3Pane.addItem(getSlotItem(3));

        OutlinePane slot4Pane = new OutlinePane(7, 1, 1, 1);
        slot4Pane.addItem(getSlotItem(4));

        OutlinePane inventory = new OutlinePane(0, 5, 9, 4);

        inventory.setOnClick((InventoryClickEvent event) -> {
            if(event.isRightClick()) {
                event.setCancelled(true);
                ItemStack clicked = event.getCurrentItem();
                if(clicked == null) return;
                if (clicked.getAmount() > 1) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                    p.sendMessage(ColorUtils.format("&cYou may only place one of an item in slots."));
                } else {
                    try {
                        CItem cItem = new CItem(clicked);
                        Integer nSlot = vault.nextAvailableSlot();
                        if (nSlot != null) {
                            JsonObject oldData = vault.getData().deepCopy();
                            JsonObject newData = vault.getData();
                            newData.addProperty("slot" + nSlot, BukkitSerialization.toBase64(clicked));
                            p.getInventory().remove(clicked);
                            vault.setData(newData);

                            long s = System.currentTimeMillis();

                            vault.save().thenAccept((UpdateResult result) -> {
                                long diff = System.currentTimeMillis() - s;
                                p.sendMessage(ColorUtils.format("&aUPDATE TOOK " + diff + "ms"));
                                if (result.wasAcknowledged()) {
                                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getPlugin(Cavelet.class), this::update, 4);

                                } else {
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                                    p.sendMessage(ColorUtils.format("&cAn error occurred saving your action. REFUNDING ITEMS"));
                                    p.getInventory().addItem(clicked);
                                    vault.setData(oldData);
                                }
                            });
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                            p.sendMessage(ColorUtils.format("&cThis vault is full"));
                        }
                    } catch (Exception exception) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
                        p.sendMessage(ColorUtils.format("&cThat is not a valid item."));
                    }

                }
            }
        });

        gui.addPane(background);
        gui.addPane(slot1Pane);
        gui.addPane(slot2Pane);
        gui.addPane(slot3Pane);
        gui.addPane(slot4Pane);
        gui.addPane(inventory);

        gui.update();
    }

    public BlockVaultGUI(Player player, Vault vault){
        this.p = player;
        this.vault = vault;
        gui = new ChestGui(3, ColorUtils.format("&dVault"));
        gui.show(p);
        update();
    }


}
