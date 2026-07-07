package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockGoldFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockGoldFurnaceTile extends BlockIronFurnaceTileBase {
	public BlockGoldFurnaceTile(BlockPos pos, BlockState state) {
		super(Reference.GOLD_FURNACE_TILE, pos, state);
	}

	@Override
	protected int getCookTimeConfig() {
		return IronFurnacesConfig.goldFurnaceSpeed;
	}

	@Override
	public String getContainerName() {
		return "container.ironfurnaces.gold_furnace";
	}

	@Override
	public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory,
			Player playerEntity) {
		return new BlockGoldFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
	}

}
