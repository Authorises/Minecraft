package dev.authorises.cavelet.customitems.intelligentitem.items;

import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthPotion extends IntelligentItem {

    public StrengthPotion(){
        setRarity(Rarity.UNCOMMON);
        setId("STRENGTH_POTION");
        setDisplayName("Strength Potion");
        setMaterial(Material.POTION);
        setDescription("Gives Strength II effect for 5:00");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(false);
        setEnchantable(true);
        setPotionColor(Color.RED);
        setFlags();
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftLeftClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftRightClickBlock(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void leftClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftLeftClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, Player player) {

    }

    @Override
    public void placeItem(BlockPlaceEvent event, Player player) {

    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
;
    }

    @Override
    public void breakWith(BlockBreakEvent event, Player player) {

    }

    @Override
    public void shiftBreakWith(BlockBreakEvent event, Player player) {

    }

    @Override
    public void consume(PlayerItemConsumeEvent event, Player player) {
        event.setCancelled(true);
        try {
            player.getInventory().remove(event.getItem());
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6000, 1));
        }catch (Exception e){
            player.sendMessage("An error occured using your strength potion.");
        }

    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {
        event.setCancelled(true);
    }
}
