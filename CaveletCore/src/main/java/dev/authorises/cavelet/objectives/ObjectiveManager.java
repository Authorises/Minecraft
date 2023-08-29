package dev.authorises.cavelet.objectives;

import java.util.ArrayList;

public class ObjectiveManager {
    private ArrayList<Objective> objectives;

    public ObjectiveManager(){
        this.objectives = new ArrayList<>();
    }

    public Objective getObjective(String id){
        Objective o = null;
        for(Objective lo : objectives){
            if(lo.getId().equals(id)){
                return lo;
            }
        }
        return o;
    }

    public void addObjective(Objective o){
        this.objectives.add(o);
    }

    public void removeObjective(Objective o){
        this.objectives.remove(o);
    }

    public ArrayList<Objective> getObjectives() {
        return objectives;
    }

    public void setObjectives(ArrayList<Objective> objectives) {
        this.objectives = objectives;
    }
}
