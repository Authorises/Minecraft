package dev.authorises.cavelet.factions;

import scala.Int;

public enum FactionRank {

    OWNER("Owner", "#e76f51", 5),
    OFFICER("Officer", "#f4a261", 4),
    TRUSTED("Trusted", "#2a9d8f", 3),
    RECRUIT("Recruit", "#264653", 2);

    public final String display;
    public final String colour;
    public final Integer weight;

    FactionRank(String display, String colour, Integer weight){
        this.display = display;
        this.colour = colour;
        this.weight = weight;
    }

}