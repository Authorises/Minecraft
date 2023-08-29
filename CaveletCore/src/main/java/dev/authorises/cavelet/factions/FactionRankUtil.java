package dev.authorises.cavelet.factions;

public class FactionRankUtil {
    public static FactionRank fromWeight(int weight){
        for(FactionRank rank : FactionRank.values()){
            if(rank.weight==weight){
                return rank;
            }
        }
        return null;
    }
}
