package dev.authorises.instinctiacore.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.auth.TokenManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import kong.unirest.Unirest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PunishmentManager {
    // Blocking
    public static Punishment getPunishmentByID(@NotNull UUID id) throws IOException {
        JsonObject p = new JsonParser().parse(Unirest.get("http://localhost:8080/punishment/"+id.toString()+"?token="+ TokenManager.token).getBody().get().toString()).getAsJsonObject(); //WebUtil.getObjectFromUrl("http://localhost:8080/punishment/"+id.toString()+"?token="+ TokenManager.token);
        try {
            Punishment punishment = new Punishment(
                    UUID.fromString(p.get("id").getAsString()),
                    PunishmentType.valueOf(p.get("type").getAsString()),
                    UUID.fromString(p.get("player").getAsString()),
                    UUID.fromString(p.get("punisher").getAsString()),
                    p.get("reason").getAsString(),
                    p.get("active").getAsBoolean(),
                    p.get("date").getAsLong()
            );
            return punishment;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static TextComponent punishmentView(Punishment p){
        TextComponent t = new TextComponent();
        Date d = new Date(p.getDate());
        DateFormat df = new SimpleDateFormat("dd/MM/yy (HH:mm:ss)");
        t.setText(p.getID().toString());
        t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&fPunishment ID: &b"+p.getID().toString()+"" +
                "\n&fPlayer UUID: &b"+p.getPlayer().toString()+"" +
                "\n&fPunisher UUID: &b"+p.getPunisher().toString()+"" +
                "\n&fStatus: "+(p.isActive()?"&aActive":"&cInactive")+"" +
                "\n&fDate: &b"+df.format(d)+"" +
                "\n&fType: &b"+p.getType().name+"" +
                "\n&fReason: &b"+p.getReason()+"" +
                "\n\n&b&nClick to manage this punishment")).create()));
        t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/punishment view "+p.getID().toString()));
        return t;
    }

    public static TextComponent punishmentViewCopy(Punishment p){
        TextComponent t = new TextComponent();
        Date d = new Date(p.getDate());
        DateFormat df = new SimpleDateFormat("dd/MM/yy (HH:mm:ss)");
        t.setText(p.getID().toString());
        t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&fPunishment ID: &b"+p.getID().toString()+"" +
                "\n&fPlayer UUID: &b"+p.getPlayer().toString()+"" +
                "\n&fPunisher UUID: &b"+p.getPunisher().toString()+"" +
                "\n&fStatus: "+(p.isActive()?"&aActive":"&cInactive")+"" +
                "\n&fDate: &b"+df.format(d)+"" +
                "\n&fType: &b"+p.getType().name+"" +
                "\n&fReason: &b"+p.getReason()+"" +
                "\n\n&b&nClick to copy this punishment's UUID")).create()));
        t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, p.getID().toString()));
        return t;
    }

    public static ArrayList<Punishment> getPunishments(UUID id) throws IOException {
        ArrayList<Punishment> p = new ArrayList<>();
        JsonArray a = new JsonParser().parse(Unirest.get("http://localhost:8080/player/"+id.toString()+"/punishments?token="+TokenManager.token).getBody().get().toString()).getAsJsonArray(); //WebUtil.getArrayFromUrl("http://localhost:8080/player/"+id.toString()+"/punishments?token="+TokenManager.token);
        a.forEach(d-> {
            JsonObject o = d.getAsJsonObject();
            p.add(new Punishment(
                    UUID.fromString(o.get("id").getAsString()),
                    PunishmentType.valueOf(o.get("type").getAsString()),
                    UUID.fromString(o.get("player").getAsString()),
                    UUID.fromString(o.get("punisher").getAsString()),
                    o.get("reason").getAsString(),
                    o.get("active").getAsBoolean(),
                    o.get("date").getAsLong()
            ));
        });
        return p;
    }

    public static ArrayList<Punishment> getPunishments(String username) throws IOException {
        ArrayList<Punishment> p = new ArrayList<>();
        JsonArray a = new JsonParser().parse(Unirest.get("http://localhost:8080/player/"+username+"/punishments?token="+TokenManager.token).getBody().get().toString()).getAsJsonArray(); //WebUtil.getArrayFromUrl("http://localhost:8080/player/"+username+"/punishments?token="+TokenManager.token+"&byname=true");
        a.forEach(d-> {
            JsonObject o = d.getAsJsonObject();
            p.add(new Punishment(
                    UUID.fromString(o.get("id").getAsString()),
                    PunishmentType.valueOf(o.get("type").getAsString()),
                    UUID.fromString(o.get("player").getAsString()),
                    UUID.fromString(o.get("punisher").getAsString()),
                    o.get("reason").getAsString(),
                    o.get("active").getAsBoolean(),
                    o.get("date").getAsLong()
            ));
        });
        return p;
    }

}
