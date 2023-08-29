package dev.authorises.lightweightlobby.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.game.MapCreatorGui;
import dev.authorises.lightweightlobby.game.ParkourSession;
import dev.authorises.lightweightlobby.map.MapData;
import dev.authorises.lightweightlobby.map.MapScore;
import dev.authorises.lightweightlobby.map.MapType;
import dev.authorises.lightweightlobby.util.InventoryClose;
import dev.authorises.lightweightlobby.util.ListUtil;
import dev.authorises.lightweightlobby.util.TextUtil;
import kong.unirest.Unirest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.enginehub.linbus.stream.token.LinToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class PlayGui {

    private List<MapGuiEntry> maps;
    private Player player;

    public PlayGui(Player player){
        this.player = player;
        this.maps = new ArrayList<>();

        String res = Unirest.get("http://"+ Server.CENTRALC_ENDPOINT+":8080"+"/maps/"+player.getUuid()+"?token="+Server.CENTRALC_TOKEN)
                .asString()
                .getBody();
        JsonArray mapsJson = Server.gson.fromJson(res, JsonArray.class);
        mapsJson.forEach((json) -> {
            this.maps.add(new MapGuiEntry(json.getAsJsonObject()));
        });

        /**
        for(int x = 0; x<200;x++){

            UUID mapId = UUID.randomUUID();
            MapData data = new MapData(List.of(UUID.randomUUID()), mapId, "testmap"+x, List.of(), MapType.CHECKPOINT, new Random().nextLong(10, 5000000), "Filler map", "minecraft:oak_log", new Pos(0.0, 0.0, 0.0, 0F, 0F), true, true, true);
            MapScore score = new MapScore(UUID.fromString("1e3141e2-62ad-404f-9820-14b0c35459ac"), mapId, System.currentTimeMillis(), 1230L);
            this.maps.add(new MapGuiEntry(score, data));
        }
         */

    }

    private void setPage(int page){
        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize("<red>Parkour Maps"));

        inventory.addInventoryCondition((clicker, slot, clickType, result) -> {
            result.setCancel(true);
        });

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

        List<MapGuiEntry> pageResult = ListUtil.getPage(maps, page, 45);

        for(int i=0;i<pageResult.size();i++){
            MapGuiEntry entry = pageResult.get(i);
            MapData mapData = entry.getMapData();
            MapScore mapScore = entry.getScore();
            if(entry.getScore()!=null){
                List<Component> lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<!italic><green>"+TextUtil.formatAbbreviation(mapData.getLikes())+" likes"));
                lore.add(MiniMessage.miniMessage().deserialize(""));
                lore.addAll(TextUtil.splitText(mapData.getDescription(), "<gray><italic>", 50).stream().map(MiniMessage.miniMessage()::deserialize).toList());
                lore.add(MiniMessage.miniMessage().deserialize(""));
                lore.add(MiniMessage.miniMessage().deserialize("<!italic><white>Best Score: <aqua>"+ TextUtil.formatTime(mapScore.getTotalTime())+" <gray><italic>"+df.format(new Date(mapScore.getTimeCompleted()))));
                lore.add(MiniMessage.miniMessage().deserialize(""));
                lore.add(MiniMessage.miniMessage().deserialize("<!italic><white>Click to play"));
                inventory.setItemStack(i,
                        ItemStack.builder(mapData.getMaterial())
                                .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>"+mapData.getName()))
                                .lore(lore)
                                .build());
            }else{
                List<Component> lore = new ArrayList<>();
                lore.add(MiniMessage.miniMessage().deserialize("<!italic><green>"+TextUtil.formatAbbreviation(mapData.getLikes())+" likes"));
                lore.add(MiniMessage.miniMessage().deserialize(""));
                lore.addAll(TextUtil.splitText(mapData.getDescription(), "<gray><italic>", 50).stream().map(MiniMessage.miniMessage()::deserialize).toList());
                lore.add(MiniMessage.miniMessage().deserialize(""));
                lore.add(MiniMessage.miniMessage().deserialize("<!italic><white>Click to play"));
                inventory.setItemStack(i,
                        ItemStack.builder(mapData.getMaterial())
                                .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>"+mapData.getName()))
                                .lore(lore)
                                .build());
            }
            int finalI = i;
            inventory.addInventoryCondition((clicker, slot, clickType, result) -> {
                result.setCancel(true);
                if(slot!= finalI) return;

                if(Server.playersInSession.containsKey(player)){
                    Server.playersInSession.get(player).quit();
                }

                Server.socketManager.sendParkourRequest(player, mapData.getUuid(), ParkourSession.SessionAction.PLAY);
                player.closeInventory();
                player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<aqua>Loading map"), MiniMessage.miniMessage().deserialize("<gray>Your map is being loaded..."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(10), Duration.ZERO)));
            });
        }
        if(pageResult.size()<45){
            for(int i=0;i<45-pageResult.size();i++){
                inventory.setItemStack(44-i,
                        ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
                                .displayName(MiniMessage.miniMessage().deserialize("<!italic><gray>Wow, there really aren't enough maps to fill this page."))
                                .build());
            }
        }

        Integer pagesAmt = (int) Math.ceil((double) maps.size() /45);

        if(page<pagesAmt){
            inventory.setItemStack(53, ItemStack.builder(Material.ARROW)
                    .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>Next Page"))
                    .build());
            inventory.addInventoryCondition((clicker, slot, clickType, result) -> {
                result.setCancel(true);
                if(slot!= 53) return;
                setPage(page+1);
            });
        }

        if(page>1){
            inventory.setItemStack(45, ItemStack.builder(Material.ARROW)
                    .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>Previous Page"))
                    .build());
            inventory.addInventoryCondition((clicker, slot, clickType, result) -> {
                result.setCancel(true);
                if(slot!= 45) return;
                setPage(page-1);
            });
        }


        player.openInventory(inventory);
    }


    public void open(){
        setPage(1);
    }

}

class MapGuiEntry{

    private final MapScore score;
    private final MapData mapData;

    public MapGuiEntry(JsonObject object) {
        this.mapData = new MapData(object.get("map").getAsJsonObject());
        if(object.has("score")){
            this.score = new MapScore(object.get("score").getAsJsonObject());
        }else{
            this.score = null;
        }
    }

    public MapGuiEntry(MapScore score, MapData mapData){
        this.score = score;
        this.mapData = mapData;
    }

    public MapScore getScore() {
        return score;
    }

    public MapData getMapData() {
        return mapData;
    }
}