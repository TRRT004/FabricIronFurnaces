package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockObsidianFurnaceTile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BlockObsidianFurnace extends BlockIronFurnaceBase {

    public static final String OBSIDIAN_FURNACE = "obsidian_furnace";

    public BlockObsidianFurnace() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).destroyTime(50.0F).explosionResistance(6000.0F));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, Reference.OBSIDIAN_FURNACE_TILE);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockObsidianFurnaceTile(pos, state);
    }
}
