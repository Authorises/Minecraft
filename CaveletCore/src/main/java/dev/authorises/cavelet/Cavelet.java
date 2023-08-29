package dev.authorises.cavelet;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import dev.authorises.cavelet.chat.ChatChannel;
import dev.authorises.cavelet.chat.ChatManager;
import dev.authorises.cavelet.command.*;
import dev.authorises.cavelet.command.acf.*;
import dev.authorises.cavelet.command.tabcompleter.ItemTabCompletion;
import dev.authorises.cavelet.cthuluagent.CthuluAgentManager;
import dev.authorises.cavelet.cthuluagent.SaleRarity;
import dev.authorises.cavelet.cthuluagent.TimedSale;
import dev.authorises.cavelet.customenchants.CustomEnchantLists;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.customitems.*;
import dev.authorises.cavelet.customitems.managers.CustomMobLootManager;
import dev.authorises.cavelet.custommobs.CustomBossMob;
import dev.authorises.cavelet.deepmarket.DeepMarketManager;
import dev.authorises.cavelet.eventsystem.EventManager;
import dev.authorises.cavelet.factions.MFaction;
import dev.authorises.cavelet.factions.MFactionManager;
import dev.authorises.cavelet.forge.ForgeUpgrade;
import dev.authorises.cavelet.forge.ForgeUpgradeManager;
import dev.authorises.cavelet.gui.AmirGUI;
import dev.authorises.cavelet.intelligentblocks.IntelligentBlockManager;
import dev.authorises.cavelet.listeners.*;
import dev.authorises.cavelet.mining.*;
import dev.authorises.cavelet.newshop.NewShopManager;
import dev.authorises.cavelet.objectives.Objective;
import dev.authorises.cavelet.objectives.ObjectiveManager;
import dev.authorises.cavelet.playerdata.MProfile;
import dev.authorises.cavelet.runnables.FiveMinuteTick;
import dev.authorises.cavelet.scoreboard.MainScoreboard;
import dev.authorises.cavelet.shop.*;
import dev.authorises.cavelet.utils.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public final class Cavelet extends JavaPlugin {

    public static FileConfiguration dataFileConfig;
    public static File dataFile;
    public static OreManager oreManager;
    public static BuyableManager buyableManager;
    public static ObjectiveManager objectiveManager;
    public static CustomMobLootManager mobLootManager;
    public static EventManager eventManager;
    public static HashMap<UUID, MProfile> cachedMPlayers = new HashMap<UUID, MProfile>();
    public static HashMap<Player, Integer> shriekLevel = new HashMap<>();
    public static AmirGUI amirGUI;
    public static TextComponent killsLeaderboard;
    public static TextComponent balanceLeaderboard;
    public static long lastLeaderboardUpdate;
    public static ArrayList<TextComponent> tips = new ArrayList<>();
    public static ArrayList<ItemStackShop> willToAdd = new ArrayList<>();
    public static ArrayList<ItemStackShop> willAdded = new ArrayList<>();
    public static ItemStackShop willCurrent = null;
    public static MongoCollection playerData;
    public static MongoCollection factionsData;
    public static MongoCollection intelligentBlocks;
    public static MongoClient client;
    public static CustomEnchantLists customEnchantLists = new CustomEnchantLists();
    public static HashMap<UUID, CustomBossMob> customBossMobs = new HashMap<>();
    public static MiniMessage miniMessage = MiniMessage.miniMessage();
    public static LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().build();
    public static PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();
    public static DeepMarketManager deepMarketManager;
    public static Song eventSong;
    public static RegionContainer container;
    public static CustomItemsManager customItemsManager;
    public static ForgeUpgradeManager forgeUpgradeManager;
    public static CthuluAgentManager cthuluAgentManager;
    public static MFactionManager factionManager;
    public static IntelligentBlockManager intelligentBlockManager;
    public static NewShopManager newShopManager;
    public static ChatManager chatManager;
    public static Gson gson = new Gson();
    public static PaperCommandManager paperCommandManager;
    public static HashMap<Player, Player> lastDamager = new HashMap<>();

    public static CustomItemShopManager amirShop;
    public static CustomItemShopManager willShop;



    public static void updateBalanceLeaderboard(){
        TextComponent message = Component.text(ColorUtils.format("&bLeaderboard &7- &bBalance"));
        ArrayList<Component> componentArrayList = new ArrayList<>();

        ArrayList<Document> players = new ArrayList<>();
        FindIterable<Document> playersIterable = playerData.find();
        for(Document d : playersIterable){
            players.add(d);
        }
        AtomicReference<Integer> x = new AtomicReference<>(1);
        players.stream()
                .sorted((a1, a2) -> {
                    Double points1 = a1.getDouble("balance");
                    Double points2 = a2.getDouble("balance");
                    return (int) (points2-points1);
                })
                .limit(5)
                .forEach(f -> {
                    try {
                        MProfile mp = new MProfile(UUID.fromString(f.getString("_id")));
                        double balance = f.getDouble("balance");
                        Component c = Component.text(ColorUtils.format("\n&b#"+x+" &7- &f"+mp.getLastUsername()+"&7 - &b$"+String.format("%,.0f", balance)))
                                .hoverEvent(HoverEvent.showText(Component.text(ColorUtils.format("&7Click to view &b"+mp.getLastUsername()))))
                                .clickEvent(ClickEvent.runCommand("/view "+mp.getUuid().toString()));
                        x.updateAndGet(v -> v + 1);
                        componentArrayList.add(c);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        for(Component c : componentArrayList){
            message=message.append(c);
        }
        balanceLeaderboard = message.toBuilder().build();
    }

    public static void updateKillsLeaderboard(){
        TextComponent message = Component.text(ColorUtils.format("&bLeaderboard &7- &cKills"));
        ArrayList<Component> componentArrayList = new ArrayList<>();

        ArrayList<Document> players = new ArrayList<>();
        FindIterable<Document> playersIterable = playerData.find();
        for(Document d : playersIterable){
            players.add(d);
        }
        AtomicReference<Integer> x = new AtomicReference<>(1);
        players.stream()
                .sorted((a1, a2) -> {
                    Double points1 = a1.getDouble("kills");
                    Double points2 = a2.getDouble("kills");
                    return (int) (points2-points1);
                })
                .limit(5)
                .forEach(f -> {
                    try {
                        MProfile mp = new MProfile(UUID.fromString(f.getString("_id")));
                        double kills = f.getDouble("kills");
                        Component c = Component.text(ColorUtils.format("\n&c#"+x+" &7- &f"+mp.getLastUsername()+"&7 - &c"+String.format("%,.0f", kills)))
                                .hoverEvent(HoverEvent.showText(Component.text(ColorUtils.format("&7Click to view &b"+mp.getLastUsername()))))
                                .clickEvent(ClickEvent.runCommand("/view "+mp.getUuid().toString()));
                        x.updateAndGet(v -> v + 1);
                        componentArrayList.add(c);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        for(Component c : componentArrayList){
            message=message.append(c);
        }
        killsLeaderboard = message.toBuilder().build();
    }

    @Override
    public void onEnable() {

        container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        boolean NoteBlockAPI = true;
        if (!Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")){
            getLogger().severe("*** NoteBlockAPI is not installed or not enabled. ***");
            NoteBlockAPI = false;
            return;
        }

        eventSong = NBSDecoder.parse(new File(this.getDataFolder().getAbsolutePath()+"/songs/event.nbs"));

        // Schedule lag task to keep track of TPS
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);


        // Token for InfluxDB
        String token = "cMPF_ULRD_vjM8I0ixXDUv6GcDg_rGjmpjpqXAZj4vaqDJ3mVDND_wD2beBXi0iLWua-4lxrU4n4sgK4oD6yqw==";

        InfluxDBClient influxDBClient = InfluxDBClientFactory.create("http://38.242.136.220:8086", token.toCharArray());

        client = new MongoClient(new MongoClientURI("mongodb+srv://admin:2kmhk18V6v5PKpfs@instinctia.lihu7lr.mongodb.net/?retryWrites=true&w=majority"));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                customBossMobs.forEach((id, mob) -> {
                    mob.updateName();
                });
            }
        },0L, 10L);


        playerData = client.getDatabase("cavelet").getCollection("PlayerData");
        factionsData = client.getDatabase("cavelet").getCollection("FactionsData");
        intelligentBlocks = client.getDatabase("cavelet").getCollection("IntelligentBlocks");

        // Initiate the Five-Minute Tick task
        new FiveMinuteTick(this);

        newShopManager = new NewShopManager();

        chatManager = new ChatManager();

        try {
            intelligentBlockManager = new IntelligentBlockManager();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        new DisableSpawnerMobAI();

        // Register all listeners
        new ListenerRegisterer(this,
                new EnchantGuiListener(),
                new CustomBossMobListeners(),
                new DisableCraftingListener(),
                new MinesBreakBlockListener(),
                new PlayerDataQuitListener(),
                new PlayerDataJoinListener(),
                new FarmsBreakBlockListener(),
                new PlayerDeathListener(),
                new ChatFormatListener(),
                new PlayerSwapViewListener(),
                new AnvilGuiListener(),
                new SpawnerMobListeners(),
                new PlayerDamageListener(),
                intelligentBlockManager
                );

        oreManager = new OreManager();
        buyableManager = new BuyableManager();
        objectiveManager = new ObjectiveManager();
        mobLootManager = new CustomMobLootManager();
        customItemsManager = new CustomItemsManager();
        //deepMarketManager = new DeepMarketManager();
        forgeUpgradeManager = new ForgeUpgradeManager();

        amirShop = new CustomItemShopManager();
        willShop = new CustomItemShopManager();

        try {
            eventManager = new EventManager();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Bukkit.getLogger().info("Error loading events, shutting down");
            Bukkit.getServer().shutdown();
            throw new RuntimeException(e);
        }
        factionManager = new MFactionManager();

        try {
            customItemsManager.init();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Bukkit.getLogger().info("Error loading custom items, shutting down");
            Bukkit.getServer().shutdown();
            throw new RuntimeException(e);
        }
        MainScoreboard.init();

        // Register Commands

        paperCommandManager = new PaperCommandManager(this);

        paperCommandManager.getCommandCompletions().registerAsyncCompletion("events", c -> {
            return eventManager.loadedEvents.keySet();
        });

        paperCommandManager.getCommandCompletions().registerAsyncCompletion("currencies", c -> {
            return List.of("money", "souls");
        });

        paperCommandManager.getCommandCompletions().registerAsyncCompletion("chatchannels", c -> {
            List<String> channels = new ArrayList<>();
            for(ChatChannel chatChannel : ChatChannel.values()){
                if(chatChannel.hasAccess.test(c.getPlayer())){
                    channels.add(chatChannel.toString().toLowerCase());
                }
            }
            return channels;
        });


        paperCommandManager.getCommandCompletions().registerAsyncCompletion("items", c -> {
            return customItemsManager.customItemHashMap.keySet();
        });

        paperCommandManager.getCommandCompletions().registerAsyncCompletion("factions", c -> {
            return factionManager.factionNames;
        });

        paperCommandManager.getCommandCompletions().registerAsyncCompletion("factionmembers", c -> {
            if(factionManager.playersFactions.containsKey(c.getPlayer().getUniqueId())){
                MFaction faction = factionManager.playersFactions.get(c.getPlayer().getUniqueId());
                return faction.lastUsernames.values();
            }else{
                return List.of();
            }
        });

        paperCommandManager.registerCommand(new EventCommand());
        paperCommandManager.registerCommand(new NewFactionCommand());
        paperCommandManager.registerCommand(new CollectCommand());
        paperCommandManager.registerCommand(new BalanceCommand());
        paperCommandManager.registerCommand(new ChatCommand());

        //Objects.requireNonNull(getCommand("faction")).setExecutor(new FactionCommand());
        Objects.requireNonNull(getCommand("npcgui")).setExecutor(new NPCGuiCommand());
        Objects.requireNonNull(getCommand("view")).setExecutor(new ViewCommand());
        Objects.requireNonNull(getCommand("leaderboard")).setExecutor(new LeaderboardCommand());
        Objects.requireNonNull(getCommand("spawncustommob")).setExecutor(new SummonCustomMobCommand());
        Objects.requireNonNull(getCommand("migrateolddata")).setExecutor(new MigrateOldDataCommand());
        Objects.requireNonNull(getCommand("apply")).setExecutor(new ApplyCommand());
        Objects.requireNonNull(getCommand("discord")).setExecutor(new DiscordCommand());
        Objects.requireNonNull(getCommand("iteminfo")).setExecutor(new ItemInfoCommand());
        Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
        Objects.requireNonNull(getCommand("caveletver")).setExecutor(new CaveletVersionCommand());
        Objects.requireNonNull(getCommand("addcustomenchant")).setExecutor(new AddCustomEnchantCommand());
        Objects.requireNonNull(getCommand("logintoprofile")).setExecutor(new LoginToProfileCommand());
        Objects.requireNonNull(getCommand("deepmarket")).setExecutor(new DeepMarketCommand());
        Objects.requireNonNull(getCommand("enchantments")).setExecutor(new EnchantmentsCommand());
        Objects.requireNonNull(getCommand("skills")).setExecutor(new SkillsCommand());
        Objects.requireNonNull(getCommand("ftop")).setExecutor(new FactionTopCommand());

        Objects.requireNonNull(getCommand("item")).setTabCompleter(new ItemTabCompletion());
        //Objects.requireNonNull(getCommand("faction")).setTabCompleter(new FactionTabCompletion());


        tips.add(Component.text(ColorUtils.format("&e&lTIP&7 Press &bSHIFT &7+ "))
                .append(Component.keybind("key.swapOffhand").color(NamedTextColor.AQUA).toBuilder().build())
                .append(Component.text(ColorUtils.format("&7 to view your stats!"))));

        tips.add(Component.text(ColorUtils.format("&e&lTIP&7 You can see your skills and their perks with /skills")));
        tips.add(Component.text(ColorUtils.format("&e&lTIP&7 Using the anvil at the market, you can add special enchants to items")));
        tips.add(Component.text(ColorUtils.format("&e&lTIP&7 To view all enchantments and their functions, click on the enchanting table and click view all enchantments")));
        tips.add(Component.text(ColorUtils.format("&e&lTIP&7 View someone else's stats with &b/view username/uuid&7.")));
        tips.add(Component.text(ColorUtils.format("&e&lTIP&7 Completing objectives will reward you with special objective boxes.")));

        objectiveManager.addObjective(new Objective(
                "warp_to_farm",
                "&fWarp to the &e&lFarm&r",
                Material.WHEAT,
                "&fWarp to the &e&lFarm&f by clicking on the &delevator&f. &7(Iron Door)",
                true));

        objectiveManager.addObjective(new Objective(
                "farming_level_2",
                "&fReach &eFarming Level 2&r",
                Material.WHEAT,
                "&fHarvest &eWheat&f until you reach &eFarming Level 2&r.",
                false));

        objectiveManager.addObjective(new Objective(
                "warp_to_market",
                "&fWarp to the &a&lMarket&r",
                Material.SCULK_SENSOR,
                "&fWarp to the &a&lMarket&f by clicking on the &delevator&f. &7(Iron Door)",
                false));

        objectiveManager.addObjective(new Objective(
                "talk_to_david",
                "&fTalk to &bDavid&f.",
                Material.PLAYER_HEAD,
                "&eRIGHT CLICK&f on David at the &a&lMarket&f.",
                false));

        objectiveManager.addObjective(new Objective(
                "sell_wheat",
                "&fSell wheat&f.",
                Material.WHEAT,
                "&fSell &eWheat&f to &bDavid&f.",
                false));

        objectiveManager.addObjective(new Objective(
                "warp_to_mine",
                "&fWarp to the &b&lMine&r",
                Material.DIAMOND_PICKAXE,
                "&fWarp to the &b&lMine&f by clicking on the &delevator&f. &7(Iron Door)",
                false));

        objectiveManager.addObjective(new Objective(
                "mining_level_2",
                "&fReach &bFarming Level 2&r",
                Material.DIAMOND_PICKAXE,
                "&fMine ores until you reach &bMining Level 2&f.",
                false));

        objectiveManager.addObjective(new Objective(
                "talk_to_joey",
                "&fTalk to &bJoey&f.",
                Material.PLAYER_HEAD,
                "&eRIGHT CLICK&f on Joey at the &a&lMarket&f.",
                false));

        objectiveManager.addObjective(new Objective(
                "sell_ores",
                "&fSell ores&f.",
                Material.DIAMOND,
                "&fSell &bores&f to &bJoey&f.",
                true));

        objectiveManager.addObjective(new Objective(
                "warp_to_dangerzone",
                "&fWarp to the &c&lDanger Zone&r",
                Material.DIAMOND_SWORD,
                "&fWarp to the &c&lDanger Zone&f by clicking on the &delevator&f. &7(Iron Door)",
                false));

        willToAdd.add(new ItemStackShop(
                Material.COBWEB,
                ShopType.TEMPORARY,
                System.currentTimeMillis()+1250000L,
                "COBWEB",
                "&dCobweb",
                new ItemStack(Material.COBWEB),
                100D,
                BuyableFrom.WILL
        ));

        willToAdd.add(new ItemStackShop(
                Material.GLASS,
                ShopType.TEMPORARY,
                System.currentTimeMillis()+1250000L,
                "GLASS",
                "&fGlass",
                new ItemStack(Material.GLASS),
                100D,
                BuyableFrom.WILL
        ));

        willToAdd.add(new ItemStackShop(
                Material.ENDER_CHEST,
                ShopType.TEMPORARY,
                System.currentTimeMillis()+1250000L,
                "ENDER_CHEST",
                "&dEnder Chest",
                new ItemStack(Material.ENDER_CHEST),
                1250D,
                BuyableFrom.WILL
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.DIAMOND_PICKAXE,
                ShopType.PERMANENT,
                0L,
                "DIAMOND_PICKAXE",
                "&bDiamond Pickaxe",
                new ItemBuilder(Material.DIAMOND_PICKAXE)
                        .setName(ColorUtils.format("&bDiamond Pickaxe"))
                        .toItemStack(),
                1000D,
                BuyableFrom.AMIR
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.GOLDEN_APPLE,
                ShopType.PERMANENT,
                0L,
                "GOLDEN_APPLE",
                "&6Golden Apple",
                new ItemBuilder(Material.GOLDEN_APPLE)
                        .setName(ColorUtils.format("&6Golden Apple"))
                        .toItemStack(),
                5000D,
                BuyableFrom.AMIR
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.DIAMOND_SWORD,
                ShopType.PERMANENT,
                1660413954954L,
                "DIAMOND_SWORD",
                "&bDiamond Sword",
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .setName(ColorUtils.format("&bDiamond Sword"))
                        .addFlags(ItemFlag.HIDE_UNBREAKABLE)
                        .toItemStack(),
                1500D,
                BuyableFrom.AMIR
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.DIAMOND_PICKAXE,
                ShopType.TEMPORARY,
                1660313954954L,
                "SUPER_DIAMOND_PICKAXE",
                "&dSuper Diamond Pickaxe",
                new ItemBuilder(Material.DIAMOND_PICKAXE)
                        .addFlags(ItemFlag.HIDE_UNBREAKABLE)
                        .addEnchant(Enchantment.DIG_SPEED, 5)
                        .toItemStack(),
                5000D,
                BuyableFrom.AMIR
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.STONE,
                ShopType.PERMANENT,
                1660313954954L,
                "STONE",
                "&fStone",
                new ItemStack(Material.STONE),
                10D,
                BuyableFrom.WILL
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.COBBLESTONE,
                ShopType.PERMANENT,
                1660313954954L,
                "COBBLESTONE",
                "&fCobblestone",
                new ItemStack(Material.COBBLESTONE),
                6D,
                BuyableFrom.WILL
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.OAK_LOG,
                ShopType.PERMANENT,
                1660313954954L,
                "OAK_LOG",
                "&fOak Log",
                new ItemStack(Material.OAK_LOG),
                30D,
                BuyableFrom.WILL
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.BREAD,
                ShopType.PERMANENT,
                1660313954954L,
                "BREAD",
                "&6Bread",
                new ItemStack(Material.BREAD),
                3D,
                BuyableFrom.WILL
        ));

        buyableManager.addBuyable(new ItemStackShop(
                Material.BREAD,
                ShopType.PERMANENT,
                1660313954954L,
                "BREAD",
                "&6Bread",
                new ItemStack(Material.BREAD),
                2D,
                BuyableFrom.AMIR
        ));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                5,
                OreSpawnRarity.COMMON,
                Material.COAL,
                Material.COAL_ORE,
                "coal",
                "coal",
                "&7Coal"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                10,
                OreSpawnRarity.COMMON,
                Material.COAL,
                Material.DEEPSLATE_COAL_ORE,
                "processed_coal",
                "processed_coal",
                "&7Coal"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                15,
                OreSpawnRarity.COMMON,
                Material.COAL,
                Material.COAL_BLOCK,
                "pure_coal",
                "pure_coal",
                "&7Coal"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                10,
                OreSpawnRarity.UNCOMMON,
                Material.RAW_IRON,
                Material.IRON_ORE,
                "iron",
                "iron",
                "&fIron"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                20,
                OreSpawnRarity.UNCOMMON,
                Material.IRON_INGOT,
                Material.DEEPSLATE_IRON_ORE,
                "processed_iron",
                "processed_iron",
                "&fIron"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                50,
                OreSpawnRarity.UNCOMMON,
                Material.IRON_INGOT,
                Material.IRON_BLOCK,
                "pure_iron",
                "pure_iron",
                "&fIron"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                20,
                OreSpawnRarity.UNCOMMON,
                Material.RAW_COPPER,
                Material.COPPER_ORE,
                "copper",
                "copper",
                "&6Copper"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                30,
                OreSpawnRarity.UNCOMMON,
                Material.COPPER_INGOT,
                Material.DEEPSLATE_COPPER_ORE,
                "processed_copper",
                "processed_copper",
                "&6Copper"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                90,
                OreSpawnRarity.UNCOMMON,
                Material.COPPER_INGOT,
                Material.COPPER_BLOCK,
                "pure_copper",
                "pure_copper",
                "&6Copper"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                50,
                OreSpawnRarity.RARE,
                Material.GOLD_INGOT,
                Material.GOLD_ORE,
                "gold",
                "gold",
                "&eGold"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                70,
                OreSpawnRarity.RARE,
                Material.GOLD_INGOT,
                Material.DEEPSLATE_GOLD_ORE,
                "processed_gold",
                "processed_gold",
                "&eGold"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                100,
                OreSpawnRarity.RARE,
                Material.GOLD_INGOT,
                Material.GOLD_BLOCK,
                "pure_gold",
                "pure_gold",
                "&eGold"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                100,
                OreSpawnRarity.RARE,
                Material.REDSTONE,
                Material.REDSTONE_ORE,
                "ruby",
                "ruby",
                "&cRuby"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                160,
                OreSpawnRarity.RARE,
                Material.REDSTONE,
                Material.DEEPSLATE_REDSTONE_ORE,
                "processed_ruby",
                "processed_ruby",
                "&cRuby"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                280,
                OreSpawnRarity.RARE,
                Material.REDSTONE,
                Material.REDSTONE_BLOCK,
                "pure_ruby",
                "pure_ruby",
                "&cRuby"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                140,
                OreSpawnRarity.RARE,
                Material.EMERALD,
                Material.EMERALD_ORE,
                "emerald",
                "emerald",
                "&aEmerald"));


        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                180,
                OreSpawnRarity.RARE,
                Material.EMERALD,
                Material.DEEPSLATE_EMERALD_ORE,
                "processed_emerald",
                "processed_emerald",
                "&aEmerald"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                350,
                OreSpawnRarity.RARE,
                Material.EMERALD,
                Material.EMERALD_BLOCK,
                "pure_emerald",
                "pure_emerald",
                "&aEmerald"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                200,
                OreSpawnRarity.DIVINE,
                Material.LAPIS_LAZULI,
                Material.LAPIS_ORE,
                "sapphire",
                "sapphire",
                "&1Sapphire"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                400,
                OreSpawnRarity.DIVINE,
                Material.LAPIS_LAZULI,
                Material.DEEPSLATE_LAPIS_ORE,
                "processed_sapphire",
                "processed_sapphire",
                "&1Sapphire"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                900,
                OreSpawnRarity.DIVINE,
                Material.LAPIS_LAZULI,
                Material.LAPIS_BLOCK,
                "pure_sapphire",
                "pure_sapphire",
                "&1Sapphire"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.DEFAULT,
                500,
                OreSpawnRarity.DIVINE,
                Material.DIAMOND,
                Material.DIAMOND_ORE,
                "diamond",
                "diamond",
                "&bDiamond"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PROCESSED,
                900,
                OreSpawnRarity.DIVINE,
                Material.DIAMOND,
                Material.DEEPSLATE_DIAMOND_ORE,
                "processed_diamond",
                "processed_diamond",
                "&bDiamond"));

        oreManager.addOre(new Ore(
                OreType.NORMAL,
                OreRarity.PURE,
                1250,
                OreSpawnRarity.DIVINE,
                Material.DIAMOND,
                Material.DIAMOND_BLOCK,
                "pure_diamond",
                "pure_diamond",
                "&bDiamond"));


        // Register all Basic Custom Items
        new RegisterBasicCustomItems();


        CustomItemShop breadShop = new CustomItemShop(
                Cavelet.customItemsManager.getItemById("BREAD"),
                20D
        );

        amirShop.addShop(breadShop);
        willShop.addShop(breadShop);

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("DIAMOND_PICKAXE"),
                2000D
        ));

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("DIAMOND_SWORD"),
                2000D
        ));

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("GOLDEN_APPLE"),
                2000D
        ));

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("MINER_BOOTS"),
                2000D
        ));

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("MINER_LEGGINGS"),
                2000D
        ));

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("MINER_CHESTPLATE"),
                2000D
        ));

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("MINER_HELMET"),
                2000D
        ));

        amirShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("AMIR_HEALTH_SHOT"),
                4500D
        ));

        willShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("STONE"),
                10D
        ));

        willShop.addShop(new CustomItemShop(
                Cavelet.customItemsManager.getItemById("COBBLESTONE"),
                7D
        ));

        /**

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("DUST"),
                10D,
                2000D,
                20D,
                DeepMarketCategory.MOB_DROPS,
                Material.GUNPOWDER));

        deepMarketManager.addItem(new DeepMarketItem(
                customItemsManager.getItemById("CTHULU_FRAGMENT"),
                30D,
                6000D,
                30D,
                DeepMarketCategory.MOB_DROPS,
                Material.LARGE_AMETHYST_BUD));

         */

        HashMap<CItemBlueprint<?>, Integer> requiredItems = new HashMap<>();
        HashMap<CustomEnchantType, Integer> addedEnchants = new HashMap<>();


        requiredItems.clear();
        addedEnchants.clear();
        requiredItems.put(customItemsManager.getItemById("COAL"), 128);
        requiredItems.put(customItemsManager.getItemById("COAL"), 128);

        addedEnchants.put(CustomEnchantType.EXPERIBOOST, 1);

        forgeUpgradeManager.addUpgrade(new ForgeUpgrade(
                Material.LIGHT,
                requiredItems,
                25000D,
                550D,
                addedEnchants,
                "Experi-Boost",
                "EXPERIBOOST",
                4,
                4,
                4
        ));

        requiredItems = new HashMap<>();
        addedEnchants = new HashMap<>();
        requiredItems.put(customItemsManager.getItemById("CTHULU_FRAGMENT"), 12);
        requiredItems.put(customItemsManager.getItemById("DUST"), 3);
        requiredItems.put(customItemsManager.getItemById("COAL"), 32);

        addedEnchants.put(CustomEnchantType.SILKTOUCH, 1);

        forgeUpgradeManager.addUpgrade(new ForgeUpgrade(
                Material.SPAWNER,
                requiredItems,
                45000D,
                250D,
                addedEnchants,
                "Silk Touch",
                "SILKTOUCH",
                0,
                0,
                10
        ));

        cthuluAgentManager = new CthuluAgentManager(
                new TimedSale(
                        7,
                    customItemsManager.getItemById("CTHULU_BOOTS"),
                    1,
                    15,
                    SaleRarity.NORMAL),
                new TimedSale(
                        3,
                        customItemsManager.getItemById("CTHULU_HELMET"),
                        1,
                        35,
                        SaleRarity.NORMAL),
                new TimedSale(
                        3,
                        customItemsManager.getItemById("CTHULU_CHESTPLATE"),
                        1,
                        35,
                        SaleRarity.SUPER_RARE),
                new TimedSale(
                        7,
                        customItemsManager.getItemById("CTHULU_LEGGINGS"),
                        1,
                        15,
                        SaleRarity.RARE),
                new TimedSale(
                        6,
                        customItemsManager.getItemById("CTHULU_REMEDY"),
                        1,
                        4,
                        SaleRarity.RARE),
                new TimedSale(
                        3,
                        customItemsManager.getItemById("PORTABLE_VAULT"),
                        1,
                        60,
                        SaleRarity.RARE),
                new TimedSale(
                        2,
                        customItemsManager.getItemById("ELEMENTAL"),
                        1,
                        200,
                        SaleRarity.SINGULARITY),
                new TimedSale(
                        10,
                        customItemsManager.getItemById("GOLDEN_APPLE"),
                        64,
                        24,
                        SaleRarity.NORMAL)
        );




        /**
        BlockDropMatchManager blockDropMatchManager = new BlockDropMatchManager();
        blockDropMatchManager.addMatch(Material.MUD, new CItem(Cavelet.customItemsManager.getItemById("MUD")).getItemStack());
        blockDropMatchManager.addMatch(Material.GRASS_BLOCK, new CItem(Cavelet.customItemsManager.getItemById("DIRT")).getItemStack());
        blockDropMatchManager.addMatch(Material.DIRT, new CItem(Cavelet.customItemsManager.getItemById("DIRT")).getItemStack());
        blockDropMatchManager.addMatch(Material.SAND, new CItem(Cavelet.customItemsManager.getItemById("SAND")).getItemStack());
        blockDropMatchManager.addMatch(Material.CLAY, new CItem(Cavelet.customItemsManager.getItemById("DIRT")).getItemStack());
        blockDropMatchManager.addMatch(Material.GRAVEL, new CItem(Cavelet.customItemsManager.getItemById("GRAVEL")).getItemStack());
        blockDropMatchManager.addMatch(Material.GRASS, new CItem(Cavelet.customItemsManager.getItemById("SEEDS")).getItemStack());
        blockDropMatchManager.addMatch(Material.FERN, new CItem(Cavelet.customItemsManager.getItemById("SEEDS")).getItemStack());
        blockDropMatchManager.addMatch(Material.TALL_GRASS, new CItem(Cavelet.customItemsManager.getItemById("SEEDS")).getItemStack());
        blockDropMatchManager.addMatch(Material.LARGE_FERN, new CItem(Cavelet.customItemsManager.getItemById("SEEDS")).getItemStack());
        Bukkit.getPluginManager().registerEvents(blockDropMatchManager, this);
         */

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(World w : Bukkit.getWorlds()){
                for(Entity entity : w.getEntities()){
                    if(entity.getType()== EntityType.SKELETON_HORSE && !customBossMobs.containsKey(entity.getUniqueId())){
                        entity.remove();
                    }
                }
            }
        }, 0L, 150L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            cachedMPlayers.values().forEach((mp) -> {
                if(mp.combatLogLeft!=null && mp.combatLogLeft!=-1){
                    if(mp.combatLogLeft-1<1){
                        mp.combatLogLeft=-1;
                    }else{
                        mp.combatLogLeft-=1;
                    }
                }
            });

        }, 0L, 20L);



    }


    @Override
    public void onDisable() {

    }
}
