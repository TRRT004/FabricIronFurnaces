package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockCrystalFurnaceTile;
import ironfurnaces.tileentity.BlockDiamondFurnaceTile;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BlockDiamondFurnace extends BlockIronFurnaceBase {

    public static final String DIAMOND_FURNACE = "diamond_furnace";

    public BlockDiamondFurnace() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, Reference.DIAMOND_FURNACE_TILE);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockDiamondFurnaceTile(pos, state);
    }
}
