package dev.authorises.cavelet.eventsystem;

import org.bukkit.entity.Player;

import java.util.List;

public interface EventFunctions {

    List<EventLocation> getPotentialLocations();
    void start();
    void end(boolean forced);
    void showEventInfo(Player player);

}
