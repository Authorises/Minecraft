package dev.authorises.cavelet.eventsystem.events;

import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.eventsystem.Event;
import dev.authorises.cavelet.eventsystem.EventLocation;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class DebugEvent extends Event {


    public DebugEvent() {
        super("Debug Event", Rarity.DIVINE, "DEBUG_EVENT");
    }

    @Override
    public List<EventLocation> getPotentialLocations(){
        return List.of(EventLocation.GLOBAL);
    };

    @Override
    public void start(){
        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage("debug event started");
        });
    };

    @Override
    public void end(boolean forced){
        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage("debug event ended");
        });
    };

    @Override
    public void showEventInfo(Player player){
        player.sendMessage(ColorUtils.format("Current event: debug :)"));
    }
}
