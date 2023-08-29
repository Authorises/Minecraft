package dev.authorises.vortechfactions.kits;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.masks.maskUtils;
import dev.authorises.vortechfactions.masks.masks.MysteryMask;
import dev.authorises.vortechfactions.shop.shopManager;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.simpleItemBuilder;
import dev.authorises.vortechfactions.utilities.textureUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.figt.loafmenus.LoafMenu;
import us.figt.loafmenus.LoafMenuItem;
import us.figt.loafmenus.MenuRowSize;
import us.figt.loafmenus.utils.MicroItemBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KitGUI extends LoafMenu {
    public KitGUI(Player player) {
        super(VortechFactions.loafMenuRegistrar, "&f&k&l|||&b&l KITS&f&l&k |||", MenuRowSize.HOPPER, player);
    }

    @Override
    public LoafMenuItem[] getMenuItems() {
        // newLoafMenuItemArray() is a small shortcut method
        LoafMenuItem[] array = newLoafMenuItemArray();

        ItemStack maskKitI = textureUtils.createCustomSkull(1, ColorUtils.format("&6&lMask Kit &f(2x Mystery Masks)"), Collections.singletonList(ColorUtils.format("&c&nYou can only use one kit per map.")), "badc048a7ce78f7dad72a07da27d85c0916881e5522eeed1e3daf217a38c1a");

        LoafMenuItem maskKit = new LoafMenuItem(maskKitI, (clicker, clickInformation) -> {
            Player p = clicker.getPlayer();
            if(VortechFactions.dataFileConfig.getList("used-kits").contains(p.getUniqueId().toString())){
                p.sendMessage(ColorUtils.format("&f(&b!&f) You have already used your kit this map!"));
            }else{
                List x = VortechFactions.dataFileConfig.getList("used-kits");
                x.add(p.getUniqueId().toString());
                VortechFactions.dataFileConfig.set("used-kits", x);
                p.getInventory().addItem(maskUtils.getMaskItem(new MysteryMask()));
                p.getInventory().addItem(maskUtils.getMaskItem(new MysteryMask()));
                try {
                    VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });

        ItemStack standardKitI = textureUtils.createCustomSkull(1, ColorUtils.format("&6&lStandard Kit &f(PvP Set)"), Collections.singletonList(ColorUtils.format("&c&nYou can only use one kit per map.")), "c285dd77c64e2368fe77c31ff7c3d42b700fb7b74f2b463e696916c90f5d27ab");

        LoafMenuItem standardKit = new LoafMenuItem(standardKitI, (clicker, clickInformation) -> {
            Player p = clicker.getPlayer();
            if(VortechFactions.dataFileConfig.getList("used-kits").contains(p.getUniqueId().toString())){
                p.sendMessage(ColorUtils.format("&f(&b!&f) You have already used your kit this map!"));
            }else{
                List x = VortechFactions.dataFileConfig.getList("used-kits");
                x.add(p.getUniqueId().toString());
                VortechFactions.dataFileConfig.set("used-kits", x);
                p.getInventory().addItem(Items.items.get("standardboots"));
                p.getInventory().addItem(Items.items.get("standardleggings"));
                p.getInventory().addItem(Items.items.get("standardchestplate"));
                p.getInventory().addItem(Items.items.get("standardhelmet"));
                p.getInventory().addItem(Items.items.get("standardsword"));
                p.getInventory().addItem(Items.items.get("standardpickaxe"));
                p.getInventory().addItem(Items.items.get("standardshovel"));
                p.getInventory().addItem(Items.items.get("standardaxe"));
                try {
                    VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });


        array[0] = maskKit;
        array[1] = standardKit;

        replaceAll(array, Material.AIR, new ItemStack(Material.STAINED_GLASS_PANE));

        return array;
    }
}