package dev.authorises.cavelet.customitems.simpleitems;

import de.tr7zw.nbtapi.NBTItem;
import dev.authorises.cavelet.Cavelet;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class HelpBook {

    public static void getBook(Player p){
        ItemStack s = new ItemStack(Material.WRITTEN_BOOK);
        NBTItem i = new NBTItem(s);
        i.setString("book_id", "HELP_BOOK");
        s=i.getItem();
        BookMeta meta = (BookMeta) s.getItemMeta();
        meta.setTitle("Welcome to Cavelet");
        meta.addPages(Cavelet.miniMessage.deserialize(
                "Welcome to <blue>Cavelet<reset>, "+p.getName()+"!<newline>" +
                        "<newline>You can <light_purple>get started<reset> by following the objectives on your scoreboard."+
                        "<newline><newline>If you ever need some help, you can ask any staff online or you can ask on the discord: <click:open_url:'https://discord.gg/QVVzdAx9bq'><#7289da>Discord Server (Click to join)</click><reset>"));
        meta.setAuthor("Cavelet");
        s.setItemMeta(meta);
        p.openBook(s);
    }

}
