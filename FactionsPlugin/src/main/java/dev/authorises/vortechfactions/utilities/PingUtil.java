package dev.authorises.vortechfactions.utilities;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PingUtil {
    private static final Logger logger = Bukkit.getLogger();
    private static Class<?> craftPlayer;
    private static Method handle;
    private static Field pingField;
    private static Method playerGetPing;

    public static void log(String message) {
        logger.info("[PingPlayer] " + message);
    }

    public static int getPlayerPing(Player player) throws Exception {
        if (playerGetPing != null) {
            return (Integer)playerGetPing.invoke(player);
        } else {
            Object converted = craftPlayer.cast(player);
            Object entityPlayer = handle.invoke(converted);
            int ping = pingField.getInt(entityPlayer);
            return ping;
        }
    }

    public static String getServerVersion() {
        Pattern oldBrand = Pattern.compile("(v|)[0-9][_.][0-9][_.][R0-9]*");
        Pattern newBrand = Pattern.compile("(v|)[0-9][_.][0-9][0-9][_.][R0-9]*");
        String pkg = Bukkit.getServer().getClass().getPackage().getName();
        String version = pkg.substring(pkg.lastIndexOf(46) + 1);
        if (!oldBrand.matcher(version).matches() && !newBrand.matcher(version).matches()) {
            version = "";
        }

        return version;
    }

    static {
        try {
            playerGetPing = Player.class.getMethod("getPing");
            log("Detected Minecraft version 1.16+, using new ping method.");
        } catch (Exception var4) {
            try {
                String serverVersion = getServerVersion();
                craftPlayer = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
                Class<?> entityPlayer = Class.forName("net.minecraft.server." + serverVersion + ".EntityPlayer");
                handle = craftPlayer.getMethod("getHandle");
                pingField = entityPlayer.getField("ping");
            } catch (Exception var3) {
                log("Error occurred while loading the plugin, are you using MCPC or Cauldron?");
                var3.printStackTrace();
            }
        }

    }
}