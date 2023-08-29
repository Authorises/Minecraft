package dev.authorises.cavelet.playerdata;

import com.mongodb.client.FindIterable;
import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.BasicCustomItem;
import dev.authorises.cavelet.customitems.CItem;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.eventsystem.Boosters;
import dev.authorises.cavelet.exceptions.InvalidItemIdException;
import dev.authorises.cavelet.objectives.Objective;
import dev.authorises.cavelet.utils.BukkitSerialization;
import dev.authorises.cavelet.utils.ChatUtil;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Updates.*;

public class MProfile {
    private String lastUsername;
    private UUID uuid;
    private Double balance;
    private Double darkSouls;
    private Double kills;
    private Double deaths;
    private Integer farmingXP;
    private Integer farmingLevel;
    private Integer miningXP;
    private Integer miningLevel;
    private Integer combatXP;
    private Integer combatLevel;
    private List<Objective> completedObjectives;
    private BossBar currentBossBar;
    public boolean combatLogDead = false;
    public List<String> claims;
    private Player p;
    public Integer combatLogLeft;


    public void savefirst() {
        List<String> objectiveIds = new ArrayList<>();
        this.completedObjectives.forEach(objective -> {
            objectiveIds.add(objective.getId());
        });
        Cavelet.playerData.insertOne(new Document("_id", this.uuid.toString())
                .append("last-username", this.lastUsername)
                .append("balance", this.balance)
                .append("dark-souls", this.darkSouls)
                .append("kills", this.kills)
                .append("deaths", this.deaths)
                .append("skills", new Document("farming-level", this.farmingLevel)
                        .append("farming-xp", this.farmingXP)
                        .append("mining-level", this.miningLevel)
                        .append("mining-xp", this.miningXP)
                        .append("combat-level", this.combatLevel)
                        .append("combat-xp", this.combatXP))
                .append("completed-objectives", objectiveIds)
                .append("claims", claims))
        ;
        Cavelet.cachedMPlayers.put(this.uuid, this);
    }

    public void save(){
        Cavelet.playerData.updateOne(new Document("_id", this.uuid.toString()), combine(
                    set("last-username", this.lastUsername),
                    set("balance", this.balance),
                    set("dark-souls", this.darkSouls),
                    set("kills", this.kills),
                    set("deaths", this.deaths),
                    set("skills", new Document("farming-level", this.farmingLevel)
                            .append("farming-xp", this.farmingXP)
                            .append("mining-level", this.miningLevel)
                            .append("mining-xp", this.miningXP)
                            .append("combat-level", this.combatLevel)
                            .append("combat-xp", this.combatXP)),
                    set("claims", claims),
                    set("combatLogDead", combatLogDead)
                ));

    }

    public void save(Player p){
        this.p = p;
        this.clearBossBar(p);
        ArrayList<String> inventory = new ArrayList<>(List.of(BukkitSerialization.playerInventoryToBase64(p.getInventory())));
        String echest = BukkitSerialization.itemStackArrayToBase64(p.getEnderChest().getContents());

        List<String> objectiveIds = new ArrayList<>();
        this.completedObjectives.forEach(objective -> {
            try {
                objectiveIds.add(objective.getId());
            }catch (Exception e){

            }
        });
        p.sendMessage(ColorUtils.format("&aSaving your data..."));
        CompletableFuture.runAsync(() -> {
            Cavelet.playerData.updateOne(new Document("_id", this.uuid.toString()), combine(
                    set("last-username", this.lastUsername),
                    set("inventory", inventory),
                    set("ender-chest", echest),
                    set("balance", this.balance),
                    set("dark-souls", this.darkSouls),
                    set("kills", this.kills),
                    set("deaths", this.deaths),
                    set("skills", new Document("farming-level", this.farmingLevel)
                            .append("farming-xp", this.farmingXP)
                            .append("mining-level", this.miningLevel)
                            .append("mining-xp", this.miningXP)
                            .append("combat-level", this.combatLevel)
                            .append("combat-xp", this.combatXP)),
                    set("completed-objectives", objectiveIds),
                    set("claims", claims),
                    set("combatLogDead", combatLogDead)));
        }).thenRun(() -> {
            Cavelet.cachedMPlayers.put(p.getUniqueId(), this);
            p.sendMessage(ColorUtils.format("&aYour data has been saved!"));
        });


    }

