package dev.authorises.cavelet.customitems;

import dev.authorises.cavelet.Cavelet;
import dev.authorises.cavelet.customenchants.CustomEnchantType;
import dev.authorises.cavelet.mining.Ore;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class RegisterBasicCustomItems {

    public CItemBlueprint<BasicCustomItem> generateCItem(BasicCustomItem i){
        CItemBlueprint<BasicCustomItem> cItemBlueprint = new CItemBlueprint<>();
        cItemBlueprint.setItem(i);
        cItemBlueprint.setRarity(i.getRarity());
        cItemBlueprint.setId(i.getId());
        cItemBlueprint.setAllowedEnchantments(List.of(i.possibleEnchants));
        cItemBlueprint.setEnchantable(i.enchantable);
        cItemBlueprint.setStackable(i.stackable);
        cItemBlueprint.setPointsValue(i.pointsValue);
        if(i.possibleForges!=null) cItemBlueprint.setAllowedForgeUpgrades(List.of(i.possibleForges));
        return cItemBlueprint;
    }

    public CItemBlueprint<ArmourCustomItem> generateCItem(ArmourCustomItem i){
        CItemBlueprint<ArmourCustomItem> cItemBlueprint = new CItemBlueprint<>();
        cItemBlueprint.setItem(i);
        cItemBlueprint.setRarity(i.getRarity());
        cItemBlueprint.setId(i.getId());
        cItemBlueprint.setPointsValue(i.pointsValue);
        ArrayList<CustomEnchantType> enchants = new ArrayList<>(Cavelet.customEnchantLists.armourEnchantments);
        if (i.enchantable) {
            for (CustomEnchantType type : i.allowedEnchantTypes) {
                if (!enchants.contains(type)) {
                    enchants.add(type);
                }
            }
            for (CustomEnchantType type : i.getType().allowedEnchantments) {
                if (!enchants.contains(type)) {
                    enchants.add(type);
                }
            }
        }
        cItemBlueprint.setEnchantable(i.enchantable);
        cItemBlueprint.setStackable(i.stackable);
        Bukkit.getLogger().info(enchants.toString());
        enchants.addAll(List.of(i.allowedEnchantTypes));
        cItemBlueprint.setAllowedEnchantments(enchants);

        if(i.possibleForges!=null) cItemBlueprint.setAllowedForgeUpgrades(List.of(i.possibleForges));
        return cItemBlueprint;
    }

    public RegisterBasicCustomItems(){
        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.DIVINE,
                "TEST_ITEM",
                "Test Item",
                Material.FLOWERING_AZALEA,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer orci risus, scelerisque et dictum in, aliquam convallis est. Fusce aliquam nibh et tellus sollicitudin, non pharetra magna aliquet. Nunc nec felis leo. Suspendisse porta, lacus sit amet fringilla auctor, turpis nulla euismod metus, eu hendrerit est ligula sit amet nisi. Aenean eleifend nibh nec elit varius, ac porttitor nisl bibendum. Curabitur varius ex ex, a sagittis enim condimentum vitae. Morbi ac bibendum ipsum, vitae pulvinar orci. Curabitur eu erat ullamcorper, sodales nisi sed, consectetur augue. Aliquam nisl sem, placerat non dapibus nec, pulvinar aliquam massa. Praesent quis vehicula arcu. Fusce sed sapien scelerisque, sollicitudin magna ac, egestas dui. Duis lacus odio, pellentesque lacinia augue et, lobortis fringilla erat. Aliquam tincidunt sem sit amet lectus semper tempor. Sed sed ligula urna. Suspendisse tempor quis mauris ac ultrices. Proin bibendum ante a sapien porttitor mollis. Maecenas vitae lacus vel massa posuere malesuada. ",
                false,
                true)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "RAW_BEEF",
                "Raw Beef",
                Material.BEEF,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.RARE,
                "SHEET_OF_PAPER",
                "Sheet of paper",
                Material.PAPER,
                "Who would think that a sheet of paper can control the world?",
                false,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.RARE,
                "VILLAGERS_TALE",
                "Villager's tale",
                Material.BOOK,
                "Children have been enlightened by the tale for hundreds of years",
                false,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.DIVINE,
                "WISHBONE",
                "Wishbone",
                Material.BONE,
                "What's your wish?",
                false,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "BOWL",
                "Bowl",
                Material.BOWL,
                "Ready to hold a tasty meal.",
                false,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "SEEDY",
                "Seedy",
                Material.SUNFLOWER,
                "Old tech",
                false,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.DIVINE,
                "WISHBONE",
                "Wishbone",
                Material.BONE,
                "What's your wish?",
                false,
                true).setPossibleEnchants(CustomEnchantType.SHARPNESS)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "RAW_PORKCHOP",
                "Raw Porkchop",
                Material.PORKCHOP,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "COOKED_PORKCHOP",
                "Cooked Porkchop",
                Material.COOKED_PORKCHOP,
                "Dropped from the pigs at spawn",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "SHEEP_ESSENCE",
                "Sheep Essence",
                Material.MUTTON,
                "Sellable at spawn",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "SKELETAL_ESSENCE",
                "Skeletal Essence",
                Material.BONE_MEAL,
                "Sellable at spawn",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "VINDICATOR_ESSENCE",
                "Vindicator Essence",
                Material.PAPER,
                "Sellable at spawn",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "DUST",
                "Dust",
                Material.GUNPOWDER,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.RARE,
                "GOLDEN_POTATO_SEED",
                "Golden Potato",
                Material.POTATO,
                "This Golden Potato can be planted and can grow into many more.",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "GOLDEN_POTATO",
                "Golden Potato",
                Material.GOLD_NUGGET,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "STONE",
                "Stone",
                Material.STONE,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "COBBLESTONE",
                "Cobblestone",
                Material.COBBLESTONE,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "MUD",
                "Mud",
                Material.MUD,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "GRAVEL",
                "Gravel",
                Material.GRAVEL,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "CLAY",
                "Clay",
                Material.CLAY,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "DIRT",
                "Dirt",
                Material.DIRT,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "SAND",
                "Sand",
                Material.SAND,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "WHEAT",
                "Wheat",
                Material.WHEAT,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "SEEDS",
                "Seeds",
                Material.WHEAT_SEEDS,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "DIAMOND_HOE",
                "Diamond Hoe",
                Material.DIAMOND_HOE,
                "",
                false,
                true)));

        BasicCustomItem DIAMOND_PICKAXE = new BasicCustomItem(
                Rarity.UNCOMMON,
                "DIAMOND_PICKAXE",
                "Diamond Pickaxe",
                Material.DIAMOND_PICKAXE,
                "A high quality Diamond Pickaxe, sold to you fresh from the forge!",
                false,
                true,
                CustomEnchantType.MENDING,
                CustomEnchantType.EFFICIENCY);

        DIAMOND_PICKAXE.setPossibleForges("EXPERIBOOST");

        Cavelet.customItemsManager.addCustomItem(generateCItem(DIAMOND_PICKAXE));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "DIAMOND_AXE",
                "Diamond Axe",
                Material.DIAMOND_AXE,
                "A high quality Diamond Axe, sold to you fresh from the forge!",
                false,
                true,
                CustomEnchantType.SHARPNESS,
                CustomEnchantType.MENDING,
                CustomEnchantType.EFFICIENCY)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "DIAMOND_HOE",
                "Diamond Hoe",
                Material.DIAMOND_HOE,
                "A high quality Diamond Hoe, sold to you fresh from the forge!",
                false,
                true,
                CustomEnchantType.MENDING)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "DIAMOND_SHOVEL",
                "Diamond Shovel",
                Material.DIAMOND_SHOVEL,
                "A high quality Diamond Shovel, sold to you fresh from the forge!",
                false,
                true,
                CustomEnchantType.MENDING,
                CustomEnchantType.EFFICIENCY)));



        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "DIAMOND_SWORD",
                "Diamond Sword",
                Material.DIAMOND_SWORD,
                "A high quality Diamond Sword, sold to you fresh from the forge!",
                false,
                true,
                CustomEnchantType.MENDING,
                CustomEnchantType.SHARPNESS,
                CustomEnchantType.SKELESHARPNESS,
                CustomEnchantType.FIRE_ASPECT
        )));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "CTHULU_KATANA",
                "Cthulu Katana",
                Material.NETHERITE_SWORD,
                "A high quality Diamond Sword, sold to you fresh from the forge!",
                false,
                true,
                CustomEnchantType.MENDING,
                CustomEnchantType.SHARPNESS,
                CustomEnchantType.SKELESHARPNESS,
                CustomEnchantType.FIRE_ASPECT
        )));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "GOLDEN_APPLE",
                "Golden Apple",
                Material.GOLDEN_APPLE,
                "Freshly cooked, this Golden Apple will grant you <#d9c636>❤❤",
                false,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.DIVINE,
                "ENCHANTED_GOLDEN_APPLE",
                "Enchanted Golden Apple",
                Material.ENCHANTED_GOLDEN_APPLE,
                "Eat to instantly gain these effects: Absorption IV (2:00), Regeneration II (0:20)<#9eb5db>, Fire Resistance (5:00), Resistance (5:00)",
                false,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.COMMON,
                "BREAD",
                "Bread",
                Material.BREAD,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                Rarity.UNCOMMON,
                "COBWEB",
                "Cobweb",
                Material.COBWEB,
                "",
                true,
                false)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.RARE,
                "CTHULU_BOOTS",
                "Cthulu Boots",
                "#e01d4b",
                "Forged deep in the depths of Cthulu's lair, these rare boots provide the ultimate combat experience",
                8D,
                8D,
                200D,
                false,
                ArmourType.BOOTS,
                true, 0).setPossibleForges("VACCINE")));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.RARE,
                "CTHULU_LEGGINGS",
                "Cthulu Leggings",
                "#e01d4b",
                "Forged deep in the depths of Cthulu's lair, these rare leggings provide the ultimate combat experience",
                13D,
                13D,
                200D,
                false,
                ArmourType.LEGGINGS,
                true, 0).setPossibleForges("WARP")));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.RARE,
                "CTHULU_CHESTPLATE",
                "Cthulu Chestplate",
                "#e01d4b",
                "Forged deep in the depths of Cthulu's lair, this rare chestplate provides the ultimate combat experience",
                15D,
                15D,
                200D,
                false,
                ArmourType.CHESTPLATE,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.RARE,
                "CTHULU_HELMET",
                "Cthulu Helmet",
                "#e01d4b",
                "Forged deep in the depths of Cthulu's lair, this rare helmet provides the ultimate combat experience",
                7D,
                7D,
                100D,
                false,
                ArmourType.HELMET,
                true, 0)));



        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.COMMON,
                "MINER_BOOTS",
                "Miner Boots",
                "#373561",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                3D,
                3D,
                100D,
                false,
                ArmourType.BOOTS,
                true, 0)));



        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.COMMON,
                "MINER_LEGGINGS",
                "Miner Leggings",
                "#373561",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                5.5D,
                5.5D,
                100D,
                false,
                ArmourType.LEGGINGS,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.COMMON,
                "MINER_CHESTPLATE",
                "Miner Chestplate",
                "#373561",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                6.5D,
                6.5D,
                100D,
                false,
                ArmourType.CHESTPLATE,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.COMMON,
                "MINER_HELMET",
                "Miner Helmet",
                "#373561",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                2.5D,
                2.5D,
                100D,
                false,
                ArmourType.HELMET,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.UNCOMMON,
                "VERDANT_BOOTS",
                "Verdant Boots",
                "#3D9970",
                "One of the stronger armour sets you can get your hands on, can help protect you from even the most dangerous creatures.",
                4.5D,
                4.5D,
                150D,
                false,
                ArmourType.BOOTS,
                true, 0)));



        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.UNCOMMON,
                "VERDANT_LEGGINGS",
                "Verdant Leggings",
                "#3D9970",
                "One of the stronger armour sets you can get your hands on, can help protect you from even the most dangerous creatures.",
                8.25D,
                8.25D,
                150D,
                false,
                ArmourType.LEGGINGS,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.UNCOMMON,
                "VERDANT_CHESTPLATE",
                "Verdant Chestplate",
                "#3D9970",
                "One of the stronger armour sets you can get your hands on, can help protect you from even the most dangerous creatures.",
                9.75D,
                9.75D,
                150D,
                false,
                ArmourType.CHESTPLATE,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.UNCOMMON,
                "VERDANT_HELMET",
                "Verdant Helmet",
                "#3D9970",
                "One of the stronger armour sets you can get your hands on, can help protect you from even the most dangerous creatures.",
                3.75D,
                3.75D,
                150D,
                false,
                ArmourType.HELMET,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.DIVINE,
                "AUTH_BOOTS",
                "Auth Boots",
                "#dba9da",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                300D,
                300D,
                1000D,
                false,
                ArmourType.BOOTS,
                true,
                1,
                CustomEnchantType.DEPTH_STRIDER)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.DIVINE,
                "AUTH_LEGGINGS",
                "Auth Leggings",
                "#dba9da",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                500.5D,
                500.5D,
                1000D,
                false,
                ArmourType.LEGGINGS,
                true, 4).setPossibleForges("WARP")));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.DIVINE,
                "AUTH_CHESTPLATE",
                "Auth Chestplate",
                "#dba9da",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                600.5D,
                600.5D,
                1000D,
                false,
                ArmourType.CHESTPLATE,
                true, 0)));

        Cavelet.customItemsManager.addCustomItem(generateCItem(new ArmourCustomItem(
                Rarity.DIVINE,
                "AUTH_HELMET",
                "Auth Helmet",
                "#dba9da",
                "This Miner Equipment is for anybody inside of the Cave System to protect them from creatures inside",
                200.5D,
                200.5D,
                1000D,
                false,
                ArmourType.HELMET,
                true,
                0, CustomEnchantType.AQUA_AFFINITY,
                CustomEnchantType.RESPIRATION)));

        for(Ore o : Cavelet.oreManager.getOres()){
            Rarity rarity = Rarity.COMMON;
            switch (o.getRarity()){
                case DEFAULT -> {
                    rarity = Rarity.COMMON;
                }
                case PROCESSED -> {
                    rarity = Rarity.UNCOMMON;
                }
                case PURE -> {
                    rarity = Rarity.RARE;
                }
            }
            Cavelet.customItemsManager.addCustomItem(generateCItem(new BasicCustomItem(
                    rarity,
                    o.getOreId().toUpperCase(),
                    o.getDisplayName().replaceAll("\\&.", ""),
                    o.getItemMaterial(),
                    "",
                    true,
                    false)));
        }
    }
}
