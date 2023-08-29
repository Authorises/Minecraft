package dev.authorises.cavelet.eventsystem;

import org.bukkit.Location;

public enum EventLocation {

    NORTHEAST("x 1000 y 1000", 1000D, 71D, 1000D),
    SOUTHEAST("x 1000 y -1000", 1000D, 77D, -1000D),
    SOUTHWEST("x -1000 y -1000", -1000D, 78D, -1000D),
    NORTHWEST("x -1000 y 1000", -1000D, 71D, 1000D),
    SPAWN("x 0 y 0", 1000000D, 77D, 1000000D),
    GLOBAL("globally", 1000000D, 77D, 1000000D)
    ;

    public String location;
    public Double x;
    public Double y;
    public Double z;

    EventLocation(String location, Double x, Double y, Double z){
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
