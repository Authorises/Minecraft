package dev.authorises.CentralC.model.punishments;

import java.util.UUID;

public class Punishment {
    public UUID id;
    public PunishmentType type;
    public UUID player;
    public UUID punisher;
    public String reason;
    public Boolean active;
    public long date;

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
