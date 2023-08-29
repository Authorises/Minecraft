package dev.authorises.cavelet.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class DavidGUI {
    public DavidGUI(Player p){
        Cavelet.objectiveManager.getObjective("talk_to_david").check(p);
        ChestGui gui = new ChestGui(5, ColorUtils.format("&cDavid"));
        OutlinePane pane = new OutlinePane(0, 0, 9, 5);
        Integer x = 0;
        while(x<22){
            pane.addItem(new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(ColorUtils.format("&b"))
                    .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .toItemStack(), event -> event.setCancelled(true)));
            x+=1;
        }
        pane.addItem(new GuiItem(new ItemBuilder(Material.WHEAT)
                .setName(ColorUtils.format("&dSell Wheat &7(Click)"))
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .toItemStack(), event -> {
                    Cavelet.objectiveManager.getObjective("sell_wheat").check(p);
                    int s = 0;
                    for(ItemStack i : p.getInventory().getContents()){
                        try {
                            if (new NBTItem(i).getString("item_id").equals("WHEAT")) {
                                s += i.getAmount();
                                p.getInventory().remove(i);
                            }
                        }catch (Exception e){

                        }
                    }
                    MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                    Double balanceAdded = (s*4)*mp.getSellMultiplier();
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                    p.sendMessage(ColorUtils.format("&fYou sold &a"+s+"x&f Wheat for &b$"+String.format("%,.2f", balanceAdded)));
                    mp.setBalance(mp.getBalance() + balanceAdded);
                    event.setCancelled(true);
                }
        ));
        x=0;
        while(x<22){
            pane.addItem(new GuiItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(ColorUtils.format("&b"))
                    .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .toItemStack(), event -> event.setCancelled(true)));
            x+=1;
        }
        gui.addPane(pane);
        gui.show(p);
    }
}
