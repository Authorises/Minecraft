package dev.authorises.cavelet.shop;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.ItemBuilder;
import dev.authorises.cavelet.utils.TimeFormat;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemStackShop extends Buyable{
    private final Material showItem;
    private final ShopType type;
    private Long millisecondsExpires;
    private final String name;
    private final String displayName;
    private final ItemStack item;
    private final BuyableFrom[] buyableFroms;

    public ItemStackShop(Material showItem, ShopType type, Long millisecondsExpires, String name, String displayName, ItemStack item, Double price, BuyableFrom... buyableFroms){
        this.showItem = showItem;
        this.type = type;
        this.millisecondsExpires = millisecondsExpires;
        this.name = name;
        this.displayName = displayName;
        this.item = item;
        setPrice(price);
        this.buyableFroms = buyableFroms;
    }

    public ItemStack getShopItem(){
        if(getType().equals(ShopType.TEMPORARY)){
            if(System.currentTimeMillis()>=this.millisecondsExpires){
                return new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                        .setName(ColorUtils.format("&cThis item is currently unavailable"))
                        .toItemStack();
            }
        }
        ItemBuilder b = new ItemBuilder(this.showItem)
                .setName(ColorUtils.format(this.displayName))
                .addLoreLine(ColorUtils.format(""))
                .addLoreLine(ColorUtils.format("&7Price: &b$"+getPrice()));
        if(this.getType().equals(ShopType.TEMPORARY)){
            b.addLoreLine(ColorUtils.format("&7Time left to buy: &b"+ TimeFormat.format(this.millisecondsExpires-System.currentTimeMillis())));
        }
                b.addLoreLine(ColorUtils.format(""))
                 .addLoreLine(ColorUtils.format("&7Left Click to purchase"));
        return b.toItemStack();
    }

    public void purchase(Player p){
        if(getType().equals(ShopType.TEMPORARY)){
            if(System.currentTimeMillis()>=this.millisecondsExpires){
                p.sendMessage(ColorUtils.format("&cThis item is currently unavailable"));
                return;
            }
        }
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        if(mp.getBalance()-getPrice()<0){
            p.sendMessage(ColorUtils.format("&cYou cannot afford to purchase this item."));
        }else{
            mp.setBalance(mp.getBalance()-getPrice());
            p.getInventory().addItem(getItem());
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
            p.sendMessage(ColorUtils.format("&fYou purchased &b1x &r"+this.displayName));
        }
    }

    public Material getShowItem() {
        return showItem;
    }

    public ShopType getType() {
        return type;
    }

    public Long getMillisecondsExpires() {
        return millisecondsExpires;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItem() {
        return item;
    }

    public BuyableFrom[] getBuyableFroms() {
        return buyableFroms;
    }

    public void setMillisecondsExpires(Long millisecondsExpires) {
        this.millisecondsExpires = millisecondsExpires;
    }
}
