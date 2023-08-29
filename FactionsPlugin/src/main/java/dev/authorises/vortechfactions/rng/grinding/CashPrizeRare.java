package dev.authorises.vortechfactions.rng.grinding;

import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.rng.RNGChanceItem;
import dev.authorises.vortechfactions.rng.RNGChanceType;
import dev.authorises.vortechfactions.rng.Rarity;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CashPrizeRare implements RNGChanceItem {
    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.RARE;
    }

    @Override
    public @NotNull RNGChanceType getType() {
        return RNGChanceType.GRINDING;
    }

    @Override
    public boolean getChatAnnouncement() {
        return false;
    }

    @Override
    public @NotNull Float getChance() {
        return 0.001F;
    }

    @Override
    public void execute(Player p) {
        p.sendMessage(ColorUtils.format(getRarity().getName()+"&f You have received "+getRarity().getColor()+"$500,000"));
        VortechFactions.econ.depositPlayer(p, 500000);
    }
}
