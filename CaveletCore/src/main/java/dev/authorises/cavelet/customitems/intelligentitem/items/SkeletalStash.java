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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.checkerframework.checker.units.qual.C;

import java.util.Random;

public class SkeletalStash extends IntelligentItem {

    public SkeletalStash(){
        setRarity(Rarity.RARE);
        setId("SKELETAL_STASH");
        setDisplayName("<#ADD8E6><bold>Skeletal Stash");
        setMaterial(Material.BONE_BLOCK);
        setDescription("%newline%" +
                "<!italic><#E6E6E6>Potential Rewards:%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>1-3x "+Rarity.DIVINE.color+"Cthulu's Remedy%newline%" +
                "<!italic><#E6E6E6>- <#ADD8E6>4-16x "+Rarity.UNCOMMON.color+"Cthulu Fragment%newline%"+
                "<!italic><#E6E6E6>- <#ADD8E6>1x <obf>HHHHHHHHHHHHHHH%newline%%newline%" +
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

            Random random = new Random();

            int numOfRemedy = random.nextInt(1, 3);
            int numOfFragment = random.nextInt(4, 16);
            int mysteryRewardId = random.nextInt(1,6);

            for(int i=numOfRemedy;i>0;i-=1){
                player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("CTHULU_REMEDY")).getItemStack());
            }

            for(int i=numOfFragment;i>0;i-=1){
                player.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("CTHULU_FRAGMENT")).getItemStack());
            }

            CItemBlueprint<?> mysteryReward = getReward(mysteryRewardId);

            player.getInventory().addItem(new CItem(mysteryReward).getItemStack());

            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 1F, 1F);

            player.sendMessage(Cavelet.miniMessage.deserialize("" +
                    "<#9eb5db>You opened your ").append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("SKELETAL_STASH")))
                            .append(Cavelet.miniMessage.deserialize("<#9eb5db> and received:" +
                                    "\n<#E6E6E6>- "+numOfRemedy +"x ")).append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("CTHULU_REMEDY"))
                            .append(Cavelet.miniMessage.deserialize("\n<#E6E6E6>- "+numOfFragment +"x ")).append(ChatUtil.getItemComponent(Cavelet.customItemsManager.getItemById("CTHULU_FRAGMENT")))
                            .append(Cavelet.miniMessage.deserialize("\n<#E6E6E6>- 1x ")).append(ChatUtil.getItemComponent(mysteryReward))
                    )
            )
            ;
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
