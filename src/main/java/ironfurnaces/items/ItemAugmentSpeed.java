package ironfurnaces.items;

import ironfurnaces.init.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemAugmentSpeed extends ItemAugment {


    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".augment_speed_pro").withStyle(ChatFormatting.GREEN));
        tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".augment_speed_con").withStyle(ChatFormatting.DARK_RED));

    }
}
