package dev.authorises.vortechfactions.utilities;

import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Faction;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.Relationship;
import dev.authorises.vortechfactions.VortechFactions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class getFactionDisplay {

    public static String get(Player p, Faction f){
        String re="&cERROR_GETTING_FACTION_NAME";
        Faction ff = VortechFactions.api.getFaction(p);
        assert ff != null;
        Relationship r = ff.getRelationshipTo(f);
        if(r.equals(Relationship.MEMBER)){
            re="&a"+f.getName();
        }else if(r.equals(Relationship.ALLY)){
            re="&5"+f.getName();
        }else if(r.equals(Relationship.TRUCE)){
            re="&d"+f.getName();
        }else if(r.equals(Relationship.ENEMY)){
            re="&c"+f.getName();
        }else if(r.equals(Relationship.DEFAULT_RELATIONSHIP)){
            re="&b"+f.getName();
        }
        return ColorUtils.format(re);
    }

    public static String getColor(Player p, Faction f){
        String re="&cERROR_GETTING_FACTION_NAME";
        Faction ff = VortechFactions.api.getFaction(p);
        assert ff != null;
        Relationship r = ff.getRelationshipTo(f);
        if(r.equals(Relationship.MEMBER)){
            re="&a";
        }else if(r.equals(Relationship.ALLY)){
            re="&5";
        }else if(r.equals(Relationship.TRUCE)){
            re="&d";
        }else if(r.equals(Relationship.ENEMY)){
            re="&c";
        }else if(r.equals(Relationship.DEFAULT_RELATIONSHIP)){
            re="&b";
        }
        return ColorUtils.format(re);
    }

    public static String getFormatted(Player p, Faction f){
        String re="&cERROR_GETTING_FACTION_NAME";
        Faction ff = VortechFactions.api.getFaction(p);
        assert ff != null;
        Relationship r = ff.getRelationshipTo(f);
        if(r.equals(Relationship.MEMBER)){
            re="&a"+f.getName();
        }else if(r.equals(Relationship.ALLY)){
            re="&5"+f.getName();
        }else if(r.equals(Relationship.TRUCE)){
            re="&d"+f.getName();
        }else if(r.equals(Relationship.ENEMY)){
            re="&c"+f.getName();
        }else if(r.equals(Relationship.DEFAULT_RELATIONSHIP)){
            re="&b"+f.getName();
        }
        return ChatColor.translateAlternateColorCodes('&', re);
    }
}
