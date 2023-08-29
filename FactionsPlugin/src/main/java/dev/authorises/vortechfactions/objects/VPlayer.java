package dev.authorises.vortechfactions.objects;

import java.util.UUID;

public class VPlayer {
    private String lastUsername;
    private UUID uuid;
    private boolean hasBundle;

    public VPlayer(String lastUsername, UUID uuid, boolean hasBundle){
        this.lastUsername = lastUsername;
        this.uuid = uuid;
        this.hasBundle = hasBundle;
    }

    public void setLastUsername(String lastUsername){
        this.lastUsername = lastUsername;
    }

    public String getLastUsername(){
        return this.lastUsername;
    }

    public boolean isHasBundle() {
        return hasBundle;
    }

    public void setHasBundle(boolean hasBundle) {
        this.hasBundle = hasBundle;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
