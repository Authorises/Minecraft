package dev.authorises.cavelet.objectives;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.utils.AdvancementMessage;
import dev.authorises.cavelet.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.List;

public class Objective {
    private final String id;
    private final String displayName;
    private final Material material;
    private final String message;
    private final AdvancementMessage advancementMessage;
    private final Boolean giveReward;

    public Objective(String id, String displayName, Material material, String message, Boolean giveReward) {
        this.id = id;
        this.displayName = displayName;
        this.message = message;
        this.giveReward = giveReward;
        if(material==null) this.material=Material.BEDROCK;
        else{
            this.material = material;
        }

        this.advancementMessage = new AdvancementMessage(
                new NamespacedKey(Cavelet.getPlugin(Cavelet.class), "cavelet-advancementobjective-"+id),
                this.displayName.replace('&', '§'),
                this.material.toString().toLowerCase(),
                Cavelet.getProvidingPlugin(Cavelet.class));
    }

    public void check(Player p){
        MProfile mp = Cavelet.cachedMPlayers.get(p.getUniqueId());
        List<Objective> completedObjectives = mp.getCompletedObjectives();
        if(!(completedObjectives.contains(this))){
            completedObjectives.add(this);
            //p.sendMessage(ColorUtils.format("&fYou have completed objective: &b"+getDisplayName()));
            if(getGiveReward()) {
                //BoxCustomItem b = (BoxCustomItem) Cavelet.customItemsManager.getItemById("OBJECTIVE_BOX").getItem();
                //p.sendMessage(ColorUtils.format("&fYou have received &b1x " + b.getName()));
                //p.getInventory().addItem(b.getItem());
            }
            //p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1F, 2F);
            mp.setCompletedObjectives(completedObjectives);
            //this.advancementMessage.showTo(p);
        }
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getGiveReward() {
        return giveReward;
    }
}
