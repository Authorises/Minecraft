package dev.authorises.CentralC.model.access;

public enum PermissionType {
    GLOBAL(1),
    PRIVATE(2),
    INTERNAL(3);

    public int weight;
    PermissionType(int weight){
        this.weight=weight;
    }

}
