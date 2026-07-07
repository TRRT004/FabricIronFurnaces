package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockIronFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockIronFurnaceTile extends BlockIronFurnaceTileBase {
    public BlockIronFurnaceTile(BlockPos pos, BlockState state) {
        super(Reference.IRON_FURNACE_TILE, pos, state);
    }

    @Override
    protected int getCookTimeConfig() {
        return IronFurnacesConfig.ironFurnaceSpeed;
    }

    @Override
    public String getContainerName() {
        return "container.ironfurnaces.iron_furnace";
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new BlockIronFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }

}
