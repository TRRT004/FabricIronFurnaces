package ironfurnaces.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class ItemAugmentFuel extends Item {

	public ItemAugmentFuel() {
		super(new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(
				net.minecraft.core.registries.Registries.ITEM,
				net.minecraft.resources.Identifier.fromNamespaceAndPath("ironfurnaces_augments", "augment_fuel"))));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context,
			net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip,
			net.minecraft.world.item.TooltipFlag flag) {
		tooltip.accept(Component.translatable("tooltip.ironfurnaces_augments.augment_fuel_pro")
				.withStyle(ChatFormatting.GREEN));
		tooltip.accept(Component.translatable("tooltip.ironfurnaces_augments.augment_fuel_con")
				.withStyle(ChatFormatting.DARK_RED));
	}
}
