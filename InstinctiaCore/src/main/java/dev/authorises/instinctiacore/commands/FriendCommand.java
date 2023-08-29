package dev.authorises.instinctiacore.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.auth.TokenManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import dev.authorises.instinctiacore.utilities.FriendRequest;
import kong.unirest.Unirest;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.kyori.adventure.text.Component;

import java.awt.*;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class FriendCommand extends Command {

    public String prefix = "&aFriends |&r ";

    public FriendCommand(){
        super("friend", "", "f", "pal");
    }

    public void helpMessage(ProxiedPlayer p){
        p.sendMessage(ColorUtils.format("" +
                "&a-------------&f[ Friend Commands ]&a-------------" +
                "\n&a/friend help &f- Shows this help message" +
                "\n&a/friend list <page> &f- Show all of your friends" +
                "\n&a/friend deny <player> &f- Deny a friend request" +
                "\n&a/friend accept <player> &f- Accept a friend request" +
                "\n&a/friend remove <player> &f- Remove a friend" +
                "\n&a/friend add <player> &f- Send a friend request" +
                "\n&a-------------------------------------------"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if(args.length>=1){
            switch (args[0].toUpperCase()){
                case "HELP":{
                    helpMessage(p);
                    break;
                }
                case "ADD":{
                    if(args.length>=2){
                        FriendRequest request = new FriendRequest(p.getUniqueId(), args[1]);
                            CompletableFuture.supplyAsync(() -> {
                                return Unirest.post("http://localhost:8080/friend-request?token="+ TokenManager.token).body(InstinctiaCore.gson.toJson(request)).getBody();
                            }).thenAccept((x) -> {
                                if (!x.equals("OK")){
                                    p.sendMessage(ColorUtils.format(prefix+x));
                                }
                            });
                    }else{
                        p.sendMessage(ColorUtils.format(prefix+"&fCorrect format: &a/friend add <player>"));
                    }
                    break;
                }
                case "ACCEPT":{
                    if(args.length>=2){
                        FriendRequest request = new FriendRequest(p.getUniqueId(), args[1]);
                        CompletableFuture.supplyAsync(() -> {
                            return Unirest.post("http://localhost:8080/friend-request?token="+ TokenManager.token).body(InstinctiaCore.gson.toJson(request)).getBody();
                        }).thenAccept((x) -> {
                            if (!x.equals("OK")){
                                p.sendMessage(ColorUtils.format(prefix+x));
                            }
                        });
                    }else{
                        p.sendMessage(ColorUtils.format(prefix+"&fCorrect format: &a/friend accept <player>"));
                    }
                    break;
                }
                case "LIST":{
                    Integer page = 1;
                    if(args.length>=2){
                        try {
                            page = Integer.valueOf(args[1]);
                        }catch (NumberFormatException e){
                            p.sendMessage(ColorUtils.format(prefix+"Page number must be a number"));
                        }
                    }
                        Integer finalPage = page;
                    Integer finalPage1 = page;
                    CompletableFuture.supplyAsync(() -> {
                            return Unirest.get("http://localhost:8080/player/"+p.getUniqueId()+"/friend-list?token="+ TokenManager.token+"&page="+ finalPage).getBody().get().toString();
                        }).thenApply((x) -> {
                            return new JsonParser().parse(x).getAsJsonObject();
                        }).thenAccept((x) -> {
                            Integer maxPages = x.get("maxPages").getAsInt();
                            if(maxPages.equals(0)){
                                p.sendMessage(ColorUtils.format(prefix+"You do not have any friends. Add someone with &a/friend add <username>"));
                                return;
                            }else{
                                ComponentBuilder bottom2 = new ComponentBuilder(ColorUtils.format("\n&a---------------&f[ "));
                                Component bottom = Component.text(ColorUtils.format("\n&a---------------&f[ "));
                                if(finalPage1 >1){
                                    bottom2.append(new ComponentBuilder(ColorUtils.format("&a< "))
                                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&aPrevious Page")).create()))
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend list "+(finalPage1 -1)))
                                            .create(), ComponentBuilder.FormatRetention.NONE);
                                }
                                if(finalPage1 <maxPages){
                                    bottom2.append(new ComponentBuilder(ColorUtils.format("&a> "))
                                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&aNext Page")).create()))
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend list "+(finalPage1 +1)))
                                            .create(), ComponentBuilder.FormatRetention.NONE);
                                }
                                bottom2.append(new ComponentBuilder(ColorUtils.format("&f ]&a---------------"))
                                        .create(), ComponentBuilder.FormatRetention.NONE);
                                DateFormat df = new SimpleDateFormat("dd/MM/yy (HH:mm:ss)");

                                ComponentBuilder message2 = new ComponentBuilder(ColorUtils.format("&a----------&f[ Friends (&b"+ finalPage1 +"/"+maxPages+"&f) ]&a----------"));
                                Component message = Component.text(ColorUtils.format(
                                        "&a----------&f[ Friends (&b"+ finalPage1 +"/"+maxPages+"&f) ]&a----------"));
                                JsonArray pageO = x.get("page").getAsJsonArray();
                                for(JsonElement s : pageO){
                                    JsonObject s2 = s.getAsJsonObject();
                                    if(s2.has("onlinePlayer")){
                                        message=message.append(Component.text(ColorUtils.format("\n"+s2.get("displayName").getAsString()+"&f - &aOnline&7")));
                                        message2.append(new ComponentBuilder(ColorUtils.format("\n"+s2.get("displayName").getAsString()+"&f - &aOnline&7"))
                                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("" +
                                                        "" +s2.get("displayName").getAsString()+
                                                        "\n" +
                                                        "\n&aOnline" +
                                                        "\n&f- Proxy &d" +s2.get("onlinePlayer").getAsJsonObject().get("currentProxy").getAsString()+
                                                        "\n&f- Server &b" +s2.get("onlinePlayer").getAsJsonObject().get("currentServer").getAsString()+
                                                        "")).create()))
                                                .create());
                                    }else{
                                        message=message.append(Component.text(ColorUtils.format("\n"+s2.get("displayName").getAsString()+"&f - &cOffline&7")));
                                        message2.append(new ComponentBuilder(ColorUtils.format("\n"+s2.get("displayName").getAsString()+"&f - &cOffline&7"))
                                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("" +
                                                        "" +s2.get("displayName").getAsString()+
                                                        "\n" +
                                                        "\n&cOffline" +
                                                        "\n&f- Last seen at &b"+df.format(new Date(s2.get("lastOnline").getAsLong()))+
                                                        "")).create()))
                                                .create());
                                    }
                                }
                                message=message.append(bottom);
                                message2.append(bottom2.create(), ComponentBuilder.FormatRetention.NONE);
                                p.sendMessage(message2.create());
                                //p.sendMessage(BungeeComponentSerializer.get().serialize(message));
                            }
                        });
                    break;
                }
            }
        }else{
            helpMessage(p);
        }
    }

}
