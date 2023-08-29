package dev.authorises.vortechfactions;

import cc.javajobs.factionsbridge.FactionsBridge;
import cc.javajobs.factionsbridge.bridge.infrastructure.struct.FactionsAPI;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.authorises.vortechfactions.combatlogger.CombatLogger;
import dev.authorises.vortechfactions.commands.*;
import dev.authorises.vortechfactions.items.ItemHandler;
import dev.authorises.vortechfactions.items.Items;
import dev.authorises.vortechfactions.kits.KitsListener;
import dev.authorises.vortechfactions.listeners.*;
import dev.authorises.vortechfactions.masks.Mask;
import dev.authorises.vortechfactions.masks.events.maskBlockEvents;
import dev.authorises.vortechfactions.masks.events.maskInteractEvents;
import dev.authorises.vortechfactions.masks.maskManager;
import dev.authorises.vortechfactions.scoreboard.FactionsScoreboard;
import dev.authorises.vortechfactions.settings.SettingsManager;
import dev.authorises.vortechfactions.shop.ShopItem;
import dev.authorises.vortechfactions.rng.RNGChanceItem;
import dev.authorises.vortechfactions.rng.listeners.FarmingListeners;
import dev.authorises.vortechfactions.rng.listeners.GrindingListeners;
import dev.authorises.vortechfactions.utilities.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;
import us.figt.loafmenus.LoafMenuRegistrar;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.logging.Logger;

public final class VortechFactions extends JavaPlugin implements Listener {

    public static Economy econ = null;
    public static Logger log = Bukkit.getLogger();
    public static airdropManager airdropManager = new airdropManager();
    public static FactionsAPI api;
    public static final List<Class<? extends ItemHandler>> itemHandlers = new ArrayList<>();
    public static final List<ShopItem> shopItems = new ArrayList<>();
    public static final List<RNGChanceItem> rngChances = new ArrayList<>();
    public static final List<Mask> masks = new ArrayList<>();
    public static ProtocolManager protocolManager;
    public static FileConfiguration dataFileConfig;
    public static File dataFile;
    public static HashMap<String, Integer> cactusCache = new HashMap<>();
    public static HashMap<String, Integer> squidCache = new HashMap<>();
    public static LoafMenuRegistrar loafMenuRegistrar;
    public static ArrayList<Location> rocketLocs = new ArrayList<>();
    public static SettingsManager settingsManager;
    public static CombatLogger combatLogger;
    public static FactionsScoreboard scoreboard;
    public static ArrayList<UUID> invincibleEntities = new ArrayList<>();

    public FileConfiguration getData() {
        return this.dataFileConfig;
    }


