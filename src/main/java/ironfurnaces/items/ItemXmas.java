package ironfurnaces.items;

import ironfurnaces.init.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemXmas extends Item {


    public ItemXmas(Item.Properties properties) {
        super(properties);
    }


    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".xmas_right_click").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".xmas1").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".xmas2").withStyle(ChatFormatting.GRAY));
    }
}
