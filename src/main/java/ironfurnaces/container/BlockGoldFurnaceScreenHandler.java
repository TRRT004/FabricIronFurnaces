package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class BlockGoldFurnaceScreenHandler extends BlockIronFurnaceScreenHandlerBase {


    public BlockGoldFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, BlockPos pos) {
        super(Reference.GOLD_FURNACE_SCREEN_HANDLER, syncId, playerInventory, pos);
    }

    public BlockGoldFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.Container inventory, ContainerData propertyDelegate) {
        super(Reference.GOLD_FURNACE_SCREEN_HANDLER, syncId, playerInventory, inventory, propertyDelegate);
    }
}
