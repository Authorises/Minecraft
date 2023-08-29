package dev.authorises.cavelet.customitems.intelligentitem.items;

import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class TestIntelligentItemConsumable extends IntelligentItem {

    public TestIntelligentItemConsumable(){
        setRarity(Rarity.DIVINE);
        setId("TEST_INTELLIGENT_ITEM_CONSUMABLE");
        setDisplayName("Test Intelligent Item (Consumable)");
        setMaterial(Material.CARROT);
        setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer orci risus, scelerisque et dictum in, aliquam convallis est. Fusce aliquam nibh et tellus sollicitudin, non pharetra magna aliquet. Nunc nec felis leo. Suspendisse porta, lacus sit amet fringilla auctor, turpis nulla euismod metus, eu hendrerit est ligula sit amet nisi. Aenean eleifend nibh nec elit varius, ac porttitor nisl bibendum. Curabitur varius ex ex, a sagittis enim condimentum vitae. Morbi ac bibendum ipsum, vitae pulvinar orci. Curabitur eu erat ullamcorper, sodales nisi sed, consectetur augue. Aliquam nisl sem, placerat non dapibus nec, pulvinar aliquam massa. Praesent quis vehicula arcu. Fusce sed sapien scelerisque, sollicitudin magna ac, egestas dui. Duis lacus odio, pellentesque lacinia augue et, lobortis fringilla erat. Aliquam tincidunt sem sit amet lectus semper tempor. Sed sed ligula urna. Suspendisse tempor quis mauris ac ultrices. Proin bibendum ante a sapien porttitor mollis. Maecenas vitae lacus vel massa posuere malesuada.");
        setPossibleEnchants(CustomEnchantType.SHARPNESS);
        setPossibleForges();
        setStackable(false);
        setEnchantable(true);
        setFlags();
    }

    @Override
    public void leftClickBlock(PlayerInteractEvent event, Player player) {
        player.sendMessage("left click block");
    }

    @Override
    public void rightClickBlock(PlayerInteractEvent event, Player player) {
        player.sendMessage("right click block");
    }

    @Override
    public void shiftLeftClickBlock(PlayerInteractEvent event, Player player) {
        player.sendMessage("shift left click block");
    }

    @Override
    public void shiftRightClickBlock(PlayerInteractEvent event, Player player) {
        player.sendMessage("shift right click block");
    }

    @Override
    public void leftClickAir(PlayerInteractEvent event, Player player) {
        player.sendMessage("left click air");
    }

    @Override
    public void rightClickAir(PlayerInteractEvent event, Player player) {
        player.sendMessage("right click air");
    }

    @Override
    public void shiftLeftClickAir(PlayerInteractEvent event, Player player) {
        player.sendMessage("shift left click air");
    }

    @Override
    public void shiftRightClickAir(PlayerInteractEvent event, Player player) {
        player.sendMessage("shift right click air");
    }

    @Override
    public void placeItem(BlockPlaceEvent event, Player player) {
        player.sendMessage("place item");
    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {
        player.sendMessage("shift place item");
    }

    @Override
    public void breakWith(BlockBreakEvent event, Player player) {
        player.sendMessage("break with");
    }

    @Override
    public void shiftBreakWith(BlockBreakEvent event, Player player) {
        player.sendMessage("shift break with");
    }

    @Override
    public void consume(PlayerItemConsumeEvent event, Player player) {
        player.sendMessage("consume");
    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {
        player.sendMessage("shift consume");
    }
}
