package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockEmeraldFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockEmeraldFurnaceTile extends BlockIronFurnaceTileBase {
	public BlockEmeraldFurnaceTile(BlockPos pos, BlockState state) {
		super(Reference.EMERALD_FURNACE_TILE, pos, state);
	}

	@Override
	protected int getCookTimeConfig() {
		return IronFurnacesConfig.emeraldFurnaceSpeed;
	}

	@Override
	public String getContainerName() {
		return "container.ironfurnaces.emerald_furnace";
	}

	@Override
	public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory,
			Player playerEntity) {
		return new BlockEmeraldFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
	}

}
