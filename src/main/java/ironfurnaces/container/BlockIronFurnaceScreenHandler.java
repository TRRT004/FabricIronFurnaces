package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class BlockIronFurnaceScreenHandler extends BlockIronFurnaceScreenHandlerBase {


    public BlockIronFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, BlockPos pos) {
        super(Reference.IRON_FURNACE_SCREEN_HANDLER, syncId, playerInventory, pos);
    }

    public BlockIronFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.Container inventory, ContainerData propertyDelegate) {
        super(Reference.IRON_FURNACE_SCREEN_HANDLER, syncId, playerInventory, inventory, propertyDelegate);
    }
}
