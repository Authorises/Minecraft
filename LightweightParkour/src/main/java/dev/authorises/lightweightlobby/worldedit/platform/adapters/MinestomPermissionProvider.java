package dev.authorises.lightweightlobby.worldedit.platform.adapters;

import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.game.MapEditor;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.PermissionHandler;

import java.util.List;
import java.util.function.BiPredicate;

public class MinestomPermissionProvider {

    private static BiPredicate<Player, String> permissionHandler = PermissionHandler::hasPermission;
    private static List<String> blacklistedPrefixes = List.of(
            "worldedit.navigation",
            "worldedit.calc",
            "worldedit.snapshots",
            "worldedit.setwand",
            "worldedit.clipboard.load",
            "worldedit.clipboard.save",
            "worldedit.schematic",
            "worldedit.timeout",
            "worldedit.inventory",
            "worldedit.limit",
            "worldedit.anyblock",
            "worldedit.setnbt",
            "worldedit.override",
            "worldedit.report",
            "worldedit.scripting"

    );

    public static void setPermissionHandler(BiPredicate<Player, String> permissionHandler) {
        MinestomPermissionProvider.permissionHandler = permissionHandler;
    }

    public static boolean hasPermission(Player player, String permission) {
        for(String s : blacklistedPrefixes){
            if(permission.startsWith(s)){
                return false;
            }
        };
        return Server.playersInSession.containsKey(player) && Server.playersInSession.get(player) instanceof MapEditor;
        //return permissionHandler.test(player, permission);
    }

}