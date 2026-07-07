package ironfurnaces.items;

import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemFurnaceCopy extends Item {


    public ItemFurnaceCopy() {
        super(new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, "item_copy"))));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        net.minecraft.world.item.component.CustomData customData = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData != null) {
            int[] settings = customData.copyTag().getIntArray("settings").orElse(new int[10]);
            tooltip.accept(Component.literal("Down: " + settings[0]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("Up: " + settings[1]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("North: " + settings[2]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("South: " + settings[3]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("West: " + settings[4]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("East: " + settings[5]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("Auto Input: " + settings[6]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("Auto Output: " + settings[7]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("Redstone Mode: " + settings[8]).withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal("Redstone Value: " + settings[9]).withStyle(ChatFormatting.GRAY));
        }
        tooltip.accept(Component.literal("Right-click to copy settings"));
        tooltip.accept(Component.literal("Sneak & right-click to apply settings"));
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (!ctx.getPlayer().isShiftKeyDown())
        {
            return super.useOn(ctx);
        }
        if (!world.isClientSide()) {
            BlockEntity te = world.getBlockEntity(pos);

            if (!(te instanceof BlockIronFurnaceTileBase)) {
                return super.useOn(ctx);
            }

            ItemStack stack = ctx.getItemInHand();
            net.minecraft.world.item.component.CustomData customData = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
            if (customData != null)
            {
                int[] settings = customData.copyTag().getIntArray("settings").orElse(new int[10]);
                for (int i = 0; i < settings.length; i++)
                    ((BlockIronFurnaceTileBase) te).furnaceSettings.set(i, settings[i]);
            }
            world.sendBlockUpdated(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            ctx.getPlayer().sendSystemMessage(Component.literal("Settings applied"));
        }

        return super.useOn(ctx);
    }
}
