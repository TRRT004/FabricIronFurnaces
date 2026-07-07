package ironfurnaces;

import ironfurnaces.gui.*;
import ironfurnaces.init.Reference;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class IronFurnacesClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MenuScreens.register(Reference.IRON_FURNACE_SCREEN_HANDLER, BlockIronFurnaceScreen::new);

		MenuScreens.register(Reference.GOLD_FURNACE_SCREEN_HANDLER, BlockGoldFurnaceScreen::new);

		MenuScreens.register(Reference.DIAMOND_FURNACE_SCREEN_HANDLER, BlockDiamondFurnaceScreen::new);

		MenuScreens.register(Reference.EMERALD_FURNACE_SCREEN_HANDLER, BlockEmeraldFurnaceScreen::new);

		MenuScreens.register(Reference.OBSIDIAN_FURNACE_SCREEN_HANDLER, BlockObsidianFurnaceScreen::new);

		MenuScreens.register(Reference.CRYSTAL_FURNACE_SCREEN_HANDLER, BlockCrystalFurnaceScreen::new);

		MenuScreens.register(Reference.NETHERITE_FURNACE_SCREEN_HANDLER, BlockNetheriteFurnaceScreen::new);

		MenuScreens.register(Reference.COPPER_FURNACE_SCREEN_HANDLER, BlockCopperFurnaceScreen::new);

		MenuScreens.register(Reference.SILVER_FURNACE_SCREEN_HANDLER, BlockSilverFurnaceScreen::new);

		MenuScreens.register(Reference.WIRELESS_HEATER_SCREEN_HANDLER, BlockWirelessHeaterScreen::new);
	}

	public static boolean isShiftKeyDown() {
		return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	public static boolean isKeyDown(int glfw) {
		com.mojang.blaze3d.platform.InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(glfw);
		if (key != InputConstants.UNKNOWN) {
			try {
				return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key.getValue());
			} catch (Exception ignored) {
			}
		}
		return false;
	}
}
