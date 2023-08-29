package dev.authorises.lightweightlobby.map;

import net.minestom.server.item.Material;

public enum MapType {

    CHECKPOINT(Material.BEACON, "Checkpoint");

    public Material item;
    public String displayName;

    MapType(Material item, String displayName){
        this.item = item;
        this.displayName = displayName;
    }

}
