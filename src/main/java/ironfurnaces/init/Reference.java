package ironfurnaces.init;

import ironfurnaces.blocks.*;
import ironfurnaces.config.Configuration;
import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.*;
import ironfurnaces.items.*;
import ironfurnaces.tileentity.*;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.network.chat.Component;

public class Reference {

    public static final String MOD_ID = "ironfurnaces";



    public static final BlockIronFurnace IRON_FURNACE = new BlockIronFurnace();
    public static BlockEntityType<BlockIronFurnaceTile> IRON_FURNACE_TILE;
    public static MenuType<BlockIronFurnaceScreenHandler> IRON_FURNACE_SCREEN_HANDLER;

    public static final BlockGoldFurnace GOLD_FURNACE = new BlockGoldFurnace();
    public static BlockEntityType<BlockGoldFurnaceTile> GOLD_FURNACE_TILE;
    public static MenuType<BlockGoldFurnaceScreenHandler> GOLD_FURNACE_SCREEN_HANDLER;

    public static final BlockDiamondFurnace DIAMOND_FURNACE = new BlockDiamondFurnace();
    public static BlockEntityType<BlockDiamondFurnaceTile> DIAMOND_FURNACE_TILE;
    public static MenuType<BlockDiamondFurnaceScreenHandler> DIAMOND_FURNACE_SCREEN_HANDLER;

    public static final BlockEmeraldFurnace EMERALD_FURNACE = new BlockEmeraldFurnace();
    public static BlockEntityType<BlockEmeraldFurnaceTile> EMERALD_FURNACE_TILE;
    public static MenuType<BlockEmeraldFurnaceScreenHandler> EMERALD_FURNACE_SCREEN_HANDLER;

    public static final BlockObsidianFurnace OBSIDIAN_FURNACE = new BlockObsidianFurnace();
    public static BlockEntityType<BlockObsidianFurnaceTile> OBSIDIAN_FURNACE_TILE;
    public static MenuType<BlockObsidianFurnaceScreenHandler> OBSIDIAN_FURNACE_SCREEN_HANDLER;

    public static final BlockCrystalFurnace CRYSTAL_FURNACE = new BlockCrystalFurnace();
    public static BlockEntityType<BlockCrystalFurnaceTile> CRYSTAL_FURNACE_TILE;
    public static MenuType<BlockCrystalFurnaceScreenHandler> CRYSTAL_FURNACE_SCREEN_HANDLER;

    public static final BlockNetheriteFurnace NETHERITE_FURNACE = new BlockNetheriteFurnace();
    public static BlockEntityType<BlockNetheriteFurnaceTile> NETHERITE_FURNACE_TILE;
    public static MenuType<BlockNetheriteFurnaceScreenHandler> NETHERITE_FURNACE_SCREEN_HANDLER;

    public static final BlockCopperFurnace COPPER_FURNACE = new BlockCopperFurnace();
    public static BlockEntityType<BlockCopperFurnaceTile> COPPER_FURNACE_TILE;
    public static MenuType<BlockCopperFurnaceScreenHandler> COPPER_FURNACE_SCREEN_HANDLER;

    public static final BlockSilverFurnace SILVER_FURNACE = new BlockSilverFurnace();
    public static BlockEntityType<BlockSilverFurnaceTile> SILVER_FURNACE_TILE;
    public static MenuType<BlockSilverFurnaceScreenHandler> SILVER_FURNACE_SCREEN_HANDLER;

    public static final BlockWirelessHeater WIRELESS_HEATER = new BlockWirelessHeater();
    public static BlockEntityType<BlockWirelessHeaterTile> WIRELESS_HEATER_TILE;
    public static MenuType<BlockWirelessHeaterScreenHandler> WIRELESS_HEATER_SCREEN_HANDLER;


    public static final ItemHeater HEATER = new ItemHeater();

    public static final ItemAugmentBlasting BLASTING_AUGMENT = new ItemAugmentBlasting();
    public static final ItemAugmentSmoking SMOKING_AUGMENT = new ItemAugmentSmoking();
    public static final ItemAugmentSpeed SPEED_AUGMENT = new ItemAugmentSpeed();
    public static final ItemAugmentFuel FUEL_AUGMENT = new ItemAugmentFuel();

