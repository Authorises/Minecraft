package dev.authorises.cavelet.intelligentblocks.blocks;

import com.google.gson.JsonObject;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlock;
import dev.authorises.cavelet.intelligentblocks.events.IntelligentBlockPlacedEvent;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FactionGateway extends IntelligentBlock {

    private String BLOCK_NAME = "FACTION_GATEWAY";

    // Empty constructor for getting name for initial gathering of intelligent blocks
    public FactionGateway(){
        setBlockName(BLOCK_NAME);
    }

    // Creating block (i.e. placing a intelligent block)
    public FactionGateway(Location location){
        setBlockName(BLOCK_NAME);
        setBlockUuid(UUID.randomUUID());
        setLocation(location);
        setData(new JsonObject());
    }

    // Loading block from storage
    public FactionGateway(Location location, UUID blockUuid, JsonObject data){
        setBlockName(BLOCK_NAME);
        setLocation(location);
        setBlockUuid(blockUuid);
        setData(data);
    }

    private MFaction getFaction(){
        return Cavelet.factionManager.loadedFactions.get(UUID.fromString(getData().get("faction-owner").getAsString()));
    }

    @Override
    public void shiftRightClick(PlayerInteractEvent event) {

    }

    @Override
    public void rightClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        String facName = getFaction().getName();
        event.setCancelled(true);
        p.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>This <#db65d7>Faction Gateway<#9eb5db> is owned by <green>"+facName).hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<#9eb5db>Click to show faction information"))).clickEvent(ClickEvent.runCommand("/f show "+facName)));
    }

    @Override
    public void shiftLeftClick(PlayerInteractEvent event) {

    }

    @Override
    public void leftClick(PlayerInteractEvent event) {

    }

    @Override
    public void broken(BlockBreakEvent event) {
        event.setDropItems(false);
        MFaction faction = getFaction();
        if(faction==null || faction.factionGateway==null) return;
        CompletableFuture.runAsync(() -> {
            Cavelet.intelligentBlockManager.removeBlock(this);
            faction.factionGateway = null;
        }).thenRun(faction::updateData).thenRun(() -> {
            event.getBlock().getWorld().playSound(event.getBlock().getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.MASTER, 1F, 1F);
            Bukkit.getOnlinePlayers().forEach((p) -> {
                p.sendMessage(Cavelet.miniMessage.deserialize("<green>"+faction.getName()+"'s <#db65d7>Faction Gateway<#9eb5db> has been destroyed."));
            });
        });
    }

    @Override
    public void shiftBroken(BlockBreakEvent event) {

    }

    @Override
    public void placed(IntelligentBlockPlacedEvent event) throws InterruptedException {
        Player p = event.getPlayer();
        if(Cavelet.factionManager.playersFactions.containsKey(p.getUniqueId())){
            MFaction faction = Cavelet.factionManager.playersFactions.get(p.getUniqueId());
            if(faction.factionGateway==null){
                faction.factionGateway = event.getPlaceEvent().getBlockPlaced().getLocation();
                faction.updateData();
                JsonObject data = getData();
                data.addProperty("faction-owner", faction.getId().toString());
                setData(data);
                p.sendMessage(ColorUtils.format("&bYou have placed your faction gateway. Any eternals or other valuable items stored in containers which are placed in this chunk will count towards your Faction Top ranking"));
                p.playSound(p.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, SoundCategory.MASTER, 1F, 1F);
            }else{
                p.sendMessage(ColorUtils.format("&cYour faction has already placed a gateway. If you would like to move it, first destroy your old gateway."));
                event.setCancelled(true);
            }
        }else{
            event.setCancelled(true);
        }
    }

}
