package dev.authorises.lightweightlobby.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.lightweightlobby.Server;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapData {
    private List<UUID> creators;
    private UUID uuid;
    private String name;
    private List<Pos> checkpoints;
    private MapType type;
    private Long likes;
    private String description;
    private Material material;
    private Pos spawnPoint;
    private Boolean firstSave;
    private Boolean isVerified;
    private Boolean isPublic;

    public MapData(List<UUID> creators, UUID uuid, String name, List<Pos> checkpoints, MapType type, Long likes, String description, String material, Pos spawnPoint, Boolean firstSave, Boolean isVerified, Boolean isPublic) {
        this.creators = creators;
        this.uuid = uuid;
        this.name = name;
        this.checkpoints = checkpoints;
        this.type = type;
        this.likes = likes;
        this.description = description;
        this.material = Material.fromNamespaceId(material);
        this.spawnPoint = spawnPoint;
        this.firstSave = firstSave;
        this.isVerified = isVerified;
        this.isPublic = isPublic;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getFirstSave() {
        return firstSave;
    }

    public void setFirstSave(Boolean firstSave) {
        this.firstSave = firstSave;
    }

    public List<UUID> getCreators() {
        return creators;
    }

    public void setCreators(List<UUID> creators) {
        this.creators = creators;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pos> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Pos> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public MapType getType() {
        return type;
    }

    public void setType(MapType type) {
        this.type = type;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Pos getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Pos spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public MapData (JsonObject jsonObject){

        this.uuid = UUID.fromString(jsonObject.get("uuid").getAsString());

        this.checkpoints = new ArrayList<>();
        jsonObject.get("checkpoints").getAsJsonArray().forEach((element) -> {
            this.checkpoints.add(new Pos(element.getAsJsonObject().get("x").getAsDouble(), element.getAsJsonObject().get("y").getAsDouble(), element.getAsJsonObject().get("z").getAsDouble()));
        });
        this.creators = new ArrayList<>();
        jsonObject.get("creators").getAsJsonArray().forEach((element) -> {
            this.creators.add(UUID.fromString(element.getAsString()));
        });
        this.description = jsonObject.get("description").getAsString();
        this.likes = jsonObject.get("likes").getAsLong();
        this.material = Material.fromNamespaceId(jsonObject.get("material").getAsString());
        this.name = jsonObject.get("name").getAsString();
        this.firstSave = jsonObject.get("firstSave").getAsBoolean();
        this.isVerified = jsonObject.get("isVerified").getAsBoolean();
        this.isPublic = jsonObject.get("isPublic").getAsBoolean();
        this.type = MapType.valueOf(jsonObject.get("type").getAsString());
        JsonObject e = jsonObject.get("spawnPoint").getAsJsonObject();
        if(e.has("yaw") && e.has("pitch")){
            this.spawnPoint = new Pos(e.get("x").getAsDouble(), e.get("y").getAsDouble(), e.get("z").getAsDouble(), e.get("yaw").getAsFloat(), e.get("pitch").getAsFloat());
        }else{
            this.spawnPoint = new Pos(e.get("x").getAsDouble(), e.get("y").getAsDouble(), e.get("z").getAsDouble());
        }

    }

    public String toJson(){

        JsonObject object = new JsonObject();
        object.addProperty("description", getDescription());

        object.addProperty("likes", getLikes());

        object.addProperty("material", getMaterial().toString());

        object.addProperty("name", getName());

        object.add("spawnPoint", Server.gson.toJsonTree(spawnPoint));

        object.add("checkpoints", Server.gson.toJsonTree(checkpoints));

        object.addProperty("uuid", getUuid().toString());

        object.addProperty("type", "CHECKPOINT");

        object.addProperty("firstSave", firstSave);

        object.addProperty("isVerified", isVerified);

        object.addProperty("isPublic", isPublic);

        JsonArray array = new JsonArray();
        this.creators.forEach((creator) -> {
            array.add(creator.toString());
        });

        object.add("creators", array);

        return object.toString();
    }

}