    public static final ItemSpooky SPOOKY = new ItemSpooky(new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.fromNamespaceAndPath(MOD_ID, "item_spooky"))));
    public static final ItemXmas XMAS = new ItemXmas(new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.fromNamespaceAndPath(MOD_ID, "item_xmas"))));
    public static final ItemFurnaceCopy COPY = new ItemFurnaceCopy();

    public static final CreativeModeTab itemGroup = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(MOD_ID, "general"),
            FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.ironfurnaces"))
                .icon(() -> new ItemStack(IRON_FURNACE))
                .displayItems((displayContext, entries) -> {
                    entries.accept(IRON_FURNACE);
                    entries.accept(GOLD_FURNACE);
                    entries.accept(DIAMOND_FURNACE);
                    entries.accept(EMERALD_FURNACE);
                    entries.accept(CRYSTAL_FURNACE);
                    entries.accept(OBSIDIAN_FURNACE);
                    entries.accept(NETHERITE_FURNACE);
                    entries.accept(COPPER_FURNACE);
                    entries.accept(SILVER_FURNACE);
                    ItemStack unchargedHeater = new ItemStack(WIRELESS_HEATER);
                    net.minecraft.world.item.component.CustomData.update(
                        net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                        unchargedHeater,
                        tag -> tag.putInt("energy", 0)
                    );
                    entries.accept(unchargedHeater);
                    ItemStack chargedHeater = new ItemStack(WIRELESS_HEATER);
                    net.minecraft.world.item.component.CustomData.update(
                        net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                        chargedHeater,
                        tag -> tag.putInt("energy", 100000)
                    );
                    entries.accept(chargedHeater);
                    entries.accept(HEATER);
                    entries.accept(SPOOKY);
                    entries.accept(XMAS);
                    entries.accept(COPY);
                    entries.accept(BLASTING_AUGMENT);
                    entries.accept(SMOKING_AUGMENT);
                    entries.accept(SPEED_AUGMENT);
                    entries.accept(FUEL_AUGMENT);
                })
                .build()
    );



    public static void init()
    {
        new Configuration(IronFurnacesConfig.class, MOD_ID);
        registration();

    }

    public static void registration() {

        registerBlock(BlockIronFurnace.IRON_FURNACE, IRON_FURNACE);
        IRON_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockIronFurnace.IRON_FURNACE), new BlockEntityType<>(BlockIronFurnaceTile::new, java.util.Set.of(IRON_FURNACE)));
        IRON_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockIronFurnace.IRON_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockIronFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockGoldFurnace.GOLD_FURNACE, GOLD_FURNACE);
        GOLD_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockGoldFurnace.GOLD_FURNACE), new BlockEntityType<>(BlockGoldFurnaceTile::new, java.util.Set.of(GOLD_FURNACE)));
        GOLD_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockGoldFurnace.GOLD_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockGoldFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockDiamondFurnace.DIAMOND_FURNACE, DIAMOND_FURNACE);
        DIAMOND_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockDiamondFurnace.DIAMOND_FURNACE), new BlockEntityType<>(BlockDiamondFurnaceTile::new, java.util.Set.of(DIAMOND_FURNACE)));
        DIAMOND_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockDiamondFurnace.DIAMOND_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockDiamondFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockEmeraldFurnace.EMERALD_FURNACE, EMERALD_FURNACE);
        EMERALD_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockEmeraldFurnace.EMERALD_FURNACE), new BlockEntityType<>(BlockEmeraldFurnaceTile::new, java.util.Set.of(EMERALD_FURNACE)));
        EMERALD_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockEmeraldFurnace.EMERALD_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockEmeraldFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockCrystalFurnace.CRYSTAL_FURNACE, CRYSTAL_FURNACE);
        CRYSTAL_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockCrystalFurnace.CRYSTAL_FURNACE), new BlockEntityType<>(BlockCrystalFurnaceTile::new, java.util.Set.of(CRYSTAL_FURNACE)));
        CRYSTAL_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockCrystalFurnace.CRYSTAL_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockCrystalFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockObsidianFurnace.OBSIDIAN_FURNACE, OBSIDIAN_FURNACE);
        OBSIDIAN_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockObsidianFurnace.OBSIDIAN_FURNACE), new BlockEntityType<>(BlockObsidianFurnaceTile::new, java.util.Set.of(OBSIDIAN_FURNACE)));
        OBSIDIAN_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockObsidianFurnace.OBSIDIAN_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockObsidianFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockNetheriteFurnace.NETHERITE_FURNACE, NETHERITE_FURNACE);
        NETHERITE_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockNetheriteFurnace.NETHERITE_FURNACE), new BlockEntityType<>(BlockNetheriteFurnaceTile::new, java.util.Set.of(NETHERITE_FURNACE)));
        NETHERITE_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockNetheriteFurnace.NETHERITE_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockNetheriteFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockCopperFurnace.COPPER_FURNACE, COPPER_FURNACE);
        COPPER_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockCopperFurnace.COPPER_FURNACE), new BlockEntityType<>(BlockCopperFurnaceTile::new, java.util.Set.of(COPPER_FURNACE)));
        COPPER_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockCopperFurnace.COPPER_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockCopperFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        registerBlock(BlockSilverFurnace.SILVER_FURNACE, SILVER_FURNACE);
        SILVER_FURNACE_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockSilverFurnace.SILVER_FURNACE), new BlockEntityType<>(BlockSilverFurnaceTile::new, java.util.Set.of(SILVER_FURNACE)));
        SILVER_FURNACE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockSilverFurnace.SILVER_FURNACE), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockSilverFurnaceScreenHandler::new, BlockPos.STREAM_CODEC));

        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockWirelessHeater.HEATER), WIRELESS_HEATER);
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockWirelessHeater.HEATER), new BlockItemHeater(WIRELESS_HEATER, new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockWirelessHeater.HEATER)))));
        WIRELESS_HEATER_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockWirelessHeater.HEATER), new BlockEntityType<>(BlockWirelessHeaterTile::new, java.util.Set.of(WIRELESS_HEATER)));
        WIRELESS_HEATER_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Reference.MOD_ID, BlockWirelessHeater.HEATER), new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockWirelessHeaterScreenHandler::new, BlockPos.STREAM_CODEC));

        registerItem("item_heater", HEATER);
        registerItem("item_spooky", SPOOKY);
        registerItem("item_xmas", XMAS);
        registerItem("item_copy", COPY);

        registerItem("augment_blasting", BLASTING_AUGMENT);
        registerItem("augment_smoking", SMOKING_AUGMENT);
        registerItem("augment_speed", SPEED_AUGMENT);
        registerItem("augment_fuel", FUEL_AUGMENT);

    }

    public static void registerBlock(String regName, Block block) {
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(Reference.MOD_ID, regName), block);
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Reference.MOD_ID, regName), new BlockItem(block, new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, regName)))));
    }
    public static void registerItem(String regName, Item item) {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Reference.MOD_ID, regName), item);
    }


}
