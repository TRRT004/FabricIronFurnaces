package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class BlockNetheriteFurnaceScreenHandler extends BlockIronFurnaceScreenHandlerBase {

	public BlockNetheriteFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory,
			BlockPos pos) {
		super(Reference.NETHERITE_FURNACE_SCREEN_HANDLER, syncId, playerInventory, pos);
	}

	public BlockNetheriteFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory,
			net.minecraft.world.Container inventory, ContainerData propertyDelegate) {
		super(Reference.NETHERITE_FURNACE_SCREEN_HANDLER, syncId, playerInventory, inventory, propertyDelegate);
	}
}