    private void createData() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }

        dataFileConfig = new YamlConfiguration();
        try {
            dataFileConfig.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        // Generate pages for shop.
        Set<Class<? extends ItemHandler>> itemHandlers1 = new Reflections("dev.authorises.vortechfactions.")
                .getSubTypesOf(ItemHandler.class);
        Set<Class<? extends ShopItem>> shopItems1 = new Reflections("dev.authorises.vortechfactions.")
                .getSubTypesOf(ShopItem.class);
        Set<Class<? extends RNGChanceItem>> rngChances1 = new Reflections("dev.authorises.vortechfactions.")
                .getSubTypesOf(RNGChanceItem.class);
        Set<Class<? extends Mask>> masks1 = new Reflections("dev.authorises.vortechfactions.")
                .getSubTypesOf(Mask.class);

        itemHandlers.addAll(itemHandlers1);

        for(Class<? extends Mask> c : masks1){
            try {
                masks.add(c.newInstance());
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        for(Class<? extends ShopItem> c : shopItems1){
            try {
                shopItems.add(c.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        for(Class<? extends RNGChanceItem> c : rngChances1){
            try {
                rngChances.add(c.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        log.info(itemHandlers.size()+" Item handlers loaded");
        log.info(shopItems.size()+" Shop items loaded");

        /**
        int test = 0;
        while (test<100){
            test +=1;
            shopItems.add(shopItems.get(0));
            log.info(""+shopItems.size());
            //itemHandlers.add(obsidianShopItem<)
            //itemHandlers.add(new Reflections("dev.authorises.vortechfactions.shopitems.obsidianShopItem").getSubTypesOf(ShopItem.class).iterator().next().asSubclass(new obsidianShopItem().getClass()));
        }
        */

        try {
            loafMenuRegistrar = new LoafMenuRegistrar(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy() ) {
            log.severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Create the data file
        createData();

        // Enable ProcolLib
        protocolManager = ProtocolLibrary.getProtocolManager();

        getCommand("value").setExecutor(new valueCommand());
        getCommand("shop").setExecutor(new shopCommand());
        getCommand("factionsettings").setExecutor(new factionSettingsCommand());
        getCommand("sell").setExecutor(new sellCommand());
        getCommand("getitem").setExecutor(new getItem());
        getCommand("value").setExecutor(new valueCommand());
        getCommand("getmask").setExecutor(new getMask());
        getCommand("airdrop").setExecutor(new airdropCommand());
        getCommand("spawn").setExecutor(new spawnCommand());
        getCommand("sethome").setExecutor(new sethomeCommand());
        getCommand("home").setExecutor(new homeCommand());
        getCommand("echest").setExecutor(new echestCommand());
        getCommand("chat").setExecutor(new chatCommand());
        getCommand("test").setExecutor(new testCommand());

        Items.init();
        api = FactionsBridge.getFactionsAPI();
        settingsManager = new SettingsManager();
        combatLogger = new CombatLogger();
        scoreboard = new FactionsScoreboard();
        scoreboard.init();
        // Easter eggs listener
        Bukkit.getPluginManager().registerEvents(new secretEvents(), this);
        // Enable events for custom items.
        Bukkit.getPluginManager().registerEvents(new customItemsListeners(), this);
        // Enable TNT protection blocks. ( netherrack )
        Bukkit.getPluginManager().registerEvents(new TntProtectionListener(), this);
        // Enable Magic Sand and Magic Gravel
        Bukkit.getPluginManager().registerEvents(new MagicsandListener(), this);
        // Enable /f alert event listener
        Bukkit.getPluginManager().registerEvents(new fAlertCommand(), this);
        // Enable /f settings event listener
        Bukkit.getPluginManager().registerEvents(new fSettingsCommand(), this);
        // Enable /f chunk event listener
        Bukkit.getPluginManager().registerEvents(new fChunkCommand(), this);
        // Enable chat listener
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        // Enable faction claim listener
        Bukkit.getPluginManager().registerEvents(new ClaimListener(), this);
        // Enable faction unclaim listener
        Bukkit.getPluginManager().registerEvents(new UnclaimListener(), this);
        // Enable faction create listener
        Bukkit.getPluginManager().registerEvents(new FactionCreateListener(), this);
        // Enable faction disband listener
        Bukkit.getPluginManager().registerEvents(new FactionDisbandListener(), this);
        // Enable Cactus listener
        Bukkit.getPluginManager().registerEvents(new CactusListener(), this);
        // Enable mask event listeners for blocks
        Bukkit.getPluginManager().registerEvents(new maskBlockEvents(), this);
        // Enable mask event listeners for interactions
        Bukkit.getPluginManager().registerEvents(new maskInteractEvents(), this);
        // Enable RNG event listeners for farming
        Bukkit.getPluginManager().registerEvents(new FarmingListeners(), this);
        // Enable RNG event listeners for grinding
        Bukkit.getPluginManager().registerEvents(new GrindingListeners(), this);
        // Enable event listeners for custom mob drops
        Bukkit.getPluginManager().registerEvents(new MobDropsListener(), this);
        // Enable player join event listener
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        // Enable player join leave listener
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        // Enable kits listener
        Bukkit.getPluginManager().registerEvents(new KitsListener(), this);
        // Enable airdrop listener
        Bukkit.getPluginManager().registerEvents(new AirdropListener(), this);
        // Enable QOL cane diamond hoe listener
        Bukkit.getPluginManager().registerEvents(new CaneBreakListener(), this);
        // Enable admin stack remover
        Bukkit.getPluginManager().registerEvents(new StackkillListener(), this);
        // Enable custom ender chest listener
        Bukkit.getPluginManager().registerEvents(new echestListener(), this);
        // Enable player death listener
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        // Enable player attack player listener
        Bukkit.getPluginManager().registerEvents(new PlayerHitPlayerListener(), this);
        // Enable invincible entity listener
        Bukkit.getPluginManager().registerEvents(new invincibleEntityListener(), this);

        // Enable cactus cache manager
        cactusCacheManager.start();

        // Enable squid cache manager
        squidCacheManager.start();

        // Enable mask manager
        maskManager.start();

        if(!(dataFileConfig.contains("used-kits"))){
            dataFileConfig.set("used-kits", new ArrayList<>());
        }
        if(!(dataFileConfig.contains("first-joins"))){
            dataFileConfig.set("first-joins", new ArrayList<>());
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                if(!(VortechFactions.airdropManager.isActive())){
                    VortechFactions.airdropManager.start();
                }
            }
        },0,36000);
    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
