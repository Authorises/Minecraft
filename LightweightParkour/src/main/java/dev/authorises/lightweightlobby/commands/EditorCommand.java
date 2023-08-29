package dev.authorises.lightweightlobby.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.game.MapCreatorGui;
import dev.authorises.lightweightlobby.game.MapEditor;
import dev.authorises.lightweightlobby.game.ParkourSession;
import dev.authorises.lightweightlobby.gui.EditorGui;
import dev.authorises.lightweightlobby.map.MapData;
import dev.authorises.lightweightlobby.map.MapType;
import dev.authorises.lightweightlobby.player.PlayerData;
import dev.authorises.lightweightlobby.util.PlayerInput;
import kong.unirest.Unirest;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import javax.sound.midi.MidiChannel;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class EditorCommand extends Command {
    EditorCommand() {
        super("editor");
        setCondition(((sender, commandString) -> !(sender instanceof ConsoleSender) ));

        addSyntax(((sender, context) -> {
            if(sender instanceof Player){
                new EditorGui((Player) sender);
            }
        }));
    }
}
