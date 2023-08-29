package dev.authorises.cavelet.intelligentblocks;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.FindIterable;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.intelligentitem.IntelligentItem;
import dev.authorises.cavelet.exceptions.InvalidBlockNameException;
import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import dev.authorises.cavelet.utils.LocationUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Updates.set;

public class IntelligentBlockManager implements Listener {
    public HashMap<UUID, IntelligentBlock> intelligentBlocksMap;
    public HashMap<Location, IntelligentBlock> intelligentBlocksLocations;
    private HashMap<String, Class<? extends IntelligentBlock>> usableIntelligentBlocks;

    public void placeBlock(String blockName, Location blockLocation, BlockPlaceEvent blockPlaceEvent) throws InvalidBlockNameException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, InterruptedException {
        if(usableIntelligentBlocks.containsKey(blockName)){
            Class<? extends IntelligentBlock> block = usableIntelligentBlocks.get(blockName);
            IntelligentBlock createdBlock = block.getConstructor(Location.class).newInstance(blockLocation);


            IntelligentBlockPlacedEvent event = new IntelligentBlockPlacedEvent(blockPlaceEvent.getPlayer(), createdBlock, blockPlaceEvent);

            createdBlock.placed(event);

            CompletableFuture.runAsync(() -> {
                if(!(event.isCancelled())) {
                    createdBlock.save();
                    intelligentBlocksLocations.put(createdBlock.getLocation(), createdBlock);
                    intelligentBlocksMap.put(createdBlock.getBlockUuid(), createdBlock);
                }
            });

        }else{
            throw new InvalidBlockNameException("Block with name "+blockName+" not found");
        }
    }

    public void removeBlock(IntelligentBlock block){
        CompletableFuture.runAsync(block::delete);
        intelligentBlocksLocations.remove(block.getLocation());
        intelligentBlocksMap.remove(block.getBlockUuid());
    }

    public IntelligentBlockManager() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        this.intelligentBlocksMap = new HashMap<>();
        this.usableIntelligentBlocks = new HashMap<>();
        this.intelligentBlocksLocations = new HashMap<>();

        Bukkit.getLogger().info("Loading available IntelligentBlocks from code");

        Set<Class<? extends IntelligentBlock>> intelligentBlocks = new Reflections("dev.authorises.cavelet.")
                .getSubTypesOf(IntelligentBlock.class);
        for(Class<? extends IntelligentBlock> c : intelligentBlocks) {
            IntelligentBlock b = c.getConstructor().newInstance();
            usableIntelligentBlocks.put(b.getBlockName(), c);
        }

        Bukkit.getLogger().info("Loading saved IntelligentBlocks from Mongo");

        FindIterable<Document> findIterable =  Cavelet.intelligentBlocks.find();
        List<Document> blocks = StreamSupport.stream(findIterable.spliterator(), false).toList();
        Bukkit.getLogger().info("Found "+blocks.size()+" blocks, loading them now");
        blocks.forEach(document -> {
            Class<? extends IntelligentBlock> type = usableIntelligentBlocks.get(document.getString("block"));

            try {
                IntelligentBlock block = type.getConstructor(Location.class, UUID.class, JsonObject.class).newInstance(
                        LocationUtil.stringToLocation(document.getString("location")),
                        UUID.fromString(document.getString("_id")),
                        new JsonParser().parse(document.getString("data")).getAsJsonObject()
                );

                intelligentBlocksMap.put(block.getBlockUuid(), block);
                intelligentBlocksLocations.put(block.getLocation(), block);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        });
        Bukkit.getLogger().info("Loaded all "+blocks.size()+" blocks. ");
    }

    @EventHandler
    private void shiftRightClick(PlayerInteractEvent event){
        if (!(event.getAction()==Action.RIGHT_CLICK_BLOCK)) return;
        if (!(event.getPlayer().isSneaking())) return;
        if (event.getClickedBlock()==null) return;

        Block clickedBlock = event.getClickedBlock();

        if(this.intelligentBlocksLocations.containsKey(clickedBlock.getLocation())){
            this.intelligentBlocksLocations.get(clickedBlock.getLocation()).shiftRightClick(event);
        }
    }

    @EventHandler
    private void rightClick(PlayerInteractEvent event){
        if (!(event.getAction()==Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getClickedBlock()==null) return;

        Block clickedBlock = event.getClickedBlock();

        if(this.intelligentBlocksLocations.containsKey(clickedBlock.getLocation())){
            this.intelligentBlocksLocations.get(clickedBlock.getLocation()).rightClick(event);
        }
    }

    @EventHandler
    private void shiftLeftClick(PlayerInteractEvent event){
        if (!(event.getAction()==Action.LEFT_CLICK_BLOCK)) return;
        if (!(event.getPlayer().isSneaking())) return;
        if (event.getClickedBlock()==null) return;

        Block clickedBlock = event.getClickedBlock();

        if(this.intelligentBlocksLocations.containsKey(clickedBlock.getLocation())){
            this.intelligentBlocksLocations.get(clickedBlock.getLocation()).shiftLeftClick(event);
        }
    }

    @EventHandler
    private void leftClick(PlayerInteractEvent event){
        if (!(event.getAction()==Action.LEFT_CLICK_BLOCK)) return;
        if (event.getClickedBlock()==null) return;

        Block clickedBlock = event.getClickedBlock();

        if(this.intelligentBlocksLocations.containsKey(clickedBlock.getLocation())){
            this.intelligentBlocksLocations.get(clickedBlock.getLocation()).leftClick(event);
        }
    }

    @EventHandler
    private void broken(BlockBreakEvent event){
        Block clickedBlock = event.getBlock();

        if(this.intelligentBlocksLocations.containsKey(clickedBlock.getLocation())){
            if(event.getPlayer().isSneaking()){
                this.intelligentBlocksLocations.get(clickedBlock.getLocation()).shiftBroken(event);
            }else{
                this.intelligentBlocksLocations.get(clickedBlock.getLocation()).broken(event);
            }

        }
    }



}
