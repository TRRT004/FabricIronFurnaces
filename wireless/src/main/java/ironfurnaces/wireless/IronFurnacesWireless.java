package ironfurnaces.wireless;

import ironfurnaces.blocks.BlockItemHeater;
import ironfurnaces.blocks.BlockWirelessHeater;
import ironfurnaces.container.BlockWirelessHeaterScreenHandler;
import ironfurnaces.items.ItemHeater;
import ironfurnaces.tileentity.BlockWirelessHeaterTile;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.BlockPos;

public class IronFurnacesWireless implements ModInitializer {
	public static final String MOD_ID = "ironfurnaces"; // Keep same namespace for resources/compat or use wireless

	public static Block WIRELESS_HEATER;
	public static BlockEntityType<BlockWirelessHeaterTile> WIRELESS_HEATER_TILE;
	public static MenuType<BlockWirelessHeaterScreenHandler> WIRELESS_HEATER_SCREEN_HANDLER;
	public static Item HEATER;

	@Override
	public void onInitialize() {
		WIRELESS_HEATER = new BlockWirelessHeater();
		Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, BlockWirelessHeater.HEATER),
				WIRELESS_HEATER);

		Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, BlockWirelessHeater.HEATER),
				new BlockItemHeater(WIRELESS_HEATER,
						new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(
								net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier
										.fromNamespaceAndPath(MOD_ID, BlockWirelessHeater.HEATER)))));

		WIRELESS_HEATER_TILE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
				Identifier.fromNamespaceAndPath(MOD_ID, BlockWirelessHeater.HEATER),
				new BlockEntityType<>(BlockWirelessHeaterTile::new, java.util.Set.of(WIRELESS_HEATER)));

		WIRELESS_HEATER_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU,
				Identifier.fromNamespaceAndPath(MOD_ID, BlockWirelessHeater.HEATER),
				new net.fabricmc.fabric.api.menu.v1.ExtendedMenuType<>(BlockWirelessHeaterScreenHandler::new,
						BlockPos.STREAM_CODEC));

		HEATER = new ItemHeater();
		Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "item_heater"), HEATER);

		// Add to creative tab
		ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
				net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
				Identifier.fromNamespaceAndPath(MOD_ID, "general"));
		CreativeModeTabEvents.modifyOutputEvent(tabKey).register(content -> {
			ItemStack unchargedHeater = new ItemStack(WIRELESS_HEATER);
			net.minecraft.world.item.component.CustomData.update(
					net.minecraft.core.component.DataComponents.CUSTOM_DATA, unchargedHeater,
					tag -> tag.putInt("energy", 0));
			content.accept(unchargedHeater);

			ItemStack chargedHeater = new ItemStack(WIRELESS_HEATER);
			net.minecraft.world.item.component.CustomData.update(
					net.minecraft.core.component.DataComponents.CUSTOM_DATA, chargedHeater,
					tag -> tag.putInt("energy", 100000));
			content.accept(chargedHeater);

			content.accept(HEATER);
		});
	}
}
