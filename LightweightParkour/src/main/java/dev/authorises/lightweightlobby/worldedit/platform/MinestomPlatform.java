package dev.authorises.lightweightlobby.worldedit.platform;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.*;
import com.sk89q.worldedit.internal.Constants;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.SideEffect;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.Registries;
import dev.authorises.lightweightlobby.Server;
import dev.authorises.lightweightlobby.worldedit.WorldEditListener;
import dev.authorises.lightweightlobby.worldedit.platform.actors.MinestomConsole;
import dev.authorises.lightweightlobby.worldedit.platform.actors.MinestomPlayer;
import dev.authorises.lightweightlobby.worldedit.platform.adapters.MinestomAdapter;
import dev.authorises.lightweightlobby.worldedit.platform.adapters.MinestomWorld;
import dev.authorises.lightweightlobby.worldedit.platform.config.WorldEditConfiguration;
import dev.authorises.lightweightlobby.worldedit.platform.misc.WorldEditCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.client.play.ClientPluginMessagePacket;
import org.enginehub.piston.CommandManager;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class MinestomPlatform extends AbstractPlatform implements MultiUserPlatform {
    private HashMap<UUID, MinestomPlayer> playerMap = new HashMap<>();
    private MinestomRegistries registries = new MinestomRegistries();

    public MinestomPlatform() {

    }

    public MinestomWorld getWorld(Instance instance) {
        return new MinestomWorld(instance);
    }

    public MinestomPlayer getPlayer(Player player) {
        return playerMap.computeIfAbsent(player.getUuid(), k -> new MinestomPlayer(this, player));
    }

    public void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    @Override
    public Collection<Actor> getConnectedUsers() {
        return MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .map(MinestomAdapter.INSTANCE::asActor)
                .collect(Collectors.toList());
    }

    @Override
    public void reload() {
    }

    @Override
    public Registries getRegistries() {
        return registries;
    }

    @Override
    public int getDataVersion() {
        return Constants.DATA_VERSION_MC_1_18;
    }

    @Override
    public boolean isValidMobType(String type) {
        return false;
    }

    @Nullable
    @Override
    public com.sk89q.worldedit.entity.Player matchPlayer(com.sk89q.worldedit.entity.Player player) {
        return player;
    }

    @Nullable
    @Override
    public World matchWorld(World world) {
        return world;
    }

    @Override
    public void registerCommands(CommandManager commandManager) {
        commandManager.getAllCommands().forEach((cmd) -> {
            MinecraftServer.getCommandManager().register(new WorldEditCommand(cmd));
        });
    }

    @Override
    public void setGameHooksEnabled(boolean enabled) {
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
        WorldEdit we = WorldEdit.getInstance();
        WorldEditListener.register(MinecraftServer.getGlobalEventHandler(), this, we);
    }

    @Override
    public LocalConfiguration getConfiguration() {
        return Server.worldEditConfig;
    }

    @Override
    public String getVersion() {
        return getPlatformVersion();
    }

    @Override
    public String getPlatformName() {
        return "WorldEdit-Minestom";
    }

    @Override
    public String getPlatformVersion() {
        return "1.0.0";
    }

    @Override
    public Map<Capability, Preference> getCapabilities() {
        Map<Capability, Preference> capabilities = new HashMap<>();
        capabilities.put(Capability.CONFIGURATION, Preference.NORMAL);
        capabilities.put(Capability.WORLD_EDITING, Preference.NORMAL);
        capabilities.put(Capability.GAME_HOOKS, Preference.NORMAL);
        capabilities.put(Capability.PERMISSIONS, Preference.NORMAL);
        capabilities.put(Capability.WORLDEDIT_CUI, Preference.NORMAL);
        capabilities.put(Capability.USER_COMMANDS, Preference.NORMAL);
        return capabilities;
    }

    @Override
    public Set<SideEffect> getSupportedSideEffects() {
        return new HashSet<>();
    }

}