package ironfurnaces.blocks;

import ironfurnaces.init.Reference;
import ironfurnaces.items.ItemEnergyDisplay;
import ironfurnaces.util.StringHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;

public class BlockItemHeater extends BlockItem implements ItemEnergyDisplay {
	public BlockItemHeater(Block block, Item.Properties properties) {
		super(block, properties);
	}

	private final int capacity = 100000;

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, Item.TooltipContext context,
			net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip,
			net.minecraft.world.item.TooltipFlag flag) {
		net.minecraft.world.item.component.CustomData customData = stack
				.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
		if (customData != null) {
			tooltip.accept(Component.literal(
					StringHelper.displayEnergy(customData.copyTag().getDouble("energy").orElse(0.0), capacity).get(0))
					.withStyle(ChatFormatting.GOLD));
		}
		if (StringHelper.isShiftKeyDown()) {
			tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater_block")
					.withStyle(ChatFormatting.GRAY));
			tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater_block1")
					.withStyle(ChatFormatting.GRAY));
			tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater_block2")
					.withStyle(ChatFormatting.GRAY));
			tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater_block3")
					.withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.accept(StringHelper.getShiftInfoText());
		}

	}

	@Override
	public double getEnergy(ItemStack stack) {
		net.minecraft.world.item.component.CustomData customData = stack
				.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
		if (customData == null) {
			return 0.0;
		}
		return customData.copyTag().getDouble("energy").orElse(0.0);
	}

	@Override
	public double getMaxEnergy(ItemStack stack) {
		return 100000;
	}

	@Override
	public boolean showEnergy(ItemStack stack) {
		return stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA) != null;
	}

	@Override
	public int getEnergyColor(ItemStack stack) {
		return 0xFF800600;
	}

}
