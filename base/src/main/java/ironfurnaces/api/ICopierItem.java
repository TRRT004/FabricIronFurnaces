package ironfurnaces.api;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public interface ICopierItem {
	InteractionResult interactCopy(Level world, BlockPos pos, Player player, InteractionHand handIn);
}
