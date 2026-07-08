package ironfurnaces.wireless;

import ironfurnaces.gui.BlockWirelessHeaterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class IronFurnacesWirelessClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(IronFurnacesWireless.WIRELESS_HEATER_SCREEN_HANDLER, BlockWirelessHeaterScreen::new);
	}
}
