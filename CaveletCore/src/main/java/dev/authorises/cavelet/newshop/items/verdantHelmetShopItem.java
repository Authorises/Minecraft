package dev.authorises.cavelet.newshop.items;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.newshop.ShopCategory;
import dev.authorises.cavelet.newshop.ShopItem;
import dev.authorises.cavelet.newshop.ShopItemType;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.InventoryUtil;
import dev.authorises.cavelet.utils.ItemBuilder;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class verdantHelmetShopItem implements ShopItem {

    @Override
    public @NotNull String displayName() {
        return "Verdant Helmet";
    }

    @Override
    public @NotNull double priceEach() {
        return 5000D;
    }

    @Override
    public @NotNull ShopItemType type() {
        return ShopItemType.ITEM;
    }

    @Override
    public @NotNull ItemStack getDispayItem() {
        return new ItemBuilder(new CItem(Cavelet.customItemsManager.getItemById("VERDANT_HELMET")).getItemStack())
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Verdant Helmet"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize(""))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>Price e/a:"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>- <#FF55FF>800 Dark Souls"))
                .addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic><#9eb5db>- <#19b6e6>$5,000"))
                .toItemStack();
    }

    @Override
    public boolean canPurchaseMany() {
        return true;
    }

    @Override
    public @NotNull Integer maxPurchaseMany() {
        return 2304;
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.LEATHER_HELMET;
    }

    @Override
    public int maxSlotSize(){
        return 1;
    }

    @Override
    public void purchase(Player p, double amt) {
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        mp.setBalance(mp.getBalance()-(amt*priceEach()));

        double slots = Integer.valueOf((int) (amt/maxSlotSize())).doubleValue();

        double cost1 = amt*5000D;
        double cost2 = amt*800D;
        if(cost1<=mp.getBalance() && cost2<=mp.getDarkSouls()){
            if(InventoryUtil.freeSlots(p)>=slots){
                p.showTitle(Title.title(Cavelet.miniMessage.deserialize("<green>Purchase successful"), Cavelet.miniMessage.deserialize("<#9eb5db>You have received your items")));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                mp.setBalance(mp.getBalance()-cost1);
                mp.setDarkSouls(mp.getDarkSouls()-cost2);
                while (amt > 0){
                    CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("VERDANT_HELMET"));
                    p.getInventory().addItem(cItem.getItemStack());
                    amt-=1;
                }
            }else{
                p.sendMessage(ColorUtils.format("&cYou do not have enough inventory space"));
                p.showTitle(Title.title(Cavelet.miniMessage.deserialize("<red>Purchase failed"), Cavelet.miniMessage.deserialize("<#9eb5db>Not enough inventory space")));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
            }
        }else{
            p.sendMessage(ColorUtils.format("&cYou cannot afford to purchase that"));
            p.showTitle(Title.title(Cavelet.miniMessage.deserialize("<red>Purchase failed"), Cavelet.miniMessage.deserialize("<#9eb5db>Insufficient funds")));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1F, 1F);
        }

    }

    @Override
    public @NotNull ArrayList<ShopCategory> getCategories() {
        ArrayList<ShopCategory> categories = new ArrayList<>();
        categories.add(ShopCategory.COMBAT);

        return categories;
    }

}
