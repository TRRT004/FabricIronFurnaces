package ironfurnaces.tileentity;

import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.container.BlockObsidianFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.BlockPos;

public class BlockObsidianFurnaceTile extends BlockIronFurnaceTileBase {
    public BlockObsidianFurnaceTile(BlockPos pos, BlockState state) {
        super(Reference.OBSIDIAN_FURNACE_TILE, pos, state);
    }

    @Override
    protected int getCookTimeConfig() {
        return IronFurnacesConfig.obsidianFurnaceSpeed;
    }

    @Override
    public String getContainerName() {
        return "container.ironfurnaces.obsidian_furnace";
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new BlockObsidianFurnaceScreenHandler(i, playerInventory, this, this.propertyDelegate);
    }

}
