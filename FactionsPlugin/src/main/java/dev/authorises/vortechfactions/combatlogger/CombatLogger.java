package dev.authorises.vortechfactions.combatlogger;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CombatLogger {
    private HashMap<UUID, Integer> combatTimer;
    private List<UUID> deadPlayers;

    public CombatLogger() {
        if(VortechFactions.dataFileConfig.contains("combatlog")){
            deadPlayers = VortechFactions.dataFileConfig.getStringList("combatlog")
                    .stream()
                    .map(UUID::fromString).
                    collect(Collectors.toList());
        }else{
            deadPlayers = new ArrayList<>();
        }

        this.combatTimer = new HashMap<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(VortechFactions.getPlugin(VortechFactions.class), new Runnable() {
            @Override
            public void run() {
                for(UUID u : combatTimer.keySet()){
                    if(Bukkit.getOnlinePlayers().stream().map(p -> p.getUniqueId()).collect(Collectors.toList()).contains(u)) {
                        Player p = Bukkit.getPlayer(u);
                        Integer newV = combatTimer.get(p.getUniqueId()) - 1;
                        if (newV == 0) {
                            combatTimer.remove(p.getUniqueId());
                            p.sendMessage(ColorUtils.format("&aYou have escaped combat."));
                            TitleUtils.sendActionBar(p, ColorUtils.format("&aCombat ended"));
                            TitleUtils.sendTitle(p, 10, 10, 10, ColorUtils.format("&a&lEscaped Combat"), ColorUtils.format("&7You are now &asafe&7 to log out."));
                        } else {
                            combatTimer.put(p.getUniqueId(), newV);
                            TitleUtils.sendActionBar(p, ColorUtils.format("&cCombat ends in " + newV + "s"));
                        }
                    }else{
                        combatTimer.remove(u);
                    }
                }
            }
        },0L, 20L);
    }

    public Integer getLeft(Player p){
        if(combatTimer.containsKey(p.getUniqueId())){
            return combatTimer.get(p.getUniqueId());
        }
        return null;
    }

    public Boolean inCombat(Player p){
        return combatTimer.containsKey(p.getUniqueId());
    }

    public Boolean attemptRestricted(Player p){
        Integer x = getLeft(p);
        if(x!=null){
            p.sendMessage(ColorUtils.format("&cYou can not do that whilst in combat, try again in "+x+"s"));
            return false;
        }
        return true;
    }

    public void logOn(Player p) throws IOException {
        if(deadPlayers.contains(p.getUniqueId())){
            (new BukkitRunnable() {
                public void run() {
                    try {
                        removeDeadPlayer(p.getUniqueId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    p.sendMessage(ColorUtils.format("&fYou have been killed because you logged off in combat."));
                    p.getInventory().clear();
                    p.setHealth(0D);
                }
            }).runTaskLater(VortechFactions.getPlugin(VortechFactions.class), 3L);

        }
    }

    public void addDeadPlayer(UUID u) throws IOException {
        deadPlayers.add(u);
        VortechFactions.dataFileConfig.set("combatlog", deadPlayers
                .stream()
                .map(UUID::toString)
                .collect(Collectors.toList()));
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    public void removeDeadPlayer(UUID u) throws IOException {
        deadPlayers.remove(u);
        VortechFactions.dataFileConfig.set("combatlog", deadPlayers
                .stream()
                .map(UUID::toString)
                .collect(Collectors.toList()));
        VortechFactions.dataFileConfig.save(VortechFactions.dataFile);
    }

    public void logOff(Player p) throws IOException {
        Bukkit.getLogger().info("Logged off: "+p.getUniqueId());
        if(combatTimer.containsKey(p.getUniqueId())) {
            Bukkit.getLogger().info("Logged off in combat: "+p.getUniqueId());
            Location l = p.getLocation();
            /**
            for (ItemStack s : p.getInventory().getContents()) {
                l.getWorld().dropItemNaturally(l, s);
            }
            p.getInventory().clear();
             */
            addDeadPlayer(p.getUniqueId());
            combatTimer.remove(p.getUniqueId());
        }
    }

    public void clear(Player p){
        if(combatTimer.containsKey(p.getUniqueId())){
            combatTimer.remove(p.getUniqueId());
        }
    }

    public void combat(Player p){
        if(!combatTimer.containsKey(p.getUniqueId())) {
            p.sendMessage(ColorUtils.format("&cYou have entered combat."));
            TitleUtils.sendTitle(p, 10, 10, 10, ColorUtils.format("&c&lCombat"), ColorUtils.format("&7If you log off in combat you will die."));
        }
        combatTimer.put(p.getUniqueId(), 20);
    }
}
