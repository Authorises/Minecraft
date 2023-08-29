package dev.authorises.lightweightlobby.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.map.MapData;
import dev.authorises.lightweightlobby.map.MapType;
import dev.authorises.lightweightlobby.sockets.SocketManager;
import dev.authorises.lightweightlobby.util.PlayerInput;
import kong.unirest.Unirest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class MapCreatorGui {

    private String name;
    private MapType type;
    private final Player player;

    public List<Component> tasksToDo(){
        List<Component> todo = new ArrayList<>();
        if(name==null){
            todo.add(MiniMessage.miniMessage().deserialize("<!italic><gray> - Set a <aqua>name"));
        }

        if(type==null){
            todo.add(MiniMessage.miniMessage().deserialize("<!italic><gray> - Set map type"));
        }

        if(todo.size()>0){
            todo.add(0, MiniMessage.miniMessage().deserialize(""));
            todo.add(0, MiniMessage.miniMessage().deserialize("<!italic><gray>Actions needed:"));
        }

        return todo;
    }

    public void mainGui(){
        Inventory mainInventory = new Inventory(InventoryType.CHEST_1_ROW, MiniMessage.miniMessage().deserialize("<red>Create a map"));

        mainInventory.addItemStack(ItemStack.builder(Material.NAME_TAG)
                .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>Change map name <gray>(Click)"))
                .lore(List.of(
                        MiniMessage.miniMessage().deserialize("<!italic><gray>Current name: <aqua>"+name)
                ))
                .build());

        mainInventory.addInventoryCondition((clicker, slot, clickType, result) -> {
            if(slot!=0) return;
            PlayerInput.doSignInput(player, "Enter new name", (inputtedName) -> {
                if(Pattern.compile("[^a-zA-Z0-9]").matcher(inputtedName).find() || inputtedName.length()>10 || inputtedName.length()<3){
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>Map names must only contain letters and numbers and be 10 or less characters long."));
                }else{
                    name = inputtedName;
                }
                mainGui();
            });
            result.setCancel(true);
        });


        List<Component> toDo = tasksToDo();
        // Tasks left to do
        if(toDo.size()>0){
           mainInventory.setItemStack(8, ItemStack.builder(Material.BARRIER)
                           .displayName(MiniMessage.miniMessage().deserialize("<!italic><red>You cannot create yet"))
                           .lore(toDo)
                   .build());
           mainInventory.addInventoryCondition((clicker, slot, clickType, result) -> {
                if(slot!=8) return;
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>You have unfinished options to select."));
                result.setCancel(true);
           });
        }
        // Ready to create
        else{
            mainInventory.setItemStack(8, ItemStack.builder(Material.LIME_CONCRETE)
                    .displayName(MiniMessage.miniMessage().deserialize("<!italic><green>Create map <gray>(Click)"))
                    .lore(toDo)
                    .build());
            mainInventory.addInventoryCondition((clicker, slot, clickType, result) -> {
                if(slot!=8) return;
                result.setCancel(true);
                submit().thenAccept((submitResult) -> {
                    JsonObject newServer = JsonParser.parseString(submitResult).getAsJsonObject();
                    if(newServer.has("error")){
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>(!) <gray>"+newServer.get("error").getAsString()));
                    }else{
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>(!) <gray>Map was created successfully. The map editor is now being loaded."));
                        Server.socketManager.sendParkourRequest(player, UUID.fromString(newServer.get("uuid").getAsString()), ParkourSession.SessionAction.EDIT);
                        player.closeInventory();
                    }
                });
            });
        }

        player.openInventory(mainInventory);

    }

    public MapCreatorGui(Player player){
        this.player = player;
        name = UUID.randomUUID().toString().substring(0, 4).toLowerCase();
        type = MapType.CHECKPOINT;
        mainGui();
    }

    private CompletableFuture<String> submit(){
        return CompletableFuture.supplyAsync(() -> {
            JsonObject newMap = new JsonObject();
            newMap.addProperty("creator", player.getUuid().toString());
            newMap.addProperty("name", name);
            newMap.addProperty("type", type.toString());

            return Unirest.post("http://"+ Server.CENTRALC_ENDPOINT+":8080"+"/newmap"+"?token="+Server.CENTRALC_TOKEN)
                    .header("Content-Type", "application/json")
                    .body(newMap.toString())
                    .asString()
                    .getBody();
        });
    }

}
