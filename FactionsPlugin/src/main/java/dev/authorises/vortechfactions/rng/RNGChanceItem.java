package dev.authorises.vortechfactions.rng;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface RNGChanceItem {

    @NotNull
    public Rarity getRarity();
    @NotNull
    public RNGChanceType getType();
    public boolean getChatAnnouncement();
    @NotNull
    public Float getChance();
    public void execute(Player p);
}
