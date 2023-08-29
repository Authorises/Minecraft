package dev.authorises.cavelet.factions;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Container;
import scala.Int;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class FactionTop {

    public List<MFaction> leaderBoard;
    private List<Material> searchTypes = new ArrayList<>();

    public static String getColorFor(Integer rank){
        if(rank==1){
            return "<#c9b037>";
        }else if(rank==2){
            return "<#b4b4b4>";
        }else if(rank==3){
            return "<#ad8a56>";
        }else {
            return "<#21252b>";
        }
    }



    public void calculateWorth(){
        List<MFaction> factionsList = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage(ColorUtils.format("&aFaction Top is being recalculated"));
        });

        Cavelet.intelligentBlockManager.intelligentBlocksMap.forEach((id, block) -> {
            if(!(Objects.equals(block.getBlockName(), "FACTION_GATEWAY"))) return;
            MFaction faction = Cavelet.factionManager.loadedFactions.get(UUID.fromString(block.getData().get("faction-owner").getAsString()));
            if(faction==null){
                block.delete();
                Cavelet.intelligentBlockManager.removeBlock(block);
                block.getLocation().getBlock().setType(Material.AIR, true);
            }
            AtomicReference<Integer> points = new AtomicReference<>(0);
            Chunk chunk = block.getLocation().getChunk();
            Arrays.stream(chunk.getTileEntities()).forEach((e) -> {
                Bukkit.getLogger().info("Found container: "+e.getBlock().getLocation().toString());
                if(!(this.searchTypes.contains(e.getType()))) return;
                Container container = (Container) e.getBlock().getState();
                Arrays.stream(container.getInventory().getContents()).filter(Objects::nonNull).forEach((itemStack) -> {
                    NBTItem nbtItem = new NBTItem(itemStack);
                    if(nbtItem.hasKey("item_id")){
                        try {
                            CItem cItem = new CItem(itemStack);
                            points.updateAndGet(v -> v + cItem.pointsValue*itemStack.getAmount());
                        } catch (InvalidItemIdException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

            });

            faction.lastUpdatedPoints = points.get();
            factionsList.add(faction);
        });

        Collections.sort(factionsList);
        Collections.reverse(factionsList);

        this.leaderBoard = factionsList;

        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage(ColorUtils.format("&aFaction Top has been recalculated, it will be recalculated again in 10 minutes."));
        });
    }

    public FactionTop(){
        Bukkit.getLogger().info("Starting Faction Top Manager...");

        this.leaderBoard = new ArrayList<>();

        searchTypes.add(Material.CHEST);
        searchTypes.add(Material.TRAPPED_CHEST);
        searchTypes.add(Material.HOPPER);
        searchTypes.add(Material.FURNACE);
        searchTypes.add(Material.DISPENSER);
        searchTypes.add(Material.BREWING_STAND);
        searchTypes.add(Material.DROPPER);
        searchTypes.add(Material.SHULKER_BOX);
        searchTypes.add(Material.BARREL);
        searchTypes.add(Material.SMOKER);
        searchTypes.add(Material.BLAST_FURNACE);


        Bukkit.getScheduler().scheduleSyncRepeatingTask(Cavelet.getPlugin(Cavelet.class), this::calculateWorth, 20, 12000);

    }

}
