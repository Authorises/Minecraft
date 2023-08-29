package dev.authorises.cavelet.intelligentblocks;

import com.google.gson.JsonObject;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import dev.authorises.cavelet.utils.LocationUtil;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Updates.set;

public class IntelligentBlock implements IntelligentBlockFunctions{
    private UUID blockUuid;
    private String blockName;
    private JsonObject data;
    private Location location;

    /**
     * Saves the all the block data to Mongo, i.e. changes or creation will persist over reboot
     */
    public CompletableFuture<UpdateResult> save(){
        return CompletableFuture.supplyAsync(() -> {
            return Cavelet.intelligentBlocks.updateOne(new Document("_id", blockUuid.toString()), Updates.combine(
                    set("_id", blockUuid.toString()),
                    set("block", blockName),
                    set("data", data.toString()),
                    set("location", LocationUtil.locationToString(location))
            ), new UpdateOptions().upsert(true));
        });
    }

    public void delete(){
        Cavelet.intelligentBlocks.deleteOne(new Document("_id", getBlockUuid().toString()));
    }

    public UUID getBlockUuid() {
        return blockUuid;
    }

    public void setBlockUuid(UUID blockUuid) {
        this.blockUuid = blockUuid;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void shiftRightClick(PlayerInteractEvent event) {

    }

    @Override
    public void rightClick(PlayerInteractEvent event) {

    }

    @Override
    public void shiftLeftClick(PlayerInteractEvent event) {

    }

    @Override
    public void leftClick(PlayerInteractEvent event) {

    }

    @Override
    public void broken(BlockBreakEvent event) {

    }

    @Override
    public void shiftBroken(BlockBreakEvent event) {

    }

    @Override
    public void placed(IntelligentBlockPlacedEvent event) throws InterruptedException {

    }
}
