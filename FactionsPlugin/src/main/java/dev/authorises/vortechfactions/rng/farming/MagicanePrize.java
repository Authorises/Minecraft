package dev.authorises.vortechfactions.rng.farming;

import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.masks.maskUtils;
import dev.authorises.vortechfactions.masks.masks.MysteryMask;
import dev.authorises.vortechfactions.rng.RNGChanceItem;
import dev.authorises.vortechfactions.rng.RNGChanceType;
import dev.authorises.vortechfactions.rng.Rarity;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MagicanePrize implements RNGChanceItem {
    @Override
    public @NotNull Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public @NotNull RNGChanceType getType() {
        return RNGChanceType.FARMING;
    }

    @Override
    public boolean getChatAnnouncement() {
        return false;
    }

    @Override
    public @NotNull Float getChance() {
        return 0.0004F;
    }

    @Override
    public void execute(Player p) {
        p.sendMessage(ColorUtils.format(getRarity().getName()+"&f You have received 1x"+getRarity().getColor()+"Magicane"));
        p.getInventory().addItem(Items.items.get("magicane"));
    }
}
