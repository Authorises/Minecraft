package dev.authorises.instinctiacore.objects;

public enum PunishmentType {
    warn("warn"),
    mute("mute"),
    ban("ban");

    public String name;

    PunishmentType(String s){
        this.name = s;
    }
}
