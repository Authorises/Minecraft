package dev.authorises.CentralC.model.access;

public class AccessToken {
    public final PermissionType permissions;
    public final String hash;

    public AccessToken(PermissionType permissions, String hash) {
        this.permissions = permissions;
        this.hash = hash;
    }

}
