package dev.authorises.cavelet.customitems.intelligentitem.items;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
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

public class StarterPack extends IntelligentItem {

    public StarterPack(){
        setRarity(Rarity.COMMON);
        setId("STARTER_PACK");
        setDisplayName("<#00BFFF><bold>Starter Pack");
        setMaterial(Material.LIGHT_GRAY_GLAZED_TERRACOTTA);
        setDescription("%newline%" +
                "<!italic><#E6E6E6>Items Inside:%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.UNCOMMON.color+"Diamond Sword%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.UNCOMMON.color+"Diamond Pickaxe%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.COMMON.color+"Miner Boots%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.COMMON.color+"Miner Leggings%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.COMMON.color+"Miner Chestplate%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1x "+Rarity.COMMON.color+"Miner Helmet%newline%%newline%" +
                "" +
                "<!italic><#ADD8E6><u>Right click<reset><!italic><#ADD8E6> to open");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(false);
        setEnchantable(true);
        setFlags(CItemFlag.HIDE_RARITY, CItemFlag.DISABLE_SMART_DESCRIPTION);
    }

    private boolean open(Player player){
        if(InventoryUtil.freeSlots(player)>=6){

            player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("DIAMOND_SWORD")).getItemStack());
            player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("DIAMOND_PICKAXE")).getItemStack());
            player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("MINER_BOOTS")).getItemStack());
            player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("MINER_LEGGINGS")).getItemStack());
            player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("MINER_CHESTPLATE")).getItemStack());
            player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("MINER_HELMET")).getItemStack());

            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 1F, 1F);
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
