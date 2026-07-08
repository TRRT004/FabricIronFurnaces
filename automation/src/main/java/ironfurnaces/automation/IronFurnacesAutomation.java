package ironfurnaces.automation;

import ironfurnaces.items.ItemFurnaceCopy;
import ironfurnaces.network.FurnaceSettingsPayload;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public class IronFurnacesAutomation implements ModInitializer {
	public static final String MOD_ID = "ironfurnaces"; // Keep same resource namespace

	public static Item COPY;

	@Override
	public void onInitialize() {
		COPY = new ItemFurnaceCopy();
		Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, "item_copy"), COPY);

		// Add to creative tab
		ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
				net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB,
				Identifier.fromNamespaceAndPath(MOD_ID, "general"));
		CreativeModeTabEvents.modifyOutputEvent(tabKey).register(content -> {
			content.accept(COPY);
		});

		// Register Server-Side Settings sync payload receiver
		PayloadTypeRegistry.serverboundPlay().register(FurnaceSettingsPayload.TYPE, FurnaceSettingsPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(FurnaceSettingsPayload.TYPE, (payload, context) -> {
			((net.minecraft.server.level.ServerLevel) context.player().level()).getServer().execute(() -> {
				BlockPos pos = payload.pos();
				int index = payload.index();
				int set = payload.set();
				BlockEntity tee = context.player().level().getBlockEntity(pos);
				if (tee != null && tee instanceof BlockIronFurnaceTileBase) {
					BlockIronFurnaceTileBase te = (BlockIronFurnaceTileBase) tee;

					te.furnaceSettings.set(index, set);
					te.getLevel().sendBlockUpdated(pos, te.getLevel().getBlockState(pos),
							te.getLevel().getBlockState(pos), 2);
				}
			});
		});
	}
}
