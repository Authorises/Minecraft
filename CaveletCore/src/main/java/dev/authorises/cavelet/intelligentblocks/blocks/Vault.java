package dev.authorises.cavelet.intelligentblocks.blocks;

import com.google.gson.JsonObject;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.gui.BlockVaultGUI;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Vault extends IntelligentBlock {

    private String BLOCK_NAME = "VAULT";

    // Empty constructor for getting name for initial gathering of intelligent blocks
    public Vault(){
        setBlockName(BLOCK_NAME);
    }

    // Creating block (i.e. placing a intelligent block)
    public Vault(Location location){
        setBlockName(BLOCK_NAME);
        setBlockUuid(UUID.randomUUID());
        setLocation(location);
        setData(new JsonObject());
    }

    // Loading block from storage
    public Vault(Location location, UUID blockUuid, JsonObject data){
        setBlockName(BLOCK_NAME);
        setLocation(location);
        setBlockUuid(blockUuid);
        setData(data);
    }

    // Returns null if there is no available slot
    public Integer nextAvailableSlot(){
        int ch = 1;
        while(ch<5){
            String x = getData().get("slot"+ch).getAsString();
            if(Objects.equals(x, "EMPTY")){
                return ch;
            }
            ch+=1;
        }
        return null;
    }

    @Override
    public void shiftRightClick(PlayerInteractEvent event) {

    }

    @Override
    public void rightClick(PlayerInteractEvent event) {
        event.setCancelled(true);
        new BlockVaultGUI(event.getPlayer(), this);
    }

    @Override
    public void shiftLeftClick(PlayerInteractEvent event) {

    }

    @Override
    public void leftClick(PlayerInteractEvent event) {

    }

    @Override
    public void broken(BlockBreakEvent event) {
        CompletableFuture.runAsync(() -> {
            Cavelet.intelligentBlockManager.removeBlock(this);
        });
    }

    @Override
    public void shiftBroken(BlockBreakEvent event) {

    }

    @Override
    public void placed(IntelligentBlockPlacedEvent event) throws InterruptedException {
        Player placer = event.getPlayer();
        placer.playSound(placer.getLocation(), Sound.BLOCK_ANCIENT_DEBRIS_PLACE, SoundCategory.MASTER, 1F, 1F);
        JsonObject data = getData();
        data.addProperty("slot1", "EMPTY");
        data.addProperty("slot2", "EMPTY");
        data.addProperty("slot3", "EMPTY");
        data.addProperty("slot4", "EMPTY");
        setData(data);
    }

}
