package dev.authorises.vortechfactions.utilities;

import dev.authorises.vortechfactions.VortechFactions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;

public class homeUtils {

    public static boolean hasHome(Player p){
        return VortechFactions.dataFileConfig.contains(p.getUniqueId().toString()+".home.world");
    }

    public static void setHome(Player p, Location l) throws IOException {
        String world = l.getWorld().getName();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Float yaw = l.getYaw();
        Float pitch = l.getPitch();
        VortechFactions.dataFileConfig.set(p.getUniqueId().toString()+".home.world", world);
        VortechFactions.dataFileConfig.set(p.getUniqueId().toString()+".home.x", x);
        VortechFactions.dataFileConfig.set(p.getUniqueId().toString()+".home.y", y);
        VortechFactions.dataFileConfig.set(p.getUniqueId().toString()+".home.z", z);
        VortechFactions.dataFileConfig.set(p.getUniqueId().toString()+".home.yaw", yaw.doubleValue());
        VortechFactions.dataFileConfig.set(p.getUniqueId().toString()+".home.pitch", pitch.doubleValue());
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    public static Location getHome(Player p){
        if(!(hasHome(p))) return null;
        return new Location(
                Bukkit.getWorld(VortechFactions.dataFileConfig.getString(p.getUniqueId().toString()+".home.world")),
                Double.valueOf(VortechFactions.dataFileConfig.getInt(p.getUniqueId().toString()+".home.x")),
                Double.valueOf(VortechFactions.dataFileConfig.getString(p.getUniqueId().toString()+".home.y")),
                Double.valueOf(VortechFactions.dataFileConfig.getString(p.getUniqueId().toString()+".home.z")),
                Double.valueOf(VortechFactions.dataFileConfig.getDouble(p.getUniqueId().toString()+".home.yaw")).floatValue(),
                Double.valueOf(VortechFactions.dataFileConfig.getDouble(p.getUniqueId().toString()+".home.pitch")).floatValue()
        );
    }


}
