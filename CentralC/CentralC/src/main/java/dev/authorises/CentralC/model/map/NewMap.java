package dev.authorises.CentralC.model.map;

import java.util.List;
import java.util.UUID;

public class NewMap {
    private final UUID creator;
    private final String name;
    private final MapType type;

    //
    public NewMap(String creator, String name, MapType type){
        this.creator = UUID.fromString(creator);
        this.name = name;
        this.type = type;
    }

    public MapData generate(){
        return new MapData(
                List.of(creator),
                UUID.randomUUID(),
                name,
                List.of(),
                type,
                0L,
                "Default map description",
                "minecraft:stone",
                new Pos(0f, 15f, 0f, 0f, 0f),
                false,
                false,
                false
        );
    }

    public UUID getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public MapType getType() {
        return type;
    }

}