    private void generateNewData(Player p) throws IOException {
        this.lastUsername = p.getName();
        this.uuid = p.getUniqueId();
        this.balance = 0D;
        this.darkSouls = 0D;
        this.kills = 0D;
        this.deaths = 0D;
        this.farmingLevel = 1;
        this.miningLevel = 1;
        this.farmingXP = 0;
        this.miningXP = 0;
        this.combatLevel = 1;
        this.combatXP = 0;
        this.completedObjectives = new ArrayList<>();
        this.claims = new ArrayList<>();

        this.savefirst();

        for(Player lp : Bukkit.getOnlinePlayers()){
            lp.sendMessage(ColorUtils.format("&bA player has joined for the first time: &f"+p.getName()));
        }

        p.getInventory().clear();
        p.getEnderChest().clear();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Cavelet.getPlugin(Cavelet.class), () -> {
            p.teleport(new Location(Bukkit.getWorlds().get(0), 3, 54, -1));
            p.getInventory().addItem(new CItem(Cavelet.customItemsManager.getItemById("STARTER_PACK")).getItemStack());
        }, 3L);

    }

    public void loadInventory(Player p) throws IOException, InvalidItemIdException, ParseException {

        if(combatLogDead){
            p.getInventory().clear();
            p.sendMessage(ColorUtils.format("&7You logged out in combat, so you were killed while you were offline."));
            p.setHealth(0.0);
            this.combatLogDead = false;
            this.save();
            return;
        }

        FindIterable<Document> f = Cavelet.playerData.find(new Document("_id", uuid.toString()));
        if(f.first()!=null){
            Document d = f.first();
            if(d.containsKey("inventory")){
                String[] inventory = ((ArrayList<String>) d.get("inventory")).toArray(new String[0]);
                p.getInventory().setArmorContents(BukkitSerialization.itemStackArrayFromBase64(inventory[1]));
                p.getInventory().setContents(BukkitSerialization.itemStackArrayFromBase64(inventory[0]));
            }
            if(d.containsKey("ender-chest")){
                String echest = d.getString("ender-chest");
                p.getEnderChest().setContents(BukkitSerialization.itemStackArrayFromBase64(echest));
            }
            this.reloadSkillsPerks(p);

        }
    }

    private Boolean loadData(UUID uuid, Boolean online){
        FindIterable<Document> f = Cavelet.playerData.find(new Document("_id", uuid.toString()));
        if(f.first()!=null){
            Document d = f.first();
            this.uuid = uuid;
            this.lastUsername = d.getString("last-username");
            this.balance = d.getDouble("balance");
            this.darkSouls = d.getDouble("dark-souls");
            this.kills = d.getDouble("kills");
            this.deaths = d.getDouble("deaths");
            Document skills = (Document) d.get("skills");
            this.farmingLevel = skills.getInteger("farming-level");
            this.farmingXP = skills.getInteger("farming-xp");
            this.miningLevel = skills.getInteger("mining-level");
            this.miningXP = skills.getInteger("mining-xp");
            this.combatLevel = skills.getInteger("combat-level");
            this.combatXP = skills.getInteger("combat-xp");
            this.claims = d.getList("claims", String.class);
            if(d.containsKey("combatLogDead")) {
                this.combatLogDead = d.getBoolean("combatLogDead");
            }
            this.completedObjectives = new ArrayList<>();
            List<String> s = d.getList("completed-objectives", String.class);
            s.forEach(ls -> {
                this.completedObjectives.add(Cavelet.objectiveManager.getObjective(ls));
            });
            if(online){
                Cavelet.cachedMPlayers.put(this.uuid, this);
            }
            return true;
        }

        return false;
    }

    public MProfile(UUID uuid) throws Exception {
        Boolean b = loadData(uuid, false);
        if(!b){
            throw new Exception("You cannot generate playerdata with a UUID, you must pass a player object");
        }
    }

    public MProfile(UUID uuid, Boolean online) throws Exception {
        Boolean b = loadData(uuid, online);
        if(!b){
            throw new Exception("You cannot generate playerdata with a UUID, you must pass a player object");
        }
    }

    public MProfile(Document d) throws Exception {
        this.uuid = uuid;
        this.lastUsername = d.getString("last-username");
        this.balance = d.getDouble("balance");
        this.darkSouls = d.getDouble("dark-souls");
        this.kills = d.getDouble("kills");
        this.deaths = d.getDouble("deaths");
        Document skills = (Document) d.get("skills");
        this.farmingLevel = skills.getInteger("farming-level");
        this.farmingXP = skills.getInteger("farming-xp");
        this.miningLevel = skills.getInteger("mining-level");
        this.miningXP = skills.getInteger("mining-xp");
        this.combatLevel = skills.getInteger("combat-level");
        this.combatXP = skills.getInteger("combat-xp");
        this.completedObjectives = new ArrayList<>();
        List<String> s = d.getList("completed-objectives", String.class);
        s.forEach(ls -> {
            this.completedObjectives.add(Cavelet.objectiveManager.getObjective(ls));
        });
        Cavelet.cachedMPlayers.put(this.uuid, this);
    }

    public MProfile(Player p) throws IOException {
        if(Cavelet.playerData.find(new Document("_id", p.getUniqueId().toString())).first()!=null){
            this.loadData(p.getUniqueId(), true);
        }else{
            this.generateNewData(p);
        }
    }

    public void showBossBar(Player p, BossBar b){
        clearBossBar(p);
        setCurrentBossBar(b);
        p.showBossBar(b);
    }

    public void clearBossBar(Player p){
        if(this.currentBossBar!=null){
            Bukkit.getBossBars().forEachRemaining((bossbar) -> {
                bossbar.removePlayer(p);
            });
            this.currentBossBar = null;
        }
    }

    public void levelUpMining(Player p){
        this.miningXP = 0;
        this.miningLevel+=1;
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1F, 1F);
        p.showTitle(Title.title(Component.text(ColorUtils.format("&b&lLevel Up")), Component.text(ColorUtils.format("&7Now &bMining Level &7"+this.miningLevel))));
        this.reloadSkillsPerks(p);
    }


    public void addMiningXP(int xp, Player p){
        xp = p.applyMending(xp);
        if(this.miningLevel>=50) {
            p.sendActionBar(Component.text(ColorUtils.format("&8[&b"+this.miningLevel+"&8] &bMAXED")));
            return;
        };
        double neededXP = (200*(Math.pow(this.miningLevel, 2)));
        xp =  Double.valueOf(xp * Boosters.miningBooster).intValue();
        Random r = new Random();
        if(r.nextInt(0, 400)==1){
            CItemBlueprint<?> mCrystal = Cavelet.customItemsManager.getItemById("MINERS_CRYSTAL");
            addClaim(p, mCrystal);
            p.playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1F, 1F);
            p.sendMessage(Cavelet.miniMessage.deserialize("<white>You were <aqua><bold>Lucky<reset><white> and found a ").append(ChatUtil.getItemComponent(mCrystal)).append(Cavelet.miniMessage.deserialize("<white>. Claim it in /claim")));
        }

        if((this.miningXP+xp)>=neededXP){
            this.levelUpMining(p);
            p.sendActionBar(Component.text(ColorUtils.format("&8[&b"+this.miningLevel+"&8] &8+"+xp+"xp &8[&b"+formatDouble(this.miningXP.doubleValue())+"/"+formatDouble(neededXP)+"&8]")));
        }else{
            this.miningXP+=xp;
            p.sendActionBar(Component.text(ColorUtils.format("&8[&b"+this.miningLevel+"&8] &8+"+xp+"xp &8[&b"+formatDouble(this.miningXP.doubleValue())+"/"+formatDouble(neededXP)+"&8]")));
        }
    }

    private String formatDouble(Double d){
        return String.format("%,.0f", d);
    }

    public void levelUpCombat(Player p){
        this.combatXP = 0;
        this.combatLevel+=1;
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1F, 1F);
        p.showTitle(Title.title(Component.text(ColorUtils.format("&c&lLevel Up")), Component.text(ColorUtils.format("&7Now &cCombat Level &7"+this.combatLevel))));
        this.reloadSkillsPerks(p);
    }


    public void addCombatXP(int xp, Player p){
        xp = p.applyMending(xp);
        xp =  Double.valueOf(xp * Boosters.combatBooster).intValue();

        double neededXP = (200*(Math.pow(this.combatLevel, 2)));

        Random r = new Random();
        if(r.nextInt(0, 600)==1){
            CItemBlueprint<?> cGauntlet = Cavelet.customItemsManager.getItemById("COMBAT_GAUNTLET");
            addClaim(p, cGauntlet);
            p.playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1F, 1F);
            p.sendMessage(Cavelet.miniMessage.deserialize("<white>You were <aqua><bold>Lucky<reset><white> and found a ").append(ChatUtil.getItemComponent(cGauntlet)).append(Cavelet.miniMessage.deserialize("<white>. Claim it in /claim")));
        }

        if(this.combatLevel>=50) {
            p.sendActionBar(Component.text(ColorUtils.format("&8[&c"+this.combatLevel+"&8] &cMAXED")));
            return;
        };
        if((this.combatXP+xp)>=neededXP){
            this.levelUpCombat(p);
            p.sendActionBar(Component.text(ColorUtils.format("&8[&c"+this.combatLevel+"&8] &8+"+xp+"xp &8[&c"+formatDouble(this.combatXP.doubleValue())+"/"+formatDouble(neededXP)+"&8]")));
        }else{
            this.combatXP+=xp;
            p.sendActionBar(Component.text(ColorUtils.format("&8[&c"+this.combatLevel+"&8] &8+"+xp+"xp &8[&c"+formatDouble(this.combatXP.doubleValue())+"/"+formatDouble(neededXP)+"&8]")));
        }
    }

    public void addCombatXP(int xp, Player p, int soulsAdded){
        xp = p.applyMending(xp);
        xp =  Double.valueOf(xp * Boosters.combatBooster).intValue();

        double neededXP = (200*(Math.pow(this.combatLevel, 2)));

        Random r = new Random();
        if(r.nextInt(0, 600)==1){
            CItemBlueprint<?> cGauntlet = Cavelet.customItemsManager.getItemById("COMBAT_GAUNTLET");
            addClaim(p, cGauntlet);
            p.playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1F, 1F);
            p.sendMessage(Cavelet.miniMessage.deserialize("<white>You were <aqua><bold>Lucky<reset><white> and found a ").append(ChatUtil.getItemComponent(cGauntlet)).append(Cavelet.miniMessage.deserialize("<white>. Claim it in /claim")));
        }

        if(this.combatLevel>=50) {
            p.sendActionBar(Component.text(ColorUtils.format("&8[&c"+this.combatLevel+"&8] &cMAXED")));
            return;
        };

        if((this.combatXP+xp)>=neededXP){
            this.levelUpCombat(p);
            p.sendActionBar(Component.text(ColorUtils.format("&8[&c"+this.combatLevel+"&8] &8+"+xp+"xp &8[&c"+formatDouble(this.combatXP.doubleValue())+"/"+formatDouble(neededXP)+"&8] &d+"+soulsAdded)));
        }else{
            this.combatXP+=xp;
            p.sendActionBar(Component.text(ColorUtils.format("&8[&c"+this.combatLevel+"&8] &8+"+xp+"xp &8[&c"+formatDouble(this.combatXP.doubleValue())+"/"+formatDouble(neededXP)+"&8] &d+"+soulsAdded)));
        }
    }

    public void levelUpFarming(Player p){
        this.farmingXP = 0;
        this.farmingLevel+=1;
        p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1F, 1F);
        p.showTitle(Title.title(Component.text(ColorUtils.format("&e&lLevel Up")), Component.text(ColorUtils.format("&7Now &eFarming Level &7"+this.farmingLevel))));
        this.reloadSkillsPerks(p);
    }


    public void addFarmingXP(int xp, Player p){
        xp = p.applyMending(xp);

        xp =  Double.valueOf(xp * Boosters.farmingBooster).intValue();

        double neededXP = (200*(Math.pow(this.farmingLevel, 2)));

        Random r = new Random();
        if(r.nextInt(0, 500)==1){
            CItemBlueprint<?> hBox = Cavelet.customItemsManager.getItemById("HARVEST_BOX");
            addClaim(p, hBox);
            p.playSound(p.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1F, 1F);
            p.sendMessage(Cavelet.miniMessage.deserialize("<white>You were <aqua><bold>Lucky<reset><white> and found a ").append(ChatUtil.getItemComponent(hBox)).append(Cavelet.miniMessage.deserialize("<white>. Claim it in /claim")));
        }

        if(this.farmingLevel>=50) {
            p.sendActionBar(Component.text(ColorUtils.format("&8[&e"+this.farmingLevel+"&8] &eMAXED")));
            return;
        };

        if((this.farmingXP+xp)>=neededXP){
            this.levelUpFarming(p);
            p.sendActionBar(Component.text(ColorUtils.format("&8[&e"+this.farmingLevel+"&8] &8+"+xp+"xp &8[&e"+formatDouble(this.farmingXP.doubleValue())+"/"+formatDouble(neededXP)+"&8]")));
        }else{
            this.farmingXP+=xp;
            p.sendActionBar(Component.text(ColorUtils.format("&8[&e"+this.farmingLevel+"&8] &8+"+xp+"xp &8[&e"+formatDouble(this.farmingXP.doubleValue())+"/"+formatDouble(neededXP)+"&8]")));
        }
    }

    public void reloadSkillsPerks(Player p){
        double addedHearts = Math.floor(farmingLevel.floatValue()/5F);
        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20+(2*addedHearts));
    }

    public void addClaim(Player p, CItemBlueprint<?> blueprint){
        this.claims.add(blueprint.getId());
    }

    public void addClaim(CItemBlueprint<?> blueprint){
        this.claims.add(blueprint.getId());
    }

    public List<Objective> getCompletedObjectives() {
        return completedObjectives;
    }

    public void setCompletedObjectives(List<Objective> completedObjectives) {
        this.completedObjectives = completedObjectives;
    }

    public Integer getFarmingXP() {
        return farmingXP;
    }

    public void setFarmingXP(Integer farmingXP) {
        this.farmingXP = farmingXP;
    }

    public Integer getFarmingLevel() {
        return farmingLevel;
    }

    public void setFarmingLevel(Integer farmingLevel) {
        this.farmingLevel = farmingLevel;
    }

    public Integer getMiningXP() {
        return miningXP;
    }

    public void setMiningXP(Integer miningXP) {
        this.miningXP = miningXP;
    }

    public Integer getMiningLevel() {
        return miningLevel;
    }

    public Double getSellMultiplier(){
        return 1+(Math.floor(getMiningLevel().floatValue()/5F)*10)/100;
    }

    public Double getDarkSoulsMultiplier(){
        return 1+(Math.floor(getCombatLevel().floatValue()/5F)*15)/100;
    }

    public void setMiningLevel(Integer miningLevel) {
        this.miningLevel = miningLevel;
    }

    public String getLastUsername() {
        return lastUsername;
    }

    public void setLastUsername(String lastUsername) {
        this.lastUsername = lastUsername;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getDarkSouls() {
        return darkSouls;
    }

    public void setDarkSouls(Double darkSouls) {
        this.darkSouls = darkSouls;
    }

    public Double getKills() {
        return kills;
    }

    public void setKills(Double kills) {
        this.kills = kills;
    }

    public Double getDeaths() {
        return deaths;
    }

    public void setDeaths(Double deaths) {
        this.deaths = deaths;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Integer getCombatXP() {
        return combatXP;
    }

    public void setCombatXP(Integer combatXP) {
        this.combatXP = combatXP;
    }

    public Integer getCombatLevel() {
        return combatLevel;
    }

    public void setCombatLevel(Integer combatLevel) {
        this.combatLevel = combatLevel;
    }

    public BossBar getCurrentBossBar() {
        return currentBossBar;
    }

    public void setCurrentBossBar(BossBar currentBossBar) {
        this.currentBossBar = currentBossBar;
    }

}
