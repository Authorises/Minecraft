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
import dev.authorises.cavelet.utils.LootDecider;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;
import scala.Int;

import java.util.Random;

public class MinersCrystal extends IntelligentItem {

    public MinersCrystal(){
        setRarity(Rarity.RARE);
        setId("MINERS_CRYSTAL");
        setDisplayName("<aqua><bold>Miners Crystal");
        setMaterial(Material.AMETHYST_SHARD);
        setDescription("%newline%" +
                "<!italic><aqua>Potential Rewards:%newline%" +
                "<!italic><aqua>- <#ADD8E6>1x "+Rarity.UNCOMMON.color+"Mining Node%newline%" +
                "<!italic><aqua>- <#ADD8E6>1-4x "+Rarity.UNCOMMON.color+"Bejewelled Rock%newline%"+
                "<!italic><aqua>- <#ADD8E6>1x "+Rarity.DIVINE.color+"Enchanted Golden Apple%newline%"+
                "<!italic><aqua>- <#ADD8E6>1x "+Rarity.DIVINE.color+"Alchemy Node%newline%%newline%" +
                "" +
                "" +
                ""+
                "<!italic><#ADD8E6><u>Right click<reset><!italic><#ADD8E6> to open");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(false);
        setEnchantable(true);
        setFlags(CItemFlag.HIDE_RARITY, CItemFlag.DISABLE_SMART_DESCRIPTION);
    }

    private CItemBlueprint<?> getReward(int id){
        switch (id){
            case 1:
            case 2:
            case 3:
            case 4: {
                return Cavelet.customItemsManager.getItemById("CTHULU_KATANA");
            }
            case 5:
            case 6:{
                return Cavelet.customItemsManager.getItemById("ELEMENTAL");
            }
        }
        return null;
    }

    private boolean open(Player player){
        if(InventoryUtil.freeSlots(player)>=5){

            Component reward = Cavelet.miniMessage.deserialize("<red>Error");

            int id = new LootDecider<Integer>()
                    .addChance(30.0, 0)
                    .addChance(30.0, 1)
                    .addChance(30.0, 2)
                    .addChance(10.0, 3)
                    .decide();

            switch (id){
                case 0:{
                    CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("MINING_NODE"));
                    player.getInventory().addItem(cItem.getItemStack());
                    reward = Cavelet.miniMessage.deserialize("<white>"+"1x ").append(ChatUtil.getItemComponent(cItem.getBlueprint()));
                    break;
                }
                case 1:{
                    CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("BEJEWELLED_ROCK"));
                    int amt = new Random().nextInt(1, 5);
                    ItemStack s = cItem.getItemStack();
                    s.setAmount(amt);
                    player.getInventory().addItem(s);
                    reward = Cavelet.miniMessage.deserialize("<white>"+amt+"x ").append(ChatUtil.getItemComponent(cItem.getBlueprint()));
                    break;
                }
                case 2:{
                    CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("ENCHANTED_GOLDEN_APPLE"));
                    player.getInventory().addItem(cItem.getItemStack());
                    reward = Cavelet.miniMessage.deserialize("<white>1x ").append(ChatUtil.getItemComponent(cItem.getBlueprint()));
                    break;
                }
                case 3:{
                    CItem cItem = new CItem(Cavelet.customItemsManager.getItemById("ALCHEMY_NODE"));
                    player.getInventory().addItem(cItem.getItemStack());
                    reward = Cavelet.miniMessage.deserialize("<white>"+"1x ").append(ChatUtil.getItemComponent(cItem.getBlueprint()));
                    break;
                }
            }

            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 1F, 1F);

            player.sendMessage(Cavelet.miniMessage.deserialize("" +
                            "<#9eb5db>You opened your ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("MINERS_CRYSTAL")))
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
