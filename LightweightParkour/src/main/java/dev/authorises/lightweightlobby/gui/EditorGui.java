package dev.authorises.lightweightlobby.gui;

import com.google.gson.JsonArray;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.game.MapCreatorGui;
import dev.authorises.lightweightlobby.game.ParkourSession;
import dev.authorises.lightweightlobby.map.MapData;
import dev.authorises.lightweightlobby.util.InventoryClose;
import kong.unirest.Unirest;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.time.Duration;
import java.util.List;

public class EditorGui {

    private void init(Player player){
        String res = Unirest.get("http://"+ Server.CENTRALC_ENDPOINT+":8080"+"/player/"+player.getUuid()+"/maps?token="+Server.CENTRALC_TOKEN)
                .asString()
                .getBody();
        JsonArray maps = Server.gson.fromJson(res, JsonArray.class);



        Inventory inventory = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize("<gray>Your Maps"));
        for(int i=0;i<maps.size();i++){
            MapData map = new MapData(maps.get(i).getAsJsonObject());
            inventory.setItemStack(i,
                    ItemStack.builder(map.getMaterial())
                            .displayName(MiniMessage.miniMessage().deserialize("<!italic><aqua>"+map.getName()))
                            .lore(List.of(
                                    MiniMessage.miniMessage().deserialize(""),
                                    MiniMessage.miniMessage().deserialize("<!italic><white>Left click to edit"),
                                    MiniMessage.miniMessage().deserialize("<!italic><white>Right click to play")
                            ))
                            .build());
            int finalI = i;
            inventory.addInventoryCondition((clicker, slot, clickType, result) -> {
                if(slot!= finalI) return;

                if(Server.playersInSession.containsKey(player)){
                    Server.playersInSession.get(player).quit();
                }

                if(clickType== ClickType.RIGHT_CLICK){
                    Server.socketManager.sendParkourRequest(player, map.getUuid(), ParkourSession.SessionAction.PLAY);
                    result.setCancel(true);
                    player.closeInventory();
                    player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<aqua>Loading map"), MiniMessage.miniMessage().deserialize("<gray>Your map is being loaded..."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(10), Duration.ZERO)));
                    return;
                }
                Server.socketManager.sendParkourRequest(player, map.getUuid(), ParkourSession.SessionAction.EDIT);
                result.setCancel(true);
                player.closeInventory();
                player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<aqua>Loading Editor"), MiniMessage.miniMessage().deserialize("<gray>Your map editor is being loaded..."), Title.Times.times(Duration.ZERO, Duration.ofSeconds(10), Duration.ZERO)));

            });
        }
        if(maps.size()<45){
            for(int i=0;i<54-maps.size();i++){
                inventory.setItemStack(53-i,
                        ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
                                .displayName(MiniMessage.miniMessage().deserialize("<!italic><gray>Click here to create a new <aqua>map<gray>."))
                                .build());
            }
            inventory.addInventoryCondition((clicker, slot, clickType, result) -> {
                if(slot>=maps.size()){
                    new MapCreatorGui(player);
                }
                result.setCancel(true);
            });
        }

        player.openInventory(inventory);
    }

    public EditorGui(Player player){
        init(player);
    }

    public EditorGui(Player player, Runnable onClose){
        InventoryClose.add(player, onClose);
        init(player);
    }

}
