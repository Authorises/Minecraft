package dev.authorises.cavelet.mining;

import java.util.ArrayList;

public class OreManager {
    private ArrayList<Ore> ores;

    public OreManager(){
        this.ores = new ArrayList<>();
    }

    public ArrayList<Ore> getOres(){
        return ores;
    }

    public void setOres(ArrayList<Ore> ores){
        this.ores = ores;
    }

    public void addOre(Ore o){
        this.ores.add(o);
    }

    public void removeOre(Ore o){
        this.ores.remove(o);
    }


    public Ore getOre(String s){
        Ore o = null;
        for(Ore lo : ores){
            if(lo.getName().equalsIgnoreCase(s)){
                return lo;
            }
        }
        return o;
    }

    public Ore getOreFromID(String id){
        Ore o = null;
        for(Ore lo : ores){
            if(lo.getOreId().equalsIgnoreCase(id)){
                return lo;
            }
        }
        return o;
    }

}
