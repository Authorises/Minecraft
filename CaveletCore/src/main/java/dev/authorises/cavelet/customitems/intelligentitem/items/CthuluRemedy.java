package dev.authorises.cavelet.customitems.intelligentitem.items;

import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CthuluRemedy extends IntelligentItem {

    public CthuluRemedy(){
        setRarity(Rarity.DIVINE);
        setId("CTHULU_REMEDY");
        setDisplayName("Cthulu's Remedy");
        setMaterial(Material.POTION);
        setDescription("Drink to instantly heal and gain 20 seconds of Resistance I");
        setPossibleEnchants();
        setPossibleForges();
        setStackable(false);
        setEnchantable(true);
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
        try {
            event.setCancelled(true);
            player.getInventory().remove(event.getItem());
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 0));
        }catch (Exception e){
            event.setCancelled(true);
            player.sendMessage("An error occured using your healthshot.");
        }

    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {
        event.setCancelled(true);
    }
}
