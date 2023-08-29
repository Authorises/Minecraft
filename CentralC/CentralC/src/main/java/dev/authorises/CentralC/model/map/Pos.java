package dev.authorises.CentralC.model.map;

public class Pos {
    public final Float x;
    public final Float y;
    public final Float z;
    public final Float yaw;
    public final Float pitch;
    public Pos(Float x, Float y, Float z, Float yaw, Float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String toString(){
        return x+"#"+y+"#"+z+"#"+yaw+"#"+pitch;
    }

    public static Pos fromString(String string){
        String[] s = string.split("#");
        if(s.length<5){
            return new Pos(Float.valueOf(s[0]), Float.valueOf(s[1]), Float.valueOf(s[2]), 1f, 1f);
        }else{
            return new Pos(Float.valueOf(s[0]), Float.valueOf(s[1]), Float.valueOf(s[2]), Float.valueOf(s[3]), Float.valueOf(s[4]));
        }

    }

}
