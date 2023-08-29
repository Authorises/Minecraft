package dev.authorises.cavelet.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {

    public static String locationToString(Location location) {

        if(location == null) return null;

        return location.getWorld().getName() + "%" + location.getX() + "%" + location.getY() + "%" + location.getZ();
    }

    public static Location stringToLocation(String s){

        if (s == null) return null;

        Object[] d = s.split("%");

        World w = Bukkit.getWorld((String)d[0]);
        double x = Double.parseDouble((String) d[1]);
        double y = Double.parseDouble((String) d[2]);
        double z = Double.parseDouble((String) d[3]);

        return new Location(w, x, y, z);
    }
}