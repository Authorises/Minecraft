package dev.authorises.instinctiacore.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import dev.authorises.instinctiacore.InstinctiaCore;
import dev.authorises.instinctiacore.auth.TokenManager;
import dev.authorises.instinctiacore.objects.*;
import dev.authorises.instinctiacore.sockets.SocketManager;
import dev.authorises.instinctiacore.utilities.ColorUtils;
import dev.authorises.instinctiacore.utilities.UUIDFromString;
import kong.unirest.Unirest;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.http.HttpResponse;
import org.bson.Document;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class PunishmentCommand extends Command {

    public PunishmentCommand(){
        super("punishment", "instinctia.staff", "pu");
    }

    public void help(ProxiedPlayer p){
        p.sendMessage(ColorUtils.format("&fPunishment Commands:" +
                "\n&b- &f/punishment help" +
                "\n&b- &f/punishment view &b[Punishment ID]" +
                "\n&b- &f/punishment revoke &b[Punishment ID]" +
                "\n&b- &f/punishment regive &b[Punishment ID]" +
                "\n&b- &f/punishment list &b[UUID/Username]" +
                "\n&b- &f/punishment punish &b[UUID/Username] [Ban/Mute/Warn] [Reason]" +
                "\n&cNOTE: &7You can only use an offline player's uuid if they have joined before."));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(args.length>=1){
                String action = args[0].toLowerCase(Locale.ROOT);
                switch (action) {
                    case "help":
                        help(p);
                        break;
                    case "view":
                        if (args.length == 2) {
                            CompletableFuture.supplyAsync(
                                    () -> {
                                        try {
                                            Punishment rpun = PunishmentManager.getPunishmentByID(UUID.fromString(args[1]));
                                            return rpun;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            throw new RuntimeException(e);
                                        }
                                    }
                            ).thenApply((pu) -> {
                                System.out.println(pu);
                                if (pu == null) {
                                    p.sendMessage(ColorUtils.format("&cError, punishment was not found with that ID"));
                                } else {
                                    TextComponent revokeButton = new TextComponent(ColorUtils.format("&d&lREVOKE"));
                                    revokeButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/punishment revoke " + pu.getID().toString()));
                                    revokeButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&b&l(!)&F Click to revoke the punishment, rendering it inactive.")).create()));

                                    TextComponent regiveButton = new TextComponent(ColorUtils.format("&a&lREGIVE"));
                                    regiveButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/punishment regive " + pu.getID().toString()));
                                    regiveButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&b&l(!)&F Click to regive the punishment, rendering it active.")).create()));

                                    Date d = new Date(pu.getDate());
                                    DateFormat df = new SimpleDateFormat("dd/MM/yy (HH:mm:ss)");
                                    BaseComponent t = PunishmentManager.punishmentViewCopy(pu);
                                    t.setColor(ChatColor.AQUA);
                                    BaseComponent c = new TextComponent(ColorUtils.format("" +
                                            "\n&fPunishment: &b"));
                                    c.addExtra(t);
                                    BaseComponent c2 = new TextComponent(ColorUtils.format("\n&fPlayer: &b" + pu.getPlayer().toString()));
                                    c2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtils.format("&bClick to view this player")).create()));
                                    c2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/p -uuid " + pu.getPlayer().toString()));
                                    c.addExtra(c2);
                                    c.addExtra(new TextComponent(ColorUtils.format(
                                            "\n&fPunisher UUID: &b" + pu.getPunisher().toString() + "" +
                                                    "\n&fStatus: " + (pu.isActive() ? "&aActive" : "&cInactive") + "" +
                                                    "\n&fDate: &b" + df.format(d) + "" +
                                                    "\n&fType: &b" + pu.getType().name + "" +
                                                    "\n&fReason: &b" + pu.getReason() +
                                                    "\n&fActions: ")));

                                    c.addExtra(pu.isActive() ? revokeButton : regiveButton);

                                    p.sendMessage(c);
                                }
                                return 0;
                            });

                        } else {
                            p.sendMessage(ColorUtils.format("&cCommand format error, check /punishment help"));
                        }
                        break;
                    case "revoke":
                        if (args.length >= 2) {
                            CompletableFuture.supplyAsync(
                                    () -> {
                                        try {
                                            return PunishmentManager.getPunishmentByID(UUID.fromString(args[1]));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            ).thenApply((pu) -> {
                                if (pu == null) {
                                    p.sendMessage(ColorUtils.format("&cError, punishment was not found with that ID"));
                                } else {
                                    long f = System.currentTimeMillis();
                                    BaseComponent b = new TextComponent(ColorUtils.format("Revoking "));
                                    b.addExtra(PunishmentManager.punishmentView(pu));
                                    p.sendMessage(b);

                                    pu.undo();
                                    CompletableFuture.runAsync(() -> {
                                        Unirest.post("http://localhost:8080/punishment/" + pu.getID()+"?token="+TokenManager.token).body(InstinctiaCore.gson.toJson(pu));
                                    }).thenRun(() -> {
                                        p.sendMessage("Done!");
                                    });
                                }
                                return 0;
                            });
                        } else {
                            p.sendMessage(ColorUtils.format("&cCommand format error, check /punishment help"));
                        }
                        break;
                    case "regive":
                        if (args.length >= 2) {
                            CompletableFuture.supplyAsync(
                                    () -> {
                                        try {
                                            return PunishmentManager.getPunishmentByID(UUID.fromString(args[1]));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            ).thenApply((pu) -> {
                                if (pu == null) {
                                    p.sendMessage(ColorUtils.format("&cError, punishment was not found with that ID"));
                                } else {
                                    long f = System.currentTimeMillis();
                                    BaseComponent b = new TextComponent(ColorUtils.format("Regiving "));
                                    b.addExtra(PunishmentManager.punishmentView(pu));
                                    p.sendMessage(b);

                                    pu.redo();
                                    CompletableFuture.runAsync(() -> {
                                        Unirest.post("http://localhost:8080/punishment/" + pu.getID()+"?token="+TokenManager.token).body(InstinctiaCore.gson.toJson(pu));
                                    }).thenRun(() -> {
                                        p.sendMessage("Done!");
                                    });
                                }
                                return 0;
                            });

                        } else {
                            p.sendMessage(ColorUtils.format("&cCommand format error, check /punishment help"));
                        }
                        break;
                    case "list":
                        if (args.length >= 2) {
                            UUID id = null;
                            try {
                                id = UUIDFromString.fromStringOrName(args[1]);
                            } catch (Exception e) {
                                p.sendMessage(ColorUtils.format("&cError! A player with that name or UUID was not found. &f(2)"));
                            }

                            if (id != null) {
                                UUID finalId = id;
                                CompletableFuture.supplyAsync(
                                        () -> {
                                            try {
                                                return PunishmentManager.getPunishments(finalId);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                ).thenAccept((punishments) -> {
                                    p.sendMessage(ColorUtils.format("Listing all punishments oldest to newest:"));
                                    for (Punishment pu : punishments) {
                                        p.sendMessage(PunishmentManager.punishmentView(pu));
                                    }
                                });
                            } else {
                                p.sendMessage(ColorUtils.format("&cError! A player with that name or UUID was not found. &f(3)"));
                            }
                        } else {
                            p.sendMessage(ColorUtils.format("&cCommand format error, check /punishment help"));
                        }
                        break;
                    case "punish":
                        if (args.length >= 4) {
                            UUID id = null;
                            try {
                                id = UUIDFromString.fromStringOrName(args[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                                PlayerData pl = PlayerDataManager.fromUsername(args[1]);
                                if(pl==null) {
                                    p.sendMessage(ColorUtils.format("&cError! A player with that name or UUID was not found. &f(2)"));
                                    return;
                                }else id=pl.uuid;
                            }
                            if (id != null) {
                                PlayerData pl = PlayerDataManager.getPlayerById(id);
                                if (pl != null) {
                                    String reason = "";
                                    int d = 3;
                                    while (d < args.length) {
                                        reason += args[d] + " ";
                                        d += 1;
                                    }
                                    switch (args[2].toLowerCase()) {
                                        case "warn": {
                                            UUID punishmentID = UUID.randomUUID();
                                            Punishment pu = new Punishment(
                                                    punishmentID,
                                                    PunishmentType.warn,
                                                    id,
                                                    p.getUniqueId(),
                                                    reason,
                                                    true,
                                                    System.currentTimeMillis()
                                            );
                                            long t = System.currentTimeMillis();
                                            BaseComponent b = new TextComponent(ColorUtils.format("&fUploading punishment: "));
                                            TextComponent c = PunishmentManager.punishmentView(pu);
                                            c.setColor(ChatColor.AQUA);
                                            b.addExtra(c);
                                            p.sendMessage(b);

                                            UUID finalId = id;
                                            CompletableFuture.runAsync(() -> {
                                                Unirest.post("http://localhost:8080/punishment/" + pu.getID()+"?token="+TokenManager.token).body(InstinctiaCore.gson.toJson(pu));
                                            }).thenRun(() -> {
                                                p.sendMessage("Done!");
                                            });
                                            break;
                                        }
                                        case "mute": {
                                            UUID punishmentID = UUID.randomUUID();
                                            Punishment pu = new Punishment(
                                                    punishmentID,
                                                    PunishmentType.mute,
                                                    id,
                                                    p.getUniqueId(),
                                                    reason,
                                                    true,
                                                    System.currentTimeMillis()
                                            );
                                            long t = System.currentTimeMillis();
                                            BaseComponent b = new TextComponent(ColorUtils.format("&fUploading punishment: "));
                                            TextComponent c = PunishmentManager.punishmentView(pu);
                                            c.setColor(ChatColor.AQUA);
                                            b.addExtra(c);
                                            p.sendMessage(b);

                                            UUID finalId = id;
                                            CompletableFuture.runAsync(() -> {
                                                Unirest.post("http://localhost:8080/punishment/" + pu.getID()+"?token="+TokenManager.token).body(InstinctiaCore.gson.toJson(pu));
                                            }).thenRun(() -> {
                                                p.sendMessage("Done!");
                                            });
                                            break;
                                        }
                                        case "ban": {
                                            UUID punishmentID = UUID.randomUUID();
                                            Punishment pu = new Punishment(
                                                    punishmentID,
                                                    PunishmentType.ban,
                                                    id,
                                                    p.getUniqueId(),
                                                    reason,
                                                    true,
                                                    System.currentTimeMillis()
                                            );
                                            long t = System.currentTimeMillis();
                                            BaseComponent b = new TextComponent(ColorUtils.format("&fUploading punishment: "));
                                            TextComponent c = PunishmentManager.punishmentView(pu);
                                            c.setColor(ChatColor.AQUA);
                                            b.addExtra(c);
                                            p.sendMessage(b);

                                            UUID finalId = id;
                                            CompletableFuture.runAsync(() -> {
                                                Unirest.post("http://localhost:8080/punishment/" + pu.getID()+"?token="+TokenManager.token).body(InstinctiaCore.gson.toJson(pu));
                                            }).thenRun(() -> {
                                                p.sendMessage("Done!");
                                            });
                                            break;
                                        }
                                    }
                                } else {
                                    p.sendMessage(ColorUtils.format("&cError! A player with that name or UUID was not found. &f(2)"));
                                }
                            } else {
                                p.sendMessage(ColorUtils.format("&cError! A player with that name or UUID was not found. &f(3)"));
                            }
                        } else {
                            p.sendMessage(ColorUtils.format("&cCommand format error, check /punishment help"));
                        }
                        break;
                    }
            }else{
                help(p);
            }
        }else{
            sender.sendMessage(ColorUtils.format("&cOnly players can run this!&r"));
        }
    }
}
