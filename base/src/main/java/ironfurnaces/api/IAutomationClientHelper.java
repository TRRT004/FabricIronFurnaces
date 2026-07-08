package ironfurnaces.api;

import net.minecraft.core.BlockPos;

public interface IAutomationClientHelper {
	void sendSettingsPacket(BlockPos pos, int index, int value);
}
