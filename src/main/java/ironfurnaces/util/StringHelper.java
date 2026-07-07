package ironfurnaces.util;

import com.google.common.collect.Lists;
import ironfurnaces.init.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class StringHelper {


    public static List<String> displayEnergy(double energy, double capacity) {
        List<String> text = new ArrayList<String>();
        NumberFormat format = DecimalFormat.getNumberInstance();
        String i = format.format(energy);
        String j = format.format(capacity);
        i = i.replaceAll("\u00A0", ",");
        j = j.replaceAll("\u00A0", ",");
        text.add(i + " / " + j + " E");
        return text;
    }

    public static List<Component> getShiftInfoGui()
    {
        List<Component> list = Lists.newArrayList();
        list.add(Component.translatable("tooltip.ironfurnaces.gui_close"));
        MutableText tooltip1 = Component.translatable("tooltip." + Reference.MOD_ID + ".gui_hold_shift");
        MutableText shift = Component.literal("[Shift]");
        MutableText tooltip2 = Component.translatable("tooltip." + Reference.MOD_ID + ".gui_shift_more_options");
        tooltip1.formatted(ChatFormatting.GRAY);
        shift.formatted(ChatFormatting.GOLD, ChatFormatting.ITALIC);
        tooltip2.formatted(ChatFormatting.GRAY);
        list.add(tooltip1.append(shift).append(tooltip2));
        return list;
    }

    public static Component getShiftInfoText()
    {
        Component tooltip1 = Component.translatable("tooltip." + Reference.MOD_ID + ".hold").withStyle(ChatFormatting.GRAY);
        Component shift = Component.literal("[Shift]").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC);
        Component tooltip2 = Component.translatable("tooltip." + Reference.MOD_ID + ".for_details").withStyle(ChatFormatting.GRAY);
        return tooltip1.copy().append(shift).append(tooltip2);
    }

}
