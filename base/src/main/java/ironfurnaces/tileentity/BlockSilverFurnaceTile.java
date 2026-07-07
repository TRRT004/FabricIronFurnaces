package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockSilverFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockSilverFurnaceTile extends BlockIronFurnaceTileBase {
    public BlockSilverFurnaceTile(BlockPos pos, BlockState state) {
        super(Reference.SILVER_FURNACE_TILE, pos, state);
    }

    @Override
    protected int getCookTimeConfig() {
        return IronFurnacesConfig.silverFurnaceSpeed;
    }

    @Override
    public String getContainerName() {
        return "container.ironfurnaces.silver_furnace";
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new BlockSilverFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }

}
