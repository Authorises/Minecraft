package dev.authorises.vortechfactions.items;

import dev.authorises.vortechfactions.utilities.ColorUtils;
import dev.authorises.vortechfactions.utilities.ItemBuilder;
import dev.authorises.vortechfactions.utilities.textureUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Items {

    public static HashMap<String, ItemStack> items;
    public static HashMap<ItemMeta, Integer> metaSellValues;
    public static HashMap<Material, Float> materialSellValues;
    public static HashMap<String, RandomItemType> randomItemTypes;
    public static HashMap<String, ArrayList<ItemStack>> kits;
    public static HashMap<Material, Float> materialBuyValues;

    public static void init(){
        items = new HashMap<>();
        metaSellValues = new HashMap<>();
        materialSellValues = new HashMap<>();
        materialBuyValues = new HashMap<>();
        randomItemTypes = new HashMap<>();
        kits = new HashMap<>();

        materialSellValues.put(Material.CACTUS, 0.15F);
        materialSellValues.put(Material.SUGAR_CANE, 3F);

        materialBuyValues.put(Material.OBSIDIAN, 75F);
        materialBuyValues.put(Material.STONE, 25F);
        materialBuyValues.put(Material.WATER_BUCKET, 30F);
        materialBuyValues.put(Material.LAVA_BUCKET, 30F);
        materialBuyValues.put(Material.DIAMOND_SWORD, 250F);
        materialBuyValues.put(Material.DIAMOND_HOE, 225F);
        materialBuyValues.put(Material.GLOWSTONE, 45F);
        materialBuyValues.put(Material.SEA_LANTERN, 55F);
        materialBuyValues.put(Material.TNT, 95F);
        materialBuyValues.put(Material.DIODE, 30F);
        materialBuyValues.put(Material.REDSTONE, 50F);
        materialBuyValues.put(Material.REDSTONE_BLOCK, 450F);
        materialBuyValues.put(Material.REDSTONE_TORCH_ON, 55F);
        materialBuyValues.put(Material.STONE_PLATE, 50F);
        materialBuyValues.put(Material.SLIME_BLOCK, 60F);
        materialBuyValues.put(Material.REDSTONE_COMPARATOR, 40F);
        materialBuyValues.put(Material.DISPENSER, 45F);
        materialBuyValues.put(Material.DROPPER, 35F);
        materialBuyValues.put(Material.STONE_SLAB2, 75F);
        materialBuyValues.put(Material.FENCE, 30F);
        materialBuyValues.put(Material.SAND, 45F);
        materialBuyValues.put(Material.GLASS, 60F);
        materialBuyValues.put(Material.GRASS, 50F);
        materialBuyValues.put(Material.DIRT, 30F);
        materialBuyValues.put(Material.SUGAR_CANE, 35F);
        materialBuyValues.put(Material.CACTUS, 35F);


        //materialBuyValues.put(Material.REDST)

        ItemStack mysteryMask = textureUtils.createCustomSkull(1, ColorUtils.format("&B&LMystery Mask"), null, "c064983c78ef587c5e574b080a8454ffd62cc329c683a68f62af7f92a1f4a");

        ItemStack chunkBuster = new ItemStack(Material.BEACON);
        ItemMeta chunkBusterMeta = chunkBuster.getItemMeta();
        chunkBusterMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        chunkBusterMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON);
        chunkBusterMeta.setDisplayName(ColorUtils.format("&bChunk Buster &f(Place)"));
        ArrayList<String> chunkBusterlore = new ArrayList<>();
        chunkBusterlore.add(ColorUtils.format(""));
        chunkBusterlore.add(ColorUtils.format("&b&l* &fPlace this &b&nChunk Buster&f to "));
        chunkBusterlore.add(ColorUtils.format("&b&l* &fimmediately remove &c&nall blocks&f in "));
        chunkBusterlore.add(ColorUtils.format("&b&l* &fthe chunk below it's Y level."));
        chunkBusterMeta.setLore(chunkBusterlore);
        chunkBuster.setItemMeta(chunkBusterMeta);

        ItemStack iceBox = new ItemStack(Material.PACKED_ICE);
        ItemMeta iceBoxMeta = iceBox.getItemMeta();
        iceBoxMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_PLACED_ON);
        iceBoxMeta.setDisplayName(ColorUtils.format("&f&l*** &b&lIce Box&f&l ***"));
        ArrayList<String> iceBoxLore = new ArrayList<>();
        iceBoxLore.add(ColorUtils.format(""));
        iceBoxLore.add(ColorUtils.format("&b&lInside:"));
        iceBoxLore.add(ColorUtils.format("&f&l- &b$100,000"));
        iceBoxLore.add(ColorUtils.format("&f&l- &f1x &f&lFrost Armour"));
        iceBoxLore.add(ColorUtils.format("&f&l- &f1x &f&lFrost Weapon"));
        iceBoxMeta.setLore(iceBoxLore);
        iceBox.setItemMeta(iceBoxMeta);

        ItemStack seaBox = new ItemStack(Material.LAPIS_BLOCK);
        ItemMeta seaBoxMeta = seaBox.getItemMeta();
        seaBoxMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_PLACED_ON);
        seaBoxMeta.setDisplayName(ColorUtils.format("&f&l*** &b&lS&f&le&b&la Box&f&l ***"));
        ArrayList<String> seaBoxLore = new ArrayList<String>();
        seaBoxLore.add(ColorUtils.format("&8&o( Right click to open )"));
        seaBoxLore.add(ColorUtils.format(""));
        seaBoxLore.add(ColorUtils.format("&b&lInside:"));
        seaBoxLore.add(ColorUtils.format("&f&l- &b$50,000"));
        seaBoxLore.add(ColorUtils.format("&f&l- &f1x &b&lSea Weapon"));
        seaBoxMeta.setLore(seaBoxLore);
        seaBox.setItemMeta(seaBoxMeta);

        ItemStack frostBoots = new ItemStack(Material.IRON_BOOTS);
        ItemStack frostLeggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack frostChestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack frostHelmet = new ItemStack(Material.IRON_HELMET);
        ItemMeta frostBootsMeta = frostBoots.getItemMeta();
        ItemMeta frostLeggingsMeta = frostLeggings.getItemMeta();
        ItemMeta frostChestplateMeta = frostChestplate.getItemMeta();
        ItemMeta frostHelmetMeta = frostHelmet.getItemMeta();
        frostBootsMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lFrosty&b&l Boots &c&l&k..."));
        frostLeggingsMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lFrosty&b&l Leggings &c&l&k..."));
        frostChestplateMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lFrosty&b&l Chestplate &c&l&k..."));
        frostHelmetMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lFrosty&b&l Helmet &c&l&k..."));
        ArrayList<String> frostlore = new ArrayList<>();
        frostlore.add("");
        frostlore.add(ColorUtils.format("&7&oFound in the deep depths of the &fIce Plains&7&o!"));
        frostBootsMeta.setLore(frostlore);
        frostLeggingsMeta.setLore(frostlore);
        frostChestplateMeta.setLore(frostlore);
        frostHelmetMeta.setLore(frostlore);
        frostBoots.setItemMeta(frostBootsMeta);
        frostLeggings.setItemMeta(frostLeggingsMeta);
        frostChestplate.setItemMeta(frostChestplateMeta);
        frostHelmet.setItemMeta(frostHelmetMeta);
        frostBoots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        frostBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        frostLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        frostLeggings.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        frostChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        frostChestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        frostHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        frostHelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 4);

        ItemStack stdIssueBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack stdIssueLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack stdIssueChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack stdIssueHelmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta stdIssueBootsMeta = stdIssueBoots.getItemMeta();
        ItemMeta stdIssueLeggingsMeta = stdIssueLeggings.getItemMeta();
        ItemMeta stdIssueChestplateMeta = stdIssueChestplate.getItemMeta();
        ItemMeta stdIssueHelmetMeta = stdIssueHelmet.getItemMeta();
        stdIssueBootsMeta.setDisplayName(ColorUtils.format("&bStandard Issue Boots &f( MK.1 )"));
        stdIssueLeggingsMeta.setDisplayName(ColorUtils.format("&bStandard Issue Leggings &f( MK.1 )"));
        stdIssueChestplateMeta.setDisplayName(ColorUtils.format("&bStandard Issue Chestplate &f( MK.1 )"));
        stdIssueHelmetMeta.setDisplayName(ColorUtils.format("&bStandard Issue Helmet &f( MK.1 )"));
        ArrayList<String> stdIssuelore = new ArrayList<>();
        stdIssuelore.add("");
        stdIssuelore.add(ColorUtils.format("&7&oThe standard kit for &bCombat Exercises&7&o!"));
        stdIssueBootsMeta.setLore(stdIssuelore);
        stdIssueLeggingsMeta.setLore(stdIssuelore);
        stdIssueChestplateMeta.setLore(stdIssuelore);
        stdIssueHelmetMeta.setLore(stdIssuelore);
        stdIssueBoots.setItemMeta(stdIssueBootsMeta);
        stdIssueLeggings.setItemMeta(stdIssueLeggingsMeta);
        stdIssueChestplate.setItemMeta(stdIssueChestplateMeta);
        stdIssueHelmet.setItemMeta(stdIssueHelmetMeta);
        stdIssueBoots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        stdIssueBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        stdIssueLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        stdIssueLeggings.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        stdIssueChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        stdIssueChestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        stdIssueHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        stdIssueHelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

        ItemStack stdSword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemStack stdAxe = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemStack stdShovel = new ItemStack(Material.DIAMOND_SPADE, 1);
        ItemStack stdPickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta stdSwordMeta = stdSword.getItemMeta();
        ItemMeta stdAxeMeta = stdAxe.getItemMeta();
        ItemMeta stdShovelMeta = stdShovel.getItemMeta();
        ItemMeta stdPickaxeMeta = stdPickaxe.getItemMeta();
        stdSwordMeta.setDisplayName(ColorUtils.format("&bStandard Issue Sword &f( MK.1 )"));
        stdAxeMeta.setDisplayName(ColorUtils.format("&bStandard Issue Axe &f( MK.1 )"));
        stdShovelMeta.setDisplayName(ColorUtils.format("&bStandard Issue Shovel &f( MK.1 )"));
        stdPickaxeMeta.setDisplayName(ColorUtils.format("&bStandard Issue Pickaxe &f( MK.1 )"));
        stdSwordMeta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        stdAxeMeta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        stdShovelMeta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        stdPickaxeMeta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        stdSwordMeta.spigot().setUnbreakable(true);
        stdAxeMeta.spigot().setUnbreakable(true);
        stdShovelMeta.spigot().setUnbreakable(true);
        stdPickaxeMeta.spigot().setUnbreakable(true);
        stdSword.setItemMeta(stdSwordMeta);
        stdAxe.setItemMeta(stdAxeMeta);
        stdShovel.setItemMeta(stdShovelMeta);
        stdPickaxe.setItemMeta(stdPickaxeMeta);
        stdSword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        stdSword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
        stdAxe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        stdAxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
        stdShovel.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
        stdPickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
        stdPickaxe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);

        ItemStack airDropBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack airDropLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack airDropChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack airDropHelmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta airDropBootsMeta = airDropBoots.getItemMeta();
        ItemMeta airDropLeggingsMeta = airDropLeggings.getItemMeta();
        ItemMeta airDropChestplateMeta = airDropChestplate.getItemMeta();
        ItemMeta airDropHelmetMeta = airDropHelmet.getItemMeta();
        airDropBootsMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lAirDrop&b&l Boots &c&l&k..."));
        airDropLeggingsMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lAirDrop&b&l Leggings &c&l&k..."));
        airDropChestplateMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lAirDrop&b&l Chestplate &c&l&k..."));
        airDropHelmetMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lAirDrop&b&l Helmet &c&l&k..."));
        ArrayList<String> airDroplore = new ArrayList<>();
        airDroplore.add("");
        airDroplore.add(ColorUtils.format("&7&oDropped by the &bVortech Organisation&7&o to supply your battles!"));
        airDropBootsMeta.setLore(airDroplore);
        airDropLeggingsMeta.setLore(airDroplore);
        airDropChestplateMeta.setLore(airDroplore);
        airDropHelmetMeta.setLore(airDroplore);
        airDropBoots.setItemMeta(airDropBootsMeta);
        airDropLeggings.setItemMeta(airDropLeggingsMeta);
        airDropChestplate.setItemMeta(airDropChestplateMeta);
        airDropHelmet.setItemMeta(airDropHelmetMeta);
        airDropBoots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        airDropBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        airDropLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        airDropLeggings.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        airDropChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        airDropChestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        airDropHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        airDropHelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

        ItemStack airDropLegendaryBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack airDropLegendaryLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack airDropLegendaryChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack airDropLegendaryHelmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta airDropLegendaryBootsMeta = airDropLegendaryBoots.getItemMeta();
        ItemMeta airDropLegendaryLeggingsMeta = airDropLegendaryLeggings.getItemMeta();
        ItemMeta airDropLegendaryChestplateMeta = airDropLegendaryChestplate.getItemMeta();
        ItemMeta airDropLegendaryHelmetMeta = airDropLegendaryHelmet.getItemMeta();
        airDropLegendaryBootsMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lLegendary AirDrop&b&l Boots &c&l&k..."));
        airDropLegendaryLeggingsMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lLegendary AirDrop&b&l Leggings &c&l&k..."));
        airDropLegendaryChestplateMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lLegendary AirDrop&b&l Chestplate &c&l&k..."));
        airDropLegendaryHelmetMeta.setDisplayName(ColorUtils.format("&c&l&k...&r &f&lLegendary AirDrop&b&l Helmet &c&l&k..."));
        ArrayList<String> airDropLegendarylore = new ArrayList<>();
        airDropLegendarylore.add("");
        airDropLegendarylore.add(ColorUtils.format("&7&oDropped by the &bVortech Organisation&7&o to supply your battles!"));
        airDropLegendaryBootsMeta.setLore(airDropLegendarylore);
        airDropLegendaryLeggingsMeta.setLore(airDropLegendarylore);
        airDropLegendaryChestplateMeta.setLore(airDropLegendarylore);
        airDropLegendaryHelmetMeta.setLore(airDropLegendarylore);
        airDropLegendaryBoots.setItemMeta(airDropLegendaryBootsMeta);
        airDropLegendaryLeggings.setItemMeta(airDropLegendaryLeggingsMeta);
        airDropLegendaryChestplate.setItemMeta(airDropLegendaryChestplateMeta);
        airDropLegendaryHelmet.setItemMeta(airDropLegendaryHelmetMeta);
        airDropLegendaryBoots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        airDropLegendaryBoots.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        airDropLegendaryLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        airDropLegendaryLeggings.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        airDropLegendaryChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        airDropLegendaryChestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        airDropLegendaryHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        airDropLegendaryHelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 4);


        ItemStack frostKatana = new ItemStack(Material.IRON_SWORD);
        ItemMeta frostKatanaMeta = frostKatana.getItemMeta();
        frostKatanaMeta.setDisplayName(ColorUtils.format("&d&k. &f&lFrosty &b&lKatana&d&k ."));
        frostKatanaMeta.setLore(frostlore);
        frostKatana.setItemMeta(frostKatanaMeta);
        frostKatana.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        frostKatana.addUnsafeEnchantment(Enchantment.DURABILITY, 5);

        ItemStack frostAxe = new ItemStack(Material.IRON_AXE);
        ItemMeta frostAxeMeta = frostAxe.getItemMeta();
        frostAxeMeta.setDisplayName(ColorUtils.format("&d&k. &f&lFrosty &b&lAxe&d&k ."));
        frostAxeMeta.setLore(frostlore);
        frostAxe.setItemMeta(frostAxeMeta);
        frostAxe.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        frostAxe.addUnsafeEnchantment(Enchantment.DURABILITY, 5);

        /**
        ItemStack deadlyDebuff = new ItemStack(Material.POTION);
        PotionMeta deadlyDebuffMeta = (PotionMeta) deadlyDebuff.getItemMeta();
        deadlyDebuffMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0), false);
        deadlyDebuffMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1), false);
        deadlyDebuffMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 300, 1), false);
        deadlyDebuffMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 4), false);
        deadlyDebuffMeta.setDisplayName(ColorUtils.format("&bDeadly Debuff &f(Throw)"));
        ArrayList<String> deadlydebufflore = new ArrayList<>();
        deadlydebufflore.add(ColorUtils.format(""));
        deadlydebufflore.add(ColorUtils.format("&fEffects:"));
        deadlydebufflore.add(ColorUtils.format("&b - Blindness I &f( 00:15 )"));
        deadlydebufflore.add(ColorUtils.format("&b - Weakness II &f( 00:15 )"));
        deadlydebufflore.add(ColorUtils.format("&b - Slowness II &f( 00:15 )"));
        deadlydebufflore.add(ColorUtils.format("&b - Nausea V &f( 00:15 )"));
        deadlyDebuffMeta.setLore(deadlydebufflore);
        deadlyDebuffMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        Potion deadlyDebuffPot = new Potion(PotionType.WATER);

        deadlyDebuffPot.setSplash(true);
        deadlyDebuffPot.apply(deadlyDebuff);
        deadlyDebuff.setItemMeta(deadlyDebuffMeta);
         */

        ItemStack godlyElixir = new ItemStack(Material.POTION);
        PotionMeta godlyElixirMeta = (PotionMeta) godlyElixir.getItemMeta();
        godlyElixirMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 2), false);
        godlyElixirMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 1), false);
        godlyElixirMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 3600, 2), false);
        godlyElixirMeta.setDisplayName(ColorUtils.format("&bGodly Elixir &f(Drink)"));
        ArrayList<String> godlyElixirlore = new ArrayList<>();
        godlyElixirlore.add(ColorUtils.format(""));
        godlyElixirlore.add(ColorUtils.format("&fEffects:"));
        godlyElixirlore.add(ColorUtils.format("&b - Speed III &f( 03:00 )"));
        godlyElixirlore.add(ColorUtils.format("&b - Strength II &f( 03:00 )"));
        godlyElixirlore.add(ColorUtils.format("&b - Jump Boost III &f( 03:00 )"));
        godlyElixirMeta.setLore(godlyElixirlore);
        godlyElixirMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        godlyElixir.setItemMeta(godlyElixirMeta);

        ItemStack depthStrider3 = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta depthStrider3Meta = depthStrider3.getItemMeta();
        depthStrider3Meta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
        depthStrider3.setItemMeta(depthStrider3Meta);

        ItemStack squidDrop = new ItemStack(Material.INK_SACK, 1);
        ItemMeta squidDropMeta = squidDrop.getItemMeta();
        squidDropMeta.setDisplayName(ColorUtils.format("&bSquid Essence"));
        squidDropMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        squidDropMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        squidDrop.setItemMeta(squidDropMeta);

        ItemStack guardianDrop = new ItemStack(Material.PRISMARINE_SHARD, 1);
        ItemMeta guardianDropMeta = guardianDrop.getItemMeta();
        guardianDropMeta.setDisplayName(ColorUtils.format("&bGuardian Essence"));
        guardianDropMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        guardianDropMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        guardianDrop.setItemMeta(guardianDropMeta);

        ItemStack rabbitDrop = new ItemStack(Material.TNT, 1);
        ItemMeta rabbitDropMeta = rabbitDrop.getItemMeta();
        rabbitDropMeta.setDisplayName(ColorUtils.format("&bRabbit TNT"));
        rabbitDropMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        rabbitDropMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        rabbitDrop.setItemMeta(rabbitDropMeta);

        ItemStack leaderBundle = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
        ItemMeta leaderBundleMeta = leaderBundle.getItemMeta();
        leaderBundleMeta.setDisplayName(ColorUtils.format("&d&l&k.... &fLeader Bundle &d&l&k.... "));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorUtils.format(""));
        lore.add(ColorUtils.format("&bInside:"));
        lore.add(ColorUtils.format("&f- &a$500,000"));
        lore.add(ColorUtils.format("&f- &f16x&b Chunk Buster"));
        lore.add(ColorUtils.format("&f- &f2x&b Standard Kit"));
        lore.add(ColorUtils.format(""));
        lore.add(ColorUtils.format("&7&l&oPlace to open"));
        leaderBundleMeta.setLore(lore);
        leaderBundleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        leaderBundleMeta.addEnchant(Enchantment.DURABILITY, 1, false);
        leaderBundle.setItemMeta(leaderBundleMeta);

        ItemStack seaWeapon = new ItemStack(Material.PRISMARINE_SHARD, 1);
        ItemMeta seaWeaponMeta = seaWeapon.getItemMeta();
        seaWeaponMeta.setDisplayName(ColorUtils.format("&b&lS&f&le&b&la Weapon &f(MK.1)"));
        ArrayList seaWeaponLore = new ArrayList();
        seaWeaponLore.add(ColorUtils.format("&bRecovered from the &fDepths of the Ocean&b by"));
        seaWeaponLore.add(ColorUtils.format("&bVortech Organisation!"));
        seaWeaponMeta.setLore(seaWeaponLore);
        seaWeapon.setItemMeta(seaWeaponMeta);
        seaWeapon.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        seaWeapon.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);

        ArrayList<ItemStack> stdKitList = new ArrayList();
        stdKitList.add(stdIssueBoots);
        stdKitList.add(stdIssueLeggings);
        stdKitList.add(stdIssueChestplate);
        stdKitList.add(stdIssueHelmet);
        stdKitList.add(stdSword);
        stdKitList.add(stdAxe);
        stdKitList.add(stdShovel);
        stdKitList.add(stdPickaxe);

        ArrayList<ItemStack> frostyKit = new ArrayList<>();

        ItemStack stdKit = new ItemStack(Material.CHEST);
        ItemMeta stdKitMeta = stdKit.getItemMeta();
        stdKitMeta.setDisplayName(ColorUtils.format("&bStandard Issue Kit"));
        ArrayList<String> stdKitLore = new ArrayList<>();
        stdKitLore.add(ColorUtils.format("&bVersion: &f1.0"));
        stdKitLore.add(ColorUtils.format("&bSupplied By:&f Vortech"));
        stdKitLore.add(ColorUtils.format("&bInside:"));
        for(ItemStack t : stdKitList){
            stdKitLore.add(ColorUtils.format("&b- &f1x "+t.getItemMeta().getDisplayName()));
        }
        stdKitLore.add(ColorUtils.format(""));
        stdKitLore.add(ColorUtils.format("&bRight Click to open"));
        stdKitMeta.setLore(stdKitLore);
        stdKit.setItemMeta(stdKitMeta);


        ItemStack rocketLauncher = new ItemStack(Material.ENDER_PEARL);
        ItemMeta rocketLauncherMeta = rocketLauncher.getItemMeta();
        rocketLauncherMeta.setDisplayName(ColorUtils.format("&cRocket Launcher"));
        rocketLauncher.setItemMeta(rocketLauncherMeta);

        ItemStack magicane = new ItemBuilder(Material.SUGAR_CANE)
                .setName(ColorUtils.format("&d&lMagicane"))
                .addUnsafeEnchantment(Enchantment.KNOCKBACK, 5)
                .toItemStack();

        ItemStack stackKiller = new ItemBuilder(Material.BLAZE_ROD)
                .setName(ColorUtils.format("&bStack Killer &7(Kill)"))
                .addLoreLine(ColorUtils.format("&7Stack Kill"))
                .toItemStack();

        kits.put("standard", stdKitList);

        randomItemTypes.put("frost", RandomItemType.FROST);
        randomItemTypes.put("airdropnormal", RandomItemType.AIRDROPNORMAL);
        randomItemTypes.put("airdroplegendary", RandomItemType.AIRDROPLEGENDARY);

        /**
         * Change metaSellValues to metametaSellValues
         * add itemmetaSellValues
         * checks if in itemmetaSellValues first, if not checks metametaSellValues.
         */

        //metaSellValues.put(new ItemStack(Material.SUGAR_CANE).getItemMeta(), 40);
        metaSellValues.put(squidDropMeta, 4);
        metaSellValues.put(guardianDropMeta, 80);
        //metaSellValues.put(rabbitDropMeta, 2500);

        items.put("stackkiller", stackKiller);
        items.put("magicane", magicane);
        items.put("chunkbuster", chunkBuster);
        items.put("rocketlauncher", rocketLauncher);
        items.put("seaweapon", seaWeapon);
        items.put("standardkit", stdKit);
        items.put("standardsword", stdSword);
        items.put("standardaxe", stdAxe);
        items.put("standardshovel", stdShovel);
        items.put("standardpickaxe", stdPickaxe);
        items.put("standardboots", stdIssueBoots);
        items.put("standardleggings", stdIssueLeggings);
        items.put("standardchestplate", stdIssueChestplate);
        items.put("standardhelmet", stdIssueHelmet);
        items.put("leaderbundle", leaderBundle);
        items.put("airdropbootslegendary", airDropLegendaryBoots);
        items.put("airdropleggingslegendary", airDropLegendaryLeggings);
        items.put("airdropchestplatelegendary", airDropLegendaryChestplate);
        items.put("airdrophelmetlegendary", airDropLegendaryHelmet);
        items.put("airdropbootsnormal", airDropBoots);
        items.put("airdropleggingsnormal", airDropLeggings);
        items.put("airdropchestplatenormal", airDropChestplate);
        items.put("airdrophelmetnormal", airDropHelmet);
        items.put("rabbitdrop", rabbitDrop);
        items.put("guardiandrop", guardianDrop);
        items.put("squiddrop", squidDrop);
        items.put("icebox", iceBox);
        items.put("seabox", seaBox);
        items.put("frostyboots", frostBoots);
        items.put("frostyleggings", frostLeggings);
        items.put("frostychestplate", frostChestplate);
        items.put("frostyhelmet", frostHelmet);
        items.put("frostykatana", frostKatana);
        items.put("frostyaxe", frostAxe);
        //items.put("mysterymask", mysteryMask);
        //items.put("deadlydebuff", deadlyDebuff);
        items.put("godlyelixir", godlyElixir);
        items.put("depthstrider3", depthStrider3);
    }

    public static ItemStack random(RandomItemType type){
        Random r = new Random();
        if(type.equals(RandomItemType.AIRDROPNORMAL)){

            ArrayList<ItemStack> options = new ArrayList();
            options.add(items.get("airdropbootsnormal"));
            options.add(items.get("airdropleggingsnormal"));
            options.add(items.get("airdropchestplatenormal"));
            options.add(items.get("airdrophelmetnormal"));
            return options.get(r.nextInt(4));
        }
        if(type.equals(RandomItemType.AIRDROPLEGENDARY)){

            ArrayList<ItemStack> options = new ArrayList();
            options.add(items.get("airdropbootslegendary"));
            options.add(items.get("airdropleggingslegendary"));
            options.add(items.get("airdropchestplatelegendary"));
            options.add(items.get("airdrophelmetlegendary"));
            return options.get(r.nextInt(4));
        }
        if(type.equals(RandomItemType.FROST)){
            ArrayList<ItemStack> options = new ArrayList();
            options.add(items.get("frostyboots"));
            options.add(items.get("frostyleggings"));
            options.add(items.get("frostychestplate"));
            options.add(items.get("frostyhelmet"));
            options.add(items.get("frostykatana"));
            options.add(items.get("frostyaxe"));
            return options.get(r.nextInt(6));

        }
        return null;
    }
}
