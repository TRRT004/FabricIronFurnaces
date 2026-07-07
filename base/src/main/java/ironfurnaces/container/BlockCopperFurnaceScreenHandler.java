package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class BlockCopperFurnaceScreenHandler extends BlockIronFurnaceScreenHandlerBase {


    public BlockCopperFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, BlockPos pos) {
        super(Reference.COPPER_FURNACE_SCREEN_HANDLER, syncId, playerInventory, pos);
    }

    public BlockCopperFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.Container inventory, ContainerData propertyDelegate) {
        super(Reference.COPPER_FURNACE_SCREEN_HANDLER, syncId, playerInventory, inventory, propertyDelegate);
    }

}
