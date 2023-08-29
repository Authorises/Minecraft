package dev.authorises.cavelet.factions;

import com.mongodb.client.FindIterable;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.LocationUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class MFaction implements Comparable<MFaction> {

    private UUID id;
    private String name;
    private UUID owner;
    private HashMap<UUID, FactionRank> members;
    public HashMap<UUID, String> lastUsernames;
    private List<UUID> invitedPlayers;
    private Long dateCreated;
    private String description;
    public Location factionGateway;
    public Integer lastUpdatedPoints;

    // Loading already existing faction from memory
    public MFaction(UUID factionId){
        FindIterable<Document> find = Cavelet.factionsData.find(new Document("_id", factionId.toString()));
        if(find.first()!=null){
            Document faction = find.first();
            this.loadFromDocument(faction);

        }

    }

    // Create a faction based on a pre-existing document
    public MFaction(Document document){
        this.loadFromDocument(document);
    }

    // Load a faction from a document
    private void loadFromDocument(Document faction){
        this.id = UUID.fromString(faction.getString("_id"));
        this.name = faction.getString("name");
        this.owner = UUID.fromString(faction.getString("owner"));

        this.members = parseMembers(faction.get("members", Document.class));
        this.lastUsernames = parseUsernames(faction.get("usernames", Document.class));
        this.description = faction.getString("description");
        this.dateCreated = faction.getLong("created");
        this.factionGateway = LocationUtil.stringToLocation(faction.getString("gateway"));
        this.invitedPlayers = new ArrayList<>();
        this.lastUpdatedPoints = 0;
    }

    // Save a faction for the first time, inserting it into mongo
    private boolean saveFirst(){

        Document document = new Document();
        document.put("_id", this.id.toString());
        document.put("name", this.name);
        document.put("owner", this.owner.toString());
        document.put("members", parseMembers());
        document.put("usernames", parseUsernames());
        document.put("description", this.description);
        document.put("created", this.dateCreated);
        document.put("gateway", LocationUtil.locationToString(this.factionGateway));

        Cavelet.factionsData.insertOne(document);

        return true;
    }

    // Update a faction's data to mongo
    public void updateData(){
        Cavelet.factionsData.updateOne(new Document("_id", this.id.toString()), combine(
                set("name", this.name),
                set("owner", this.owner.toString()),
                set("members", parseMembers()),
                set("usernames", parseUsernames()),
                set("description", this.description),
                set("gateway", LocationUtil.locationToString(this.factionGateway))
        ));
    }

    private HashMap<String, String> parseMembers(){
        HashMap<String, String> r = new HashMap<>();
        members.forEach((uuid, rank) -> {
            r.put(uuid.toString(), rank.toString());
        });
        return r;
    }

    private HashMap<UUID, FactionRank> parseMembers(Document data){
        HashMap<UUID, FactionRank> r = new HashMap<>();
        data.forEach((uuid, rank) -> {
            r.put(UUID.fromString(uuid), FactionRank.valueOf((String) rank));
        });
        return r;
    }

    private HashMap<String, String> parseUsernames(){
        HashMap<String, String> r = new HashMap<>();
        lastUsernames.forEach((uuid, username) -> {
            r.put(uuid.toString(), username);
        });
        return r;
    }

    private HashMap<UUID, String> parseUsernames(Document data){
        HashMap<UUID, String> r = new HashMap<>();
        data.forEach((uuid, username) -> {
            r.put(UUID.fromString(uuid), (String)username);
        });
        return r;
    }

    // Creating new faction with parameters
    public MFaction(String name, Player owner){
        this.id = UUID.randomUUID();
        this.name = name;
        this.owner = owner.getUniqueId();
        this.members = new HashMap<>();
        this.lastUsernames = new HashMap<>();
        this.members.put(owner.getUniqueId(), FactionRank.OWNER);
        this.lastUsernames.put(owner.getUniqueId(), owner.getName());
        this.invitedPlayers = new ArrayList<>();
        this.description = "Default description.";
        this.dateCreated = System.currentTimeMillis();
        this.factionGateway = null;
        this.lastUpdatedPoints = 0;
        if(!(saveFirst())){
            owner.sendMessage(ColorUtils.format("&cAn error occurred creating your faction. Please contact staff."));
        }

    }

    // debug fac
    public MFaction(){
        this.id = UUID.randomUUID();
        this.name = UUID.randomUUID().toString();
        this.owner = UUID.randomUUID();
        this.members = new HashMap<>();
        this.lastUsernames = new HashMap<>();
        this.members.put(this.owner, FactionRank.OWNER);
        this.lastUsernames.put(this.owner, "debugName");
        this.invitedPlayers = new ArrayList<>();
        this.description = "debug faction";
        this.dateCreated = System.currentTimeMillis();
        this.factionGateway = null;
        this.lastUpdatedPoints = 0;
    }


    public List<Player> getOnlinePlayers(){
        ArrayList<Player> online = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            if(this.members.containsKey(p.getUniqueId())) online.add(p);
        }
        return online;
    }

    public UUID getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }


    public Long getDateCreated() {
        return dateCreated;
    }

    public HashMap<UUID, FactionRank> getMembers() {
        return members;
    }

    public void setMembers(HashMap<UUID, FactionRank> members) {
        this.members = members;
    }

    public List<UUID> getInvitedPlayers() {
        return invitedPlayers;
    }

    public void setInvitedPlayers(List<UUID> invitedPlayers) {
        this.invitedPlayers = invitedPlayers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(@NotNull MFaction o) {
        if(lastUpdatedPoints == null || o.lastUpdatedPoints == null){
            return 0;
        }
        return lastUpdatedPoints.compareTo(o.lastUpdatedPoints);
    }
}
