package dev.authorises.cavelet.eventsystem.events.cthulu;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customitems.CItemBlueprint;
import dev.authorises.cavelet.customitems.Rarity;
import dev.authorises.cavelet.custommobs.CustomBossMob;
import dev.authorises.cavelet.eventsystem.Event;
import dev.authorises.cavelet.eventsystem.EventLocation;
import dev.authorises.cavelet.utils.ChatUtil;
import dev.authorises.cavelet.utils.ColorUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Random;

public class CthuluEvent extends Event {

    private EventLocation location;
    private CustomBossMob bossMob;

    private EventLocation pickLocation(){
        List<EventLocation> potentialLocations = List.of(
                EventLocation.NORTHEAST,
                EventLocation.NORTHWEST,
                EventLocation.SOUTHEAST,
                EventLocation.SOUTHWEST
        );
        return potentialLocations.get(new Random().nextInt(potentialLocations.size()));
    }

    public CthuluEvent() {
        super("Cthulu", Rarity.RARE, "CTHULU");
    }

    @Override
    public void start(){

        this.location = pickLocation();

        Component startMessage = Cavelet.miniMessage.deserialize("<#19b6e6>Event! <#e01d4b>Cthulu<#9eb5db> has spawned in at <#19b6e6>"+location.location+"<#9eb5db>. Kill them to gain divine rewards");

        final Title title = Title.title(Cavelet.miniMessage.deserialize("<bold><#e01d4b>Cthulu"), Cavelet.miniMessage.deserialize("<#e01d4b>Cthulu <#9eb5db>has spawned in at <#19b6e6>"+location.location+"<#9eb5db>."));

        Bukkit.getOnlinePlayers().forEach((p) -> {
            p.sendMessage(startMessage);
            p.showTitle(title);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1F, 1F);
        });

        this.bossMob = new CustomBossMob(
                EntityType.SKELETON_HORSE,
                "CTHULU",
                "<#e01d4b>Cthulu",
                1000D,
                false,
                BossBar.Color.RED,
                2.0,
                (player, event) -> {

                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 4F, 4F);
                    CItemBlueprint<?> stash = Cavelet.customItemsManager.getItemById("SKELETAL_STASH");
                    player.sendMessage(Cavelet.miniMessage.deserialize("<#9eb5db>You have killed <#e01d4b>Cthulu<#9eb5db>, and in reward have gained 1x ")
                            .append(ChatUtil.getItemComponent(stash))
                            .append(Cavelet.miniMessage.deserialize("<#9eb5db>, claim it with /claim.")));
                    Cavelet.cachedMPlayers.get(player.getUniqueId()).addClaim(player, Cavelet.customItemsManager.getItemById("SKELETAL_STASH"));
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                    Component d = Cavelet.miniMessage.deserialize("<#19b6e6>Event! <#e01d4b>Cthulu<#9eb5db> has been slaughtered by <#19b6e6>"+player.getName()+"<#9eb5db>. They received 1x ").append(ChatUtil.getItemComponent(stash));

                    Bukkit.getOnlinePlayers().forEach((p) -> {
                        p.sendMessage(d);
                    });
                    this.end(false);
                }
        );

        this.bossMob.spawn(new Location(Bukkit.getWorlds().get(0), this.location.x, this.location.y, this.location.z));

    };

    @Override
    public void end(boolean forced){
        this.bossMob.death();
        Cavelet.eventManager.clearCurrentEvent();
    };

    @Override
    public void showEventInfo(Player player){
        Component c = Cavelet.miniMessage.deserialize("<#9eb5db>Current event: <#e01d4b>Cthulu\n<#9eb5db>Cthulu's location: <#19b6e6>"+location.location+"<#9eb5db>.\n Kill Cthulu to gain divine rewards");
        player.sendMessage(c);
    }
}
