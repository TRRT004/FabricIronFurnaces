package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockDiamondFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockDiamondFurnaceTile extends BlockIronFurnaceTileBase {
    public BlockDiamondFurnaceTile(BlockPos pos, BlockState state) {
        super(Reference.DIAMOND_FURNACE_TILE, pos, state);
    }

    @Override
    protected int getCookTimeConfig() {
        return IronFurnacesConfig.diamondFurnaceSpeed;
    }

    @Override
    public String getContainerName() {
        return "container.ironfurnaces.diamond_furnace";
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new BlockDiamondFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }

}
