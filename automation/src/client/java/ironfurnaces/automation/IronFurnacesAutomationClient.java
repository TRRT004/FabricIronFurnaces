package ironfurnaces.automation;

import ironfurnaces.api.AutomationAPI;
import ironfurnaces.network.FurnaceSettingsPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class IronFurnacesAutomationClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AutomationAPI.clientHelper = (pos, index, value) -> {
			ClientPlayNetworking.send(new FurnaceSettingsPayload(pos, index, value));
		};
	}
}
