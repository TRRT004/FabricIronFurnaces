package ironfurnaces.util;

import com.google.common.collect.Lists;
import ironfurnaces.init.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

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

	public static List<Component> getShiftInfoGui() {
		List<Component> list = Lists.newArrayList();
		list.add(Component.translatable("tooltip.ironfurnaces.gui_close"));
		MutableComponent tooltip1 = Component.translatable("tooltip." + Reference.MOD_ID + ".gui_hold_shift");
		MutableComponent shift = Component.literal("[Shift]");
		MutableComponent tooltip2 = Component.translatable("tooltip." + Reference.MOD_ID + ".gui_shift_more_options");
		tooltip1.withStyle(ChatFormatting.GRAY);
		shift.withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC);
		tooltip2.withStyle(ChatFormatting.GRAY);
		list.add(tooltip1.append(shift).append(tooltip2));
		return list;
	}

	public static Component getShiftInfoText() {
		Component tooltip1 = Component.translatable("tooltip." + Reference.MOD_ID + ".hold")
				.withStyle(ChatFormatting.GRAY);
		Component shift = Component.literal("[Shift]").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC);
		Component tooltip2 = Component.translatable("tooltip." + Reference.MOD_ID + ".for_details")
				.withStyle(ChatFormatting.GRAY);
		return tooltip1.copy().append(shift).append(tooltip2);
	}

	public static boolean isShiftKeyDown() {
		try {
			Class<?> screenClass = Class.forName("net.minecraft.client.gui.screens.Screen");
			return (boolean) screenClass.getMethod("hasShiftDown").invoke(null);
		} catch (Exception e) {
			return false;
		}
	}

}
