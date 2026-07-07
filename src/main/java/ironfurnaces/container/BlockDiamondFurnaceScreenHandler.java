package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class BlockDiamondFurnaceScreenHandler extends BlockIronFurnaceScreenHandlerBase {


    public BlockDiamondFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, BlockPos pos) {
        super(Reference.DIAMOND_FURNACE_SCREEN_HANDLER, syncId, playerInventory, pos);
    }

    public BlockDiamondFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.entity.player.Inventory inventory, ContainerData propertyDelegate) {
        super(Reference.DIAMOND_FURNACE_SCREEN_HANDLER, syncId, playerInventory, inventory, propertyDelegate);
    }
}
