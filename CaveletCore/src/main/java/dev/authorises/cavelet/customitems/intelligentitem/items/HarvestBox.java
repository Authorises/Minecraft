package dev.authorises.cavelet.customitems.intelligentitem.items;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.customitems.CItemFlag;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import dev.authorises.cavelet.utils.ChatUtil;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.InventoryUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class HarvestBox extends IntelligentItem {

    public HarvestBox(){
        setRarity(Rarity.UNCOMMON);
        setId("HARVEST_BOX");
        setDisplayName("<#6AB04A><bold>Harvest Box");
        setMaterial(Material.REINFORCED_DEEPSLATE);
        setDescription("%newline%" +
                "<!italic><#E6E6E6>Potential Rewards:%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1-8x "+Rarity.RARE.color+"Golden Potato Seeds%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>2x <gray>Hopper%newline%"+
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.DIVINE.color+"Enchanted Golden Apple%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.UNCOMMON.color+"Compacted Crops%newline%%newline%" +
                "" +
                "<!italic><#ADD8E6><u>Right click<reset><!italic><#ADD8E6> to open");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(false);
        setEnchantable(true);
        setFlags(CItemFlag.HIDE_RARITY, CItemFlag.DISABLE_SMART_DESCRIPTION);
    }

    private boolean open(Player player){
        if(InventoryUtil.freeSlots(player)>=1){

            Component reward = Cavelet.miniMessage.deserialize("<red>Error");

            int id = new Random().nextInt(0, 40);

            if(id<3){
                CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("ENCHANTED_GOLDEN_APPLE"));
                player.getInventory().addItem(cItem.getItemStack());
                reward = Cavelet.miniMessage.deserialize("<white>"+"1x ").append(ChatUtil.getItemComponent(cItem.getBlueprint()));
            }else if(id<10){
                CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("GOLDEN_POTATO_SEED"));
                int amt = new Random().nextInt(1,8);
                reward = Cavelet.miniMessage.deserialize("<white>"+amt+"x ").append(ChatUtil.getItemComponent(cItem.getBlueprint()));
                ItemStack s = cItem.getItemStack();
                s.setAmount(amt);
                player.getInventory().addItem(s);
            }else if(id<20){
                ItemStack s = new ItemStack(Material.HOPPER, 2);
                player.getInventory().addItem(s);
                reward = Cavelet.miniMessage.deserialize("<white>2x <gray>Hopper");
            }else if(id<40){
                CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("COMPACTED_CROPS"));
                player.getInventory().addItem(cItem.getItemStack());
                reward = Cavelet.miniMessage.deserialize("<white>1x ").append(ChatUtil.getItemComponent(cItem.getBlueprint()));
            }

            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 1F, 1F);

            player.sendMessage(Cavelet.miniMessage.deserialize("" +
                            "<#9eb5db>You opened your ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("HARVEST_BOX")))
                    .append(Cavelet.miniMessage.deserialize("<#9eb5db> and received:\n<white>- ").append(reward))
            );
            return true;
        }else{
            player.sendMessage(ColorUtils.format("&cYou do not have enough open inventory space to open this. Free up some space and try again."));
            return false;
        }

    }

    @Override
    public void leftClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        if(open(player)){
            player.getInventory().remove(event.getItem());
        }
    }

    @Override
    public void shiftLeftClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftRightClickBlock(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        if(open(player)){
            player.getInventory().remove(event.getItem());
        }
    }

    @Override
    public void leftClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        if(open(player)){
            player.getInventory().remove(event.getItem());
        }
    }

    @Override
    public void shiftLeftClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void placeItem(BlockPlaceEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void breakWith(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftBreakWith(BlockBreakEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void consume(PlayerItemConsumeEvent event, Player player) {
        event.setCancelled(true);
    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {
        event.setCancelled(true);
    }
}
