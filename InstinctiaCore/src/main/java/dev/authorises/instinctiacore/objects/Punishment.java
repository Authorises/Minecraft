package dev.authorises.instinctiacore.objects;

import java.util.UUID;

public class Punishment {
    private UUID id;
    private PunishmentType type;
    private UUID player;
    private UUID punisher;
    private String reason;
    private boolean active;
    private long date;

    public UUID getID(){
        return id;
    }

    public PunishmentType getType(){
        return type;
    }

    public UUID getPlayer(){
        return player;
    }

    public UUID getPunisher(){
        return punisher;
    }

    public String getReason(){
        return reason;
    }

    public boolean isActive(){
        return active;
    }

    public void undo(){
        this.active = false;
    }

    public void redo(){
        this.active = true;
    }

    public long getDate(){
        return this.date;
    }

    public Punishment(UUID id, PunishmentType punishmentType, UUID player, UUID punisher, String reason, Boolean active, long date) {
        this.id = id;
        this.type = punishmentType;
        this.player = player;
        this.punisher = punisher;
        this.reason = reason;
        this.active = active;
        this.date = date;
    }
}
