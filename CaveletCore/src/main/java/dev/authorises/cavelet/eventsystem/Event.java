package dev.authorises.cavelet.eventsystem;

import dev.authorises.cavelet.customitems.Rarity;
import org.bukkit.entity.Player;

import java.util.List;

public class Event implements EventFunctions{


    public final String name;
    public final Rarity rarity;
    public final String id;

    public Event(String name, Rarity rarity, String id){
        this.name = name;
        this.rarity = rarity;
        this.id = id;
    }


    @Override
    public List<EventLocation> getPotentialLocations() {
        return List.of(EventLocation.GLOBAL);
    }

    @Override
    public void start() {

    }

    @Override
    public void end(boolean forced) {

    }

    @Override
    public void showEventInfo(Player player) {

    }
}
