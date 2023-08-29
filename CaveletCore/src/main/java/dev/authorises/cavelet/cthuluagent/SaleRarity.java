package dev.authorises.cavelet.cthuluagent;

import java.util.concurrent.ThreadLocalRandom;

public enum SaleRarity {
    NORMAL("<#E4572E>", 56D, "Normal"),
    RARE("<#29335C>", 26D, "Rare"),
    SUPER_RARE("<#F3A712>", 10D, "Super-Rare"),
    DELUXE("<#A8C686>", 5D, "Deluxe"),
    UBER_DELUXE("<#669BBC>", 2.5D,"Uber-Deluxe"),
    SINGULARITY("<#7619e0>", 0.5D, "Singularity");


    public String color;
    public Double percent;
    public String name;

    SaleRarity(String color, Double percent, String name){
        this.color = color;
        this.percent = percent;
        this.name = name;
    }

    private static double nextDoubleBetween(double min, double max) {
        return (ThreadLocalRandom.current().nextDouble() * (max - min)) + min;
    }

    public static SaleRarity pickRandom(){
        Double d = nextDoubleBetween(0D, 99.999D);
        for(SaleRarity s : SaleRarity.values()){
            if(s.percent<d){
                return s;
            }else{
                d+=s.percent;
            }
        }
        return null;
    }


}
