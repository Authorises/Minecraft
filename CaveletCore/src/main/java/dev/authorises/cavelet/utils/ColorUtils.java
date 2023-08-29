package dev.authorises.cavelet.utils;

import org.bukkit.ChatColor;

public class ColorUtils {

    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
