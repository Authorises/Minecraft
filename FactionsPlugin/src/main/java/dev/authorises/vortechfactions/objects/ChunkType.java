package dev.authorises.vortechfactions.objects;

public enum ChunkType {
    DEFAULT("Default", 100000, "&b"),
    RAID("Raid", 25, "&c"),
    CACTUS("Cactus", 64, "&a"),
    //STORAGE("Storage", 16, "&f"),
    SPAWNERS("Spawners", 16, "&d");

    final String name;
    final Integer maxNum;
    final String color;

    private ChunkType(String name, Integer maxNum, String color){
        this.name = name;
        this.maxNum = maxNum;
        this.color = color;
    }

    public String getName(){
        return this.name;
    }

    public Integer getMaxNum(){
        return this.maxNum;

    }

    public String getColor(){
        return this.color;
    }

}
