package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class BlockSilverFurnaceScreenHandler extends BlockIronFurnaceScreenHandlerBase {

	public BlockSilverFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory,
			BlockPos pos) {
		super(Reference.SILVER_FURNACE_SCREEN_HANDLER, syncId, playerInventory, pos);
	}

	public BlockSilverFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory,
			net.minecraft.world.Container inventory, ContainerData propertyDelegate) {
		super(Reference.SILVER_FURNACE_SCREEN_HANDLER, syncId, playerInventory, inventory, propertyDelegate);
	}
}
