package dev.authorises.cavelet.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerRegisterer {

    public ListenerRegisterer(JavaPlugin plugin, Listener... listeners){
        for(Listener l : listeners){
            Bukkit.getPluginManager().registerEvents(l, plugin);
        }
    }
}
