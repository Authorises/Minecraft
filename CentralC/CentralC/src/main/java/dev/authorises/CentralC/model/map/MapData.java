package dev.authorises.CentralC.model.map;

import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class MapData {
    private List<UUID> creators;
    private final UUID uuid;
    private String name;
    private List<Pos> checkpoints;
    private MapType type;
    private Long likes;
    private String description;
    private String material;
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
        this.material = material;
        this.spawnPoint = spawnPoint;
        this.firstSave = firstSave;
        this.isVerified = isVerified;
        this.isPublic = isPublic;
    }

    public MapData(Document document){
        this.creators = document.getList("creators", String.class).stream().map((UUID::fromString)).toList();
        this.uuid = UUID.fromString(document.getString("_id"));
        this.name = document.getString("name");
        this.checkpoints = document.getList("checkpoints", String.class).stream().map(Pos::fromString).toList();
        this.type = MapType.valueOf(document.getString("type"));
        this.likes = document.getLong("likes");
        this.description = document.getString("description");
        this.material = document.getString("material");
        this.spawnPoint = Pos.fromString(document.getString("spawn"));
        this.firstSave = document.getBoolean("firstSave");
        this.isVerified = document.getBoolean("verified");
        this.isPublic = document.getBoolean("public");
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public Boolean getPublic() {
        return isPublic;
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Pos getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Pos spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public Boolean getFirstSave() {
        return firstSave;
    }

    public void setFirstSave(Boolean firstSave) {
        this.firstSave = firstSave;
    }
}
