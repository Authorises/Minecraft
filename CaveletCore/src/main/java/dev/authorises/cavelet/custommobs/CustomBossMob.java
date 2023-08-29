package dev.authorises.cavelet.custommobs;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.ColorUtils;
import dev.authorises.cavelet.utils.EntityBuilder;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.function.BiConsumer;

public class CustomBossMob {
    private final EntityType entityType;
    private final String name;
    private final String displayName;
    private final Double maxHealth;
    private final Boolean canMove;
    private final BossBar.Color bossBarColor;
    private final Double playerDamage;
    private  Entity e;
    public Boolean alive;
    public final BiConsumer<Player, EntityDeathEvent> onDeath;
    private BossBar bossBar;

    public CustomBossMob(
            EntityType entityType,
            String name,
            String displayName,
            Double health,
            Boolean canMove,
            BossBar.Color bossBarColor,
            Double playerDamage,
            BiConsumer<Player, EntityDeathEvent> onDeath
            ) {
        this.entityType = entityType;
        this.name = name;
        this.displayName = displayName;
        this.maxHealth = health;
        this.canMove = canMove;
        this.bossBarColor = bossBarColor;
        this.playerDamage = playerDamage;
        this.onDeath = onDeath;

    }

    public String getHealthMessage(){
        return ColorUtils.format("<reset><red>❤ "+Math.round(((LivingEntity)this.e).getHealth())+"/"+Math.round(getMaxHealth()));
    }

    public void updateName(){
        if(((LivingEntity)this.e).getHealth()<1.0){
            for(Player p : Bukkit.getOnlinePlayers()){
                MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                if(mp.getCurrentBossBar()==this.bossBar){
                    mp.setCurrentBossBar(null);
                    p.hideBossBar(this.bossBar);
                }
            }
        }else {
            this.bossBar.progress(Double.valueOf((((LivingEntity) this.e).getHealth() / getMaxHealth())).floatValue());
            this.bossBar.name(Cavelet.miniMessage.deserialize(getDisplayName() + " " + getHealthMessage()));
            for (Player p : Bukkit.getOnlinePlayers()) {
                MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
                if (mp.getCurrentBossBar() != this.bossBar) {
                    mp.setCurrentBossBar(this.bossBar);
                } else {
                    mp.showBossBar(p, this.bossBar);
                }
            }

        }

    }

    public void kill(Player p, EntityDeathEvent event){
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        death();
        onDeath.accept(p, event);
    }

    public void death(){
        Bukkit.getOnlinePlayers().forEach((p) -> {
            MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
            if(mp.getCurrentBossBar()==this.bossBar){
                mp.setCurrentBossBar(null);
                p.hideBossBar(this.bossBar);
            }
        });
        this.alive = false;
        this.e.remove();
        Cavelet.customBossMobs.remove(this.e.getUniqueId());
    }

    public void spawn(Location l){
        this.e = new EntityBuilder(getEntityType())
                .setMove(getCanMove())
                .spawn(l);
        ((LivingEntity)this.e).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        if(!getCanMove()) {
            ((LivingEntity) this.e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
        }
        ((LivingEntity)this.e).setHealth(getMaxHealth());
        this.alive = true;
        this.e.customName(Cavelet.miniMessage.deserialize(getDisplayName()));
        Cavelet.customBossMobs.put(this.e.getUniqueId(), this);
        this.bossBar = BossBar.bossBar(Cavelet.miniMessage.deserialize(getDisplayName() + " " + getHealthMessage()), Double.valueOf((((LivingEntity) this.e).getHealth() / getMaxHealth())).floatValue(), getBossBarColor(), BossBar.Overlay.PROGRESS);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Double getMaxHealth() {
        return maxHealth;
    }

    public Entity getE() {
        return e;
    }

    public Boolean getCanMove() {
        return canMove;
    }

    public BossBar.Color getBossBarColor() {
        return bossBarColor;
    }

    public Double getPlayerDamage() {
        return playerDamage;
    }
}
