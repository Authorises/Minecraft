package dev.authorises.vortechcore.gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import dev.authorises.vortechcore.VortechCore;
import dev.authorises.vortechcore.utilities.ColorUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PlayGui {

    public PlayGui(Player p){
        ChestGui gui = new ChestGui(6, ColorUtils.format("&cAmir"));
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        gui.setOnGlobalClick(c->c.setCancelled(true));
        OutlinePane games = new OutlinePane(1,1,7,4);
        ItemStack lobbyItem = new ItemStack(Material.SNOW);
        ItemMeta lobbyItemMeta = lobbyItem.getItemMeta();
        try {
            lobbyItemMeta.setDisplayName(ColorUtils.format("&bLobby &f(" + VortechCore.onlineCount.get("lm") + " Online )"));
            lobbyItem.setAmount(VortechCore.onlineCount.get("lm"));
        }catch (Exception e){
            lobbyItemMeta.setDisplayName(ColorUtils.format("&cLobby &f(Offline)"));
            lobbyItem.setAmount(1);
        }
        ArrayList<String> lobbyItemLore = new ArrayList<>();
        lobbyItemLore.add(ColorUtils.format("&fThe lobby is a waiting area where you can select games to play."));
        lobbyItemLore.add(ColorUtils.format("&fIf a server crashes, you will be sent to a lobby automatically."));
        lobbyItemLore.add(ColorUtils.format("&fWhile waiting for servers to open, the lobby is the only place you can go!"));
        lobbyItemLore.add(ColorUtils.format(""));
        lobbyItemLore.add(ColorUtils.format("&b&lCLICK TO JOIN"));
        lobbyItemMeta.setLore(lobbyItemLore);
        lobbyItem.setItemMeta(lobbyItemMeta);

        ItemStack factionsItem = new ItemStack(Material.TNT);
        ItemMeta factionsItemMeta = factionsItem.getItemMeta();
        try {
            factionsItemMeta.setDisplayName(ColorUtils.format("&cFactions &f(" + VortechCore.onlineCount.get("fac") + " Online )"));
            factionsItem.setAmount(VortechCore.onlineCount.get("fac"));
        }catch (Exception e){
            factionsItemMeta.setDisplayName(ColorUtils.format("&cFactions &f(Offline)"));
            factionsItem.setAmount(1);
        }
        ArrayList<String> factionsItemLore = new ArrayList<>();
        factionsItemLore.add(ColorUtils.format("&fIn factions you battle against your enemies to try"));
        factionsItemLore.add(ColorUtils.format("&fto get as much value as possible, raiding eachother"));
        factionsItemLore.add(ColorUtils.format("&fwith cannons. This server has competitive rewards."));
        factionsItemLore.add(ColorUtils.format(""));
        factionsItemLore.add(ColorUtils.format("&b&lCLICK TO JOIN"));
        factionsItemMeta.setLore(factionsItemLore);
        factionsItem.setItemMeta(factionsItemMeta);


        games.addItem(new GuiItem(lobbyItem, click -> {
            p.performCommand("play lm");
        }));

        games.addItem(new GuiItem(factionsItem, click -> {
            p.performCommand("play fac");
        }));

        gui.addPane(background);
        gui.addPane(games);

        gui.show(p);
    }

}
