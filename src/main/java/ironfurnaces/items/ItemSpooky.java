package ironfurnaces.items;

import ironfurnaces.init.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;

public class ItemSpooky extends Item {

	public ItemSpooky(Item.Properties properties) {
		super(properties);
	}

	@Environment(EnvType.CLIENT)
	@Override
	// Overriding appendHoverText is deprecated in Minecraft, but it remains the
	// standard method to dynamically populate item tooltips.
	public void appendHoverText(ItemStack stack, Item.TooltipContext context,
			net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip,
			net.minecraft.world.item.TooltipFlag flag) {
		tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".spooky_right_click")
				.withStyle(ChatFormatting.GRAY));
		tooltip.accept(
				Component.translatable("tooltip." + Reference.MOD_ID + ".spooky1").withStyle(ChatFormatting.GRAY));
		tooltip.accept(
				Component.translatable("tooltip." + Reference.MOD_ID + ".spooky2").withStyle(ChatFormatting.GRAY));
		tooltip.accept(
				Component.translatable("tooltip." + Reference.MOD_ID + ".spooky3").withStyle(ChatFormatting.GRAY));
	}
}
