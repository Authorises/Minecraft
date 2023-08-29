package dev.authorises.vortechfactions.chat;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.cmd.BrigadierProvider;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.zcore.util.TL;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.authorises.vortechfactions.VortechFactions;
import dev.authorises.vortechfactions.masks.maskUtils;
import dev.authorises.vortechfactions.masks.masks.MysteryMask;
import dev.authorises.vortechfactions.settings.BooleanSetting;
import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.figt.loafmenus.LoafMenu;
import us.figt.loafmenus.LoafMenuItem;
import us.figt.loafmenus.MenuRowSize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class chatGui extends LoafMenu {

    public chatGui(Player p){
        super(VortechFactions.loafMenuRegistrar, ColorUtils.format("&bSelect chat mode"), MenuRowSize.ONE, p);
    }

    public static List<String> chunk(String s, int limit) {
        List<String> parts = new ArrayList<String>();
        while(s.length() > limit) {
            int splitAt = limit-1;
            for(;splitAt>0 && !Character.isWhitespace(s.charAt(splitAt)); splitAt--);
            if(splitAt == 0)
                return parts; // can't be split
            parts.add(ColorUtils.format("&f"+s.substring(0, splitAt)));
            s = s.substring(splitAt+1);
        }
        parts.add(s);
        return parts;
    }

    @Override
    public LoafMenuItem[] getMenuItems() {
        LoafMenuItem[] array = newLoafMenuItemArray();
        Player player = getPlayer();
        FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);

        Boolean publicChatEnabled = ((BooleanSetting)VortechFactions.settingsManager.getSetting("public-chat-visible")).getState(player.getUniqueId());
        Boolean factionChatEnabled = ((BooleanSetting)VortechFactions.settingsManager.getSetting("faction-chat-visible")).getState(player.getUniqueId());
        Boolean modChatEnabled = ((BooleanSetting)VortechFactions.settingsManager.getSetting("mod-chat-visible")).getState(player.getUniqueId());
        Boolean truceChatEnabled = ((BooleanSetting)VortechFactions.settingsManager.getSetting("truce-chat-visible")).getState(player.getUniqueId());
        Boolean allyChatEnabled = ((BooleanSetting)VortechFactions.settingsManager.getSetting("ally-chat-visible")).getState(player.getUniqueId());

        ChatMode selectedMode = fplayer.getChatMode();

        LoafMenuItem publicChat = new LoafMenuItem(
                new ItemBuilder(Material.PAPER)
                        .setName(ColorUtils.format("&bPublic Chat"))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Description"))
                        .addLoreLine(ColorUtils.format("&f&bpublic chat&f is a chat all players have access to "))
                        .addLoreLine(ColorUtils.format("&fand is used for communication with other players."))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Visibility is set to "+(publicChatEnabled?"&aEnabled":"&cDisabled")))
                        .addLoreLine(ColorUtils.format((selectedMode.equals(ChatMode.PUBLIC)?"&aSelected":"&cNot Selected")))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Left click to select"))
                        .addLoreLine(ColorUtils.format("&7Right click to toggle visibility"))
                        .toItemStack(), (clicker, clickInformation) ->
        {
            if(clickInformation.getType().isLeftClick()){
                fplayer.setChatMode(ChatMode.PUBLIC);
                fplayer.msg(TL.COMMAND_CHAT_MODE_PUBLIC);
                new chatGui(player).open();
            }else if(clickInformation.getType().isRightClick()){
                try {
                    ((BooleanSetting) VortechFactions.settingsManager.getSetting("public-chat-visible")).toggleState(player.getUniqueId());
                    new chatGui(player).open();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });

        LoafMenuItem factionChat = new LoafMenuItem(
                new ItemBuilder(Material.PAPER)
                        .setName(ColorUtils.format("&aFaction Chat"))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Description"))
                        .addLoreLine(ColorUtils.format("&f&afaction chat&f is a chat all members in your "))
                        .addLoreLine(ColorUtils.format("&afaction&f have access to and use for communication."))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Visibility is set to "+(factionChatEnabled?"&aEnabled":"&cDisabled")))
                        .addLoreLine(ColorUtils.format((selectedMode.equals(ChatMode.FACTION)?"&aSelected":"&cNot Selected")))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Left click to select"))
                        .addLoreLine(ColorUtils.format("&7Right click to toggle visibility"))
                        .toItemStack(), (clicker, clickInformation) ->
        {
            if(clickInformation.getType().isLeftClick()){
                fplayer.setChatMode(ChatMode.FACTION);
                fplayer.msg(TL.COMMAND_CHAT_MODE_FACTION);
                new chatGui(player).open();
            }else if(clickInformation.getType().isRightClick()){
                try {
                    ((BooleanSetting) VortechFactions.settingsManager.getSetting("faction-chat-visible")).toggleState(player.getUniqueId());
                    new chatGui(player).open();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });

        LoafMenuItem modChat = new LoafMenuItem(
                new ItemBuilder(Material.PAPER)
                        .setName(ColorUtils.format("&eMod Chat"))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Description"))
                        .addLoreLine(ColorUtils.format("&f&emod chat&f is a chat for faction members who "))
                        .addLoreLine(ColorUtils.format("&fhave &belevated privileges&f and you must be"))
                        .addLoreLine(ColorUtils.format("&ffaction rank &eMod&f or higher to view/message there."))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Visibility is set to "+(modChatEnabled?"&aEnabled":"&cDisabled")))
                        .addLoreLine(ColorUtils.format((selectedMode.equals(ChatMode.MOD)?"&aSelected":"&cNot Selected")))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Left click to select"))
                        .addLoreLine(ColorUtils.format("&7Right click to toggle visibility"))
                        .toItemStack(), (clicker, clickInformation) ->
        {
            if(clickInformation.getType().isLeftClick()){
                fplayer.setChatMode(ChatMode.MOD);
                fplayer.msg(TL.COMMAND_CHAT_MODE_MOD);
                new chatGui(player).open();
            }else if(clickInformation.getType().isRightClick()){
                try {
                    ((BooleanSetting) VortechFactions.settingsManager.getSetting("mod-chat-visible")).toggleState(player.getUniqueId());
                    new chatGui(player).open();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });

        LoafMenuItem truceChat = new LoafMenuItem(
                new ItemBuilder(Material.PAPER)
                        .setName(ColorUtils.format("&dTruce Chat"))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Description"))
                        .addLoreLine(ColorUtils.format("&f&dtruce chat&f is a chat for your faction to"))
                        .addLoreLine(ColorUtils.format("&fprivately talk to all of their &dtruces&f."))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Visibility is set to "+(truceChatEnabled?"&aEnabled":"&cDisabled")))
                        .addLoreLine(ColorUtils.format((selectedMode.equals(ChatMode.TRUCE)?"&aSelected":"&cNot Selected")))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Left click to select"))
                        .addLoreLine(ColorUtils.format("&7Right click to toggle visibility"))
                        .toItemStack(), (clicker, clickInformation) ->
        {
            if(clickInformation.getType().isLeftClick()){
                fplayer.setChatMode(ChatMode.TRUCE);
                fplayer.msg(TL.COMMAND_CHAT_MODE_TRUCE);
                new chatGui(player).open();
            }else if(clickInformation.getType().isRightClick()){
                try {
                    ((BooleanSetting) VortechFactions.settingsManager.getSetting("truce-chat-visible")).toggleState(player.getUniqueId());
                    new chatGui(player).open();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });

        LoafMenuItem allyChat = new LoafMenuItem(
                new ItemBuilder(Material.PAPER)
                        .setName(ColorUtils.format("&5Ally Chat"))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Description"))
                        .addLoreLine(ColorUtils.format("&f&5ally chat&f is a chat for your faction to"))
                        .addLoreLine(ColorUtils.format("&fprivately talk to all of their &5allies&f."))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Visibility is set to "+(allyChatEnabled?"&aEnabled":"&cDisabled")))
                        .addLoreLine(ColorUtils.format((selectedMode.equals(ChatMode.ALLIANCE)?"&aSelected":"&cNot Selected")))
                        .addLoreLine(ColorUtils.format(""))
                        .addLoreLine(ColorUtils.format("&7Left click to select"))
                        .addLoreLine(ColorUtils.format("&7Right click to toggle visibility"))
                        .toItemStack(), (clicker, clickInformation) ->
        {
            if(clickInformation.getType().isLeftClick()){
                fplayer.setChatMode(ChatMode.ALLIANCE);
                fplayer.msg(TL.COMMAND_CHAT_MODE_ALLIANCE);
                new chatGui(player).open();
            }else if(clickInformation.getType().isRightClick()){
                try {
                    ((BooleanSetting) VortechFactions.settingsManager.getSetting("ally-chat-visible")).toggleState(player.getUniqueId());
                    new chatGui(player).open();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return true;
        });

        array[0] = publicChat;
        array[1] = factionChat;
        array[2] = modChat;
        array[3] = truceChat;
        array[4] = allyChat;
        return array;
    }

    public TL getUsageTranslation() {
        return TL.COMMAND_CHAT_DESCRIPTION;
    }

    protected class ChatBrigadier implements BrigadierProvider {
        @Override
        public ArgumentBuilder<Object, ?> get(ArgumentBuilder<Object, ?> parent) {
            return parent.then(LiteralArgumentBuilder.literal("public"))
                    .then(LiteralArgumentBuilder.literal("mod"))
                    .then(LiteralArgumentBuilder.literal("alliance"))
                    .then(LiteralArgumentBuilder.literal("faction"))
                    .then(LiteralArgumentBuilder.literal("truce"));
        }
    }

}
