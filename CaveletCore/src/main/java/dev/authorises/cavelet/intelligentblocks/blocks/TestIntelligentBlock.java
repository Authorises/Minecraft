package dev.authorises.cavelet.intelligentblocks.blocks;

import com.google.gson.JsonObject;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class TestIntelligentBlock extends IntelligentBlock {

    private String BLOCK_NAME = "TESTBLOCK";

    // Empty constructor for getting name for initial gathering of intelligent blocks
    public TestIntelligentBlock(){
        setBlockName(BLOCK_NAME);
    }

    // Creating block (i.e. placing a intelligent block)
    public TestIntelligentBlock(Location location){
        setBlockName(BLOCK_NAME);
        setBlockUuid(UUID.randomUUID());
        setLocation(location);
        setData(new JsonObject());
    }

    // Loading block from storage
    public TestIntelligentBlock(Location location, UUID blockUuid, JsonObject data){
        setBlockName(BLOCK_NAME);
        setLocation(location);
        setBlockUuid(blockUuid);
        setData(data);
    }

    @Override
    public void shiftRightClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("shift right clicked");
    }

    @Override
    public void rightClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("right clicked");
    }

    @Override
    public void shiftLeftClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("shift left clicked");
    }

    @Override
    public void leftClick(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("left clicked");
    }

    @Override
    public void broken(BlockBreakEvent event) {
        event.getPlayer().sendMessage("broken");
    }

    @Override
    public void shiftBroken(BlockBreakEvent event) {
        event.getPlayer().sendMessage("shift broken");
    }

    @Override
    public void placed(IntelligentBlockPlacedEvent event) {
        event.getPlayer().sendMessage("placed");
    }

}
