package dev.authorises.cavelet.cthuluagent;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.gui.CthuluAgentGUI;
import eu.decentsoftware.holograms.api.DHAPI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.json.simple.parser.ParseException;

import java.util.*;

public class CthuluAgentManager {
    List<TimedSale> sales;


    // temp stuff - changes a lot
    public List<CthuluAgentGUI> openGuis;
    public Integer currentStock=null;
    public TimedSale currentSale=null;
    public List<UUID> boughtBy;
    public long ends;

    public TimedSale randomSale(){
       SaleRarity rarity = SaleRarity.pickRandom();
       List<TimedSale> s = sales.stream().filter(t -> t.rarity.equals(rarity)).toList();
       if(!(s.size()>0)) return randomSale();
       return s.get(new Random().nextInt(s.size()));
    }

    public void attemptPurchase(Player p) throws ParseException {
        if(boughtBy.contains(p.getUniqueId())){
            p.sendMessage(Cavelet.miniMessage.deserialize("<red>You have already bought one of these items from the Cthulu Agent. Come back in "+getMessage(ends+2000)));
            return;
        }
        if(!(currentStock-currentSale.amountPerSale>=1)){
            p.sendMessage(Cavelet.miniMessage.deserialize("<red>The Cthulu Agent has ran out of stock for this item. Next restock in "+getMessage(ends+2000)));
            return;
        }

        HashMap<ItemStack, Integer> newAmounts = new HashMap<>();
        int left = currentSale.fragmentsCost;
        for (ItemStack s : p.getInventory().getContents()) {
            if (s != null) {
                if (left > 0) {
                    NBTItem i = new NBTItem(s);
                    if (i.hasKey("item_id")) {
                        if (i.getString("item_id").equalsIgnoreCase("CTHULU_FRAGMENT")) {
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

        if (left > 0) {
            p.sendMessage(Cavelet.miniMessage.deserialize("<red>You do not have enough Cthulu Fragments to purchase this item."));
            return;
        }

        newAmounts.forEach((a, b) -> {
            if (b > 0) {
                a.setAmount(b);
            } else {
                p.getInventory().remove(a);
            }
        });

        int x = currentSale.amountPerSale;
        while (x>0){
            x-=1;
            CItem add = new CItem(currentSale.itemSold);
            p.getInventory().addItem(add.getItemStack());
            currentStock-=1;
            boughtBy.add(p.getUniqueId());
        }

        openGuis.forEach(s -> {
            try {
                s.update();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void setSale(TimedSale s) throws ParseException {
        this.currentSale = s;
        this.ends = System.currentTimeMillis()+(5*60*1000);
        this.currentStock = s.stock;
        this.boughtBy = new ArrayList<>();
        updateRemaining();
        updateStock();
        ItemStack i = new CItem(this.currentSale.itemSold).item;
        String icon = "#ICON: "+i.getType();
        if(i.getItemMeta() instanceof LeatherArmorMeta){
            LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
            Color c = meta.getColor();
            //NumberFormat.hexToDecimal("#"+Integer.toHexString(c.asRGB()).substring(2))
            icon +=" {display:{color:"+c.asRGB()+"}}";
        }
        DHAPI.getHologram("cthulu_agent").getPage(0).setLine(2, icon);
        DHAPI.getHologram("cthulu_agent").getPage(0).setLine(3, "<#5be374>"+currentSale.amountPerSale+"x "+ Cavelet.miniMessage.serialize(new CItem(this.currentSale.itemSold).getItemStack().getItemMeta().displayName()).replace("<!italic>", "").replace("<bold>", "&b"));
    }

    public String getMessage(long ends) {
        long d = ends - System.currentTimeMillis();
        long seconds = d / 1000;
        long minutes = seconds / 60;;
        if (minutes > 0) {
            return minutes + "m " + (seconds-(60*minutes)) + "s";
        } else {
            if (seconds > 0) {
                return seconds + "s";
            }
        }
        return "now";
    }

    public void updateRemaining(){
        DHAPI.getHologram("cthulu_agent").getPage(0).setLine(4, "<#9eb5db>"+getMessage(ends)+" remaining");
    }

    public void updateStock(){
        DHAPI.getHologram("cthulu_agent").getPage(0).setLine(5, "<#5be374>"+currentStock+"<#9eb5db> left in stock");
    }

    public CthuluAgentManager(TimedSale... timedSales){
        sales = new ArrayList<>(List.of(timedSales));
        openGuis = new ArrayList<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), () -> {
            if(this.currentSale == null){
                try {
                    setSale(randomSale());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            if(System.currentTimeMillis()>ends){
                try {
                    setSale((randomSale()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }else{
                updateRemaining();
                updateStock();
                openGuis.forEach(s -> {
                    try {
                        s.update();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        },0L, 20L);

    }

}
