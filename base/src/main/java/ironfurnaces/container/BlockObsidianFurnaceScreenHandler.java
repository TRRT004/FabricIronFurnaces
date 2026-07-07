package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class BlockObsidianFurnaceScreenHandler extends BlockIronFurnaceScreenHandlerBase {


    public BlockObsidianFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, BlockPos pos) {
        super(Reference.OBSIDIAN_FURNACE_SCREEN_HANDLER, syncId, playerInventory, pos);
    }

    public BlockObsidianFurnaceScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.Container inventory, ContainerData propertyDelegate) {
        super(Reference.OBSIDIAN_FURNACE_SCREEN_HANDLER, syncId, playerInventory, inventory, propertyDelegate);
    }
}
