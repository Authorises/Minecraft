package dev.authorises.vortechfactions.utilities;

import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.masks.maskUtils;
import dev.authorises.vortechfactions.masks.masks.MysteryMask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class airdropManager {

    private Location loc;
    private Block b;
    public int clicksLeft;
    public HashMap<Player, Integer> playerClicks;
    private boolean active;

    public boolean isActive(){
        return this.active;
    }

    public void getStats(Player p){
        p.sendMessage(ColorUtils.format("&b&lAirDrop&7 statistics:"));
        if(playerClicks.containsKey(p)){
            p.sendMessage(ColorUtils.format("&7- &fYour Clicks: &6"+playerClicks.get(p)));
        }else{
            p.sendMessage(ColorUtils.format("&7- &fYour Clicks: &60"));
        }
        p.sendMessage(ColorUtils.format("&7- &fClicks left: &6"+this.clicksLeft));
        p.sendMessage(ColorUtils.format("&7- &fPercent opened: &6"+Math.round(((500-this.clicksLeft)/500)*100)+"%"));
    }

    public void start(){
        this.active = true;
        this.loc = new Location(Bukkit.getWorlds().get(0), -6,81, 36);
        this.b = loc.getBlock();
        this.clicksLeft = 500;
        b.setType(Material.BEACON);
        playerClicks = new HashMap<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(ColorUtils.format(""));
            p.sendMessage(ColorUtils.format("&b&lAirDrop!"));
            p.sendMessage(ColorUtils.format(""));
            p.sendMessage(ColorUtils.format("&fAn &b&lAirDrop&f has been dropped in the &c&nwarzone&f, it's location is &d&nunknown&f. Click it &6"+this.clicksLeft+"&f times to receive the loot!"));
            p.sendMessage(ColorUtils.format(""));

        }
    }

    public void stop(){
        this.active = false;
        b.setType(Material.AIR);
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(ColorUtils.format("&b&lAirDrop&f&l - &f The AirDrop has been forcefully ended."));
        }
    }

    public void click(Player p){
        if(!(playerClicks.containsKey(p))){
            playerClicks.put(p, 1);
        }else{
            playerClicks.put(p, playerClicks.get(p)+1);
        }
        if(this.clicksLeft==1){
            for(Player lp : Bukkit.getOnlinePlayers()){
                Integer percent = Math.round((playerClicks.get(p)/500)*100);
                lp.sendMessage(ColorUtils.format(""));
                lp.sendMessage(ColorUtils.format("&b&lAirDrop!"));
                lp.sendMessage(ColorUtils.format(""));
                lp.sendMessage(ColorUtils.format(p.getDisplayName()+" has &a&lunlocked&f the &b&lAirDrop&f, they had a click count of &6"+ playerClicks.get(p)+"&f clicks, which was &6"+percent+"%&f of everyone's clicks."));
                lp.sendMessage(ColorUtils.format(""));
                this.active = false;
                b.setType(Material.AIR);
                Bukkit.getWorlds().get(0).dropItem(loc, Items.items.get("airdropbootsnormal"));
                Bukkit.getWorlds().get(0).dropItem(loc, Items.items.get("airdropleggingsnormal"));
                Bukkit.getWorlds().get(0).dropItem(loc, Items.items.get("airdropchestplatenormal"));
                Bukkit.getWorlds().get(0).dropItem(loc, Items.items.get("airdrophelmetnormal"));
                Bukkit.getWorlds().get(0).dropItem(loc, maskUtils.getMaskItem(new MysteryMask()));
            }
        }else{
            this.clicksLeft-=1;
            TitleUtils.sendActionBar(p, ColorUtils.format("&b&lAirDrop&f - &6"+this.clicksLeft+"&f clicks left."));
        }


    }

}
