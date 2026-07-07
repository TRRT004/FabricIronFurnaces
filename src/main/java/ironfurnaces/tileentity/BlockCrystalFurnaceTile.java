package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockCrystalFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockCrystalFurnaceTile extends BlockIronFurnaceTileBase {
    public BlockCrystalFurnaceTile(BlockPos pos, BlockState state) {
        super(Reference.CRYSTAL_FURNACE_TILE, pos, state);
    }

    @Override
    protected int getCookTimeConfig() {
        return IronFurnacesConfig.crystalFurnaceSpeed;
    }

    @Override
    public String getContainerName() {
        return "container.ironfurnaces.crystal_furnace";
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new BlockCrystalFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }

}
