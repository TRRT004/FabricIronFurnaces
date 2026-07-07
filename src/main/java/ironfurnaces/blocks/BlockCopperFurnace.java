package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockCopperFurnaceTile;

import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BlockCopperFurnace extends BlockIronFurnaceBase implements EntityBlock {

    public static final String COPPER_FURNACE = "copper_furnace";

    public BlockCopperFurnace() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK));
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, Reference.COPPER_FURNACE_TILE);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockCopperFurnaceTile(pos, state);
    }
}
