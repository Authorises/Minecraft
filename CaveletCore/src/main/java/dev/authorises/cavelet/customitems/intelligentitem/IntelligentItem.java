package dev.authorises.cavelet.customitems.intelligentitem;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.CItemFlag;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.exceptions.InvalidBlockNameException;
import dev.authorises.cavelet.utils.ItemBuilder;
import dev.authorises.cavelet.utils.LoreChunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class IntelligentItem implements IntelligentItemFunctions{
    public Rarity rarity;
    public String id;
    public String displayName;
    public Material material;
    public String description;
    public CustomEnchantType[] possibleEnchants;
    public String[] possibleForges;
    public boolean stackable;
    public boolean enchantable;
    public Integer pointsValue = 0;
    public Color potionColor = null;
    public List<CItemFlag> flags;

    public ItemStack getItem(){
        ItemBuilder is = new ItemBuilder(material)
                .setComponentName(Cavelet.miniMessage.deserialize("<!italic>"+rarity.color+displayName));
        if(!(this.flags.contains(CItemFlag.HIDE_RARITY))){
            is=is.addComponentLoreLine(Cavelet.miniMessage.deserialize("<!italic>"+rarity.displayName+" Item"));
        }
        if(this.flags.contains(CItemFlag.DISABLE_SMART_DESCRIPTION)){
            List<String> descriptionChunks = List.of(description.split("%newline%"));
            for(String s : descriptionChunks){
                is=is.addComponentLoreLine(Cavelet.miniMessage.deserialize(s));
            }
        }
        else if(description!=""){
            is=is.addLoreLine("");
            List<String> descriptionChunks = LoreChunk.chunk(description, 60, "<!italic><#9eb5db>");
            for(String s : descriptionChunks){
                is=is.addComponentLoreLine(Cavelet.miniMessage.deserialize(s));
            }
        }

        ItemStack item = is.toItemStack();

        if(item.getItemMeta() instanceof PotionMeta){
            PotionMeta iPotionMeta = (PotionMeta) item.getItemMeta();
            iPotionMeta.setColor(getPotionColor());
            item.setItemMeta(iPotionMeta);
        }

        NBTItem nbtItem = new NBTItem(is.toItemStack());
        nbtItem.setString("item_id", id);
        if(!stackable){
            nbtItem.setUUID("random", UUID.randomUUID());
        }
        return nbtItem.getItem();
    }

    public Component getDisplayNameFull(){
        ItemStack i = getItem();
        Component c = getDisplayNameBasic();
        Component lore = Component.empty();
        lore = lore.append(getDisplayNameBasic());
        lore = lore.append(Component.newline());
        for(Component s : i.getItemMeta().lore()){
            lore = lore.append(s);
            lore = lore.append(Component.newline());
        }
        c=c.hoverEvent(HoverEvent.showText(lore));
        return c;
    }

    public Component getDisplayNameBasic(){
        return Cavelet.miniMessage.deserialize(rarity.color+displayName);
    }

    public void setFlags(CItemFlag... flags){
        this.flags = List.of(flags);
    }

    public void setPotionColor(Color meta){
        this.potionColor = meta;
    }

    public Color getPotionColor(){
        return this.potionColor;
    }


    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPointsValue(Integer pointsValue){
        this.pointsValue = pointsValue;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPossibleEnchants(CustomEnchantType... possibleEnchants) {
        this.possibleEnchants = possibleEnchants;
    }

    public void setPossibleForges(String... possibleForges) {
        this.possibleForges = possibleForges;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public void setEnchantable(boolean enchantable) {
        this.enchantable = enchantable;
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
    public void placeItem(BlockPlaceEvent event, Player player) throws InvalidBlockNameException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {

    }

    @Override
    public void shiftPlaceItem(BlockPlaceEvent event, Player player) {

    }

    @Override
    public void breakWith(BlockBreakEvent event, Player player) {

    }

    @Override
    public void shiftBreakWith(BlockBreakEvent event, Player player) {

    }

    @Override
    public void consume(PlayerItemConsumeEvent event, Player player) {

    }

    @Override
    public void shiftConsume(PlayerItemConsumeEvent event, Player player) {

    }
}
