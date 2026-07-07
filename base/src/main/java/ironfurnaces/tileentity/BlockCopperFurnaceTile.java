package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockCopperFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BlockCopperFurnaceTile extends BlockIronFurnaceTileBase {
    public BlockCopperFurnaceTile(BlockPos pos, BlockState state) {
        super(Reference.COPPER_FURNACE_TILE, pos, state);
    }

    @Override
    protected int getCookTimeConfig() {
        return IronFurnacesConfig.copperFurnaceSpeed;
    }

    @Override
    public String getContainerName() {
        return "container.ironfurnaces.copper_furnace";
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new BlockCopperFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }


}
