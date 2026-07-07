package ironfurnaces.items;

import ironfurnaces.init.Reference;
import ironfurnaces.util.StringHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;

import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemHeater extends Item {


    public ItemHeater() {
        super(new Item.Properties());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        if (StringHelper.isShiftKeyDown())
        {
            if (stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
                net.minecraft.nbt.CompoundTag nbtTag = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag();
                tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater").withStyle(ChatFormatting.GRAY));
                tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heaterX").withStyle(ChatFormatting.GRAY).append(Component.literal("" + nbtTag.getInt("X").orElse(0)).withStyle(ChatFormatting.GRAY)));
                tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heaterY").withStyle(ChatFormatting.GRAY).append(Component.literal("" + nbtTag.getInt("Y").orElse(0)).withStyle(ChatFormatting.GRAY)));
                tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heaterZ").withStyle(ChatFormatting.GRAY).append(Component.literal("" + nbtTag.getInt("Z").orElse(0)).withStyle(ChatFormatting.GRAY)));
            } else {
                tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater_not_bound").withStyle(ChatFormatting.GRAY));
                tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater_tip").withStyle(ChatFormatting.GRAY));
                tooltip.accept(Component.translatable("tooltip." + Reference.MOD_ID + ".heater_tip1").withStyle(ChatFormatting.GRAY));
            }
        }
        else
        {
            tooltip.accept(StringHelper.getShiftInfoText());
        }

    }
}
