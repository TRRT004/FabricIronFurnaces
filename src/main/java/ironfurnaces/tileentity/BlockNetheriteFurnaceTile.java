package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockNetheriteFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockNetheriteFurnaceTile extends BlockIronFurnaceTileBase {
    public BlockNetheriteFurnaceTile(BlockPos pos, BlockState state) {
        super(Reference.NETHERITE_FURNACE_TILE, pos, state);
    }

    @Override
    protected int getCookTimeConfig() {
        return IronFurnacesConfig.netheriteFurnaceSpeed;
    }

    @Override
    public String getContainerName() {
        return "container.ironfurnaces.netherite_furnace";
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new BlockNetheriteFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }

}
