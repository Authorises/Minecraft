package dev.authorises.vortechfactions.utilities;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.masks.maskUtils;
import dev.authorises.vortechfactions.masks.masks.MysteryMask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class FirstJoin {

    public void run(Player p){
        if(!(VortechFactions.dataFileConfig.getList("first-joins").contains(p.getUniqueId().toString()))){
            List x = VortechFactions.dataFileConfig.getList("first-joins");
            x.add(p.getUniqueId().toString());
            VortechFactions.dataFileConfig.set("first-joins", x);
            for(Player lp : Bukkit.getOnlinePlayers()) {
                lp.sendMessage(ColorUtils.format(""));
                lp.sendMessage(ColorUtils.format(p.getDisplayName()+" &fJoined for the &b&nFirst Time&f!"));
                if(lp!=p) {
                    lp.sendMessage(ColorUtils.format(""));
                }
            }
            p.sendMessage(ColorUtils.format("&fWelcome to &b&lInstinctia Factions&f."));
            p.sendMessage(ColorUtils.format("&fDiscord: &bhttps://discord.gg/QBGTN5PDf5"));
            p.sendMessage(ColorUtils.format(""));
            p.getInventory().addItem(maskUtils.getMaskItem(new MysteryMask()));
            p.getInventory().addItem(Items.items.get("standardboots"));
            p.getInventory().addItem(Items.items.get("standardleggings"));
            p.getInventory().addItem(Items.items.get("standardchestplate"));
            p.getInventory().addItem(Items.items.get("standardhelmet"));
            p.getInventory().addItem(Items.items.get("standardsword"));
            p.getInventory().addItem(Items.items.get("standardpickaxe"));
            p.getInventory().addItem(Items.items.get("standardshovel"));
            p.getInventory().addItem(Items.items.get("standardaxe"));
            VortechFactions.econ.withdrawPlayer(p, VortechFactions.econ.getBalance(p));
            VortechFactions.econ.depositPlayer(p, 50000);
            try {
                VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
