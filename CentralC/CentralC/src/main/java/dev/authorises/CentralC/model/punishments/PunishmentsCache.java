package dev.authorises.CentralC.model.punishments;

import java.util.HashMap;
import java.util.UUID;

public class PunishmentsCache {
    public HashMap<UUID, Punishment> cachedPunishments;

    public PunishmentsCache(){
        this.cachedPunishments = new HashMap<UUID, Punishment>();
    }
}
