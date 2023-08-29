package dev.authorises.cavelet.listeners;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.utils.ColorUtils;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AbstractChatEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ChatFormatListener implements Listener {

    @EventHandler
    public void chatListener(AsyncChatEvent e){
        e.setCancelled(true);

        Cavelet.chatManager.dispatchMessage(e.getPlayer(), Cavelet.plainTextComponentSerializer.serialize(e.message()));

        /** OLD chat system
        Player p = e.getPlayer();
        if(p.hasPermission("staff")){
            String s = "<#55FFFF>"+ Cavelet.miniMessage.serialize(e.getPlayer().displayName())+": ";
            Component c = Cavelet.miniMessage.deserialize(s)
                    .hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<#AAAAAA>Click to <#E2ADF2>view stats<#AAAAAA> for ").append(p.displayName())))
                    .clickEvent(ClickEvent.runCommand("/view "+e.getPlayer().getUniqueId())).append(e.message().color(TextColor.fromHexString("#ffffff")));
            Bukkit.getOnlinePlayers().forEach(lp -> lp.sendMessage(c));
        }else{
            String s = "<#ffffff>"+ Cavelet.miniMessage.serialize(e.getPlayer().displayName())+": ";
            Component c = Cavelet.miniMessage.deserialize(s)
                    .hoverEvent(HoverEvent.showText(Cavelet.miniMessage.deserialize("<#AAAAAA>Click to <#05B2DC>view stats<#AAAAAA> for ").append(p.displayName())))
                    .clickEvent(ClickEvent.runCommand("/view "+e.getPlayer().getUniqueId())).append(e.message().color(TextColor.fromHexString("#ffffff")));
            Bukkit.getOnlinePlayers().forEach(lp -> lp.sendMessage(c));
        }
         */

    }


}
