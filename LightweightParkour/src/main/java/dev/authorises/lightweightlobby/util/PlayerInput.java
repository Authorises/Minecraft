package dev.authorises.lightweightlobby.util;

import dev.authorises.lightweightlobby.Server;
import io.minio.messages.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.*;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.network.packet.server.play.BlockEntityDataPacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTString;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

public class PlayerInput {
    private static final HashMap<UUID, Consumer<?>> callbackMap = new HashMap<>();
	public static List<Material> pickableIcons = List.of(
            Material.STONE,
            Material.OAK_SAPLING,
            Material.WARPED_NYLIUM,
            Material.SAND,
            Material.RAW_GOLD_BLOCK,
            Material.AMETHYST_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK,
            Material.SPONGE,
            Material.GLASS,
            Material.COBWEB,
            Material.GRASS,
            Material.FERN,
            Material.FLOWERING_AZALEA,
            Material.DEAD_BUSH,
            Material.POPPY,
            Material.TWISTING_VINES,
            Material.BOOKSHELF,
            Material.CHORUS_FLOWER,
            Material.CACTUS,
            Material.NOTE_BLOCK,
            Material.CARVED_PUMPKIN,
            Material.MELON,
            Material.REINFORCED_DEEPSLATE,
            Material.IRON_BARS,
            Material.LILY_PAD,
            Material.SCULK_VEIN,
            Material.ENCHANTING_TABLE,
            Material.END_PORTAL_FRAME,
            Material.BEACON,
            Material.ANVIL,
            Material.HAY_BLOCK,
            Material.LILAC,
            Material.SEA_LANTERN,
            Material.TURTLE_EGG,
            Material.CONDUIT,
            Material.TUBE_CORAL,
            Material.SCAFFOLDING,
            Material.SLIME_BALL,
            Material.SLIME_BLOCK,
            Material.HONEY_BLOCK,
            Material.OBSERVER,
            Material.DROPPER,
            Material.TARGET,
            Material.SCULK_SENSOR,
            Material.TNT,
            Material.REDSTONE_LAMP,
            Material.IRON_DOOR,
            Material.QUARTZ,
            Material.GUNPOWDER,
            Material.COMPASS,
            Material.RECOVERY_COMPASS,
            Material.CLOCK,
            Material.COOKIE,
            Material.ENDER_PEARL
    );

    private static final Set<Class<?>> ignoredClasses = Set.of(
        ClientAnimationPacket.class,
        ClientPongPacket.class,
        ClientPlayerPositionPacket.class,
        ClientPlayerRotationPacket.class,
        ClientPlayerPositionAndRotationPacket.class,
        ClientKeepAlivePacket.class
    );

    public static void doIconInput(Player player, Consumer<Material> consumer){
        Inventory pick = new Inventory(InventoryType.CHEST_6_ROW, MiniMessage.miniMessage().deserialize("<red>Pick an icon"));

        InventoryClose.add(player, () -> {
            consumer.accept(pickableIcons.get(0));
        });

        for(int i=0;i<pickableIcons.size();i++){
            Material material = pickableIcons.get(i);
            pick.addItemStack(ItemStack.builder(material)
                    .displayName(MiniMessage.miniMessage().deserialize("<!italic>"+material.namespace().value().toUpperCase()+" <gray>(Click)"))
                    .build());
            int finalI = i;
            pick.addInventoryCondition((player1, slot, clickType, inventoryConditionResult) -> {
                if(slot!= finalI) return;
                player1.closeInventory();
                InventoryClose.end(player1);
                consumer.accept(material);
                inventoryConditionResult.setCancel(true);
            });
        }

        player.openInventory(pick);
    }

    public static void doSignInput(Player player, Consumer<String> consumer) {
        doSignInput(player, "Enter Value", consumer);
    }
	
    public static void doSignInput(Player player, String prompt, Consumer<String> consumer) {
        UUID uuid = player.getUuid();
		
        if(callbackMap.containsKey(uuid)) {
            callbackMap.get(uuid).accept(null);
            callbackMap.remove(uuid);
        }
        callbackMap.put(uuid, consumer);
		
        final Point signPosition = new Vec(player.getPosition().blockX(), 0, player.getPosition().blockZ());
		
        BlockChangePacket packet = new BlockChangePacket(signPosition, Block.OAK_SIGN.stateId());
        player.getPlayerConnection().sendPacket(packet);
		
        NBTCompound nbt = new NBTCompound(
                Map.of(
                        "Text1", new NBTString(""),
                        "Text2", new NBTString(""),
                        "Text3", new NBTString("^^^^^^^^^^^"),
                        "Text4", new NBTString(prompt)
                )
        );
		
        BlockEntityDataPacket packet2 = new BlockEntityDataPacket(signPosition, 9, nbt);
        player.getPlayerConnection().sendPacket(packet2);
		
        OpenSignEditorPacket packet3 = new OpenSignEditorPacket(signPosition);
        player.getPlayerConnection().sendPacket(packet3);
		
        String id = "RegionParamShortStr-"+player.getUuid();
		
        MinecraftServer.getGlobalEventHandler().removeChildren(id);
        EventNode<PlayerEvent> node = EventNode.type(id, EventFilter.PLAYER, (playerevent, p) -> player == p).setPriority(999);
        node.addListener(PlayerPacketEvent.class, (event) -> {
            if(event.getPacket() instanceof ClientUpdateSignPacket) {
                ClientUpdateSignPacket updateSign = (ClientUpdateSignPacket) event.getPacket();
				
                callbackMap.remove(uuid);
                MinecraftServer.getGlobalEventHandler().removeChildren(id);
                if(event.getPlayer().getInstance() != null) {
                    BlockChangePacket blockChangePacket = new BlockChangePacket(signPosition, event.getPlayer().getInstance().getBlock(signPosition).stateId());
                    player.getPlayerConnection().sendPacket(blockChangePacket);
                }
				
                consumer.accept(updateSign.lines().get(0) + updateSign.lines().get(1));
            } else if(!ignoredClasses.contains(event.getPacket().getClass())) {
                callbackMap.remove(uuid);
                MinecraftServer.getGlobalEventHandler().removeChildren(id);
                if(event.getPlayer().getInstance() != null) {
                    BlockChangePacket blockChangePacket = new BlockChangePacket(signPosition, event.getPlayer().getInstance().getBlock(signPosition).stateId());
                    player.getPlayerConnection().sendPacket(blockChangePacket);
                }
				
                consumer.accept(null);
            }
        });
		
        node.addListener(PlayerDisconnectEvent.class, (event) -> {
            MinecraftServer.getGlobalEventHandler().removeChildren(id);
        });
		
        MinecraftServer.getGlobalEventHandler().addChild(node);
    }
}