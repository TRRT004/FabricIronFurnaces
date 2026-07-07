package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockIronFurnaceTile;
import ironfurnaces.tileentity.BlockNetheriteFurnaceTile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.RandomSource;

public class BlockNetheriteFurnace extends BlockIronFurnaceBase {

    public static final String NETHERITE_FURNACE = "netherite_furnace";

    public BlockNetheriteFurnace() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).destroyTime(50.0F).explosionResistance(6000.0F));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if ((Boolean)state.getValue(BlockStateProperties.LIT)) {
            double d = (double)pos.getX() + 0.5D;
            double e = (double)pos.getY();
            double f = (double)pos.getZ() + 0.5D;
            if (random.nextDouble() < 0.1D) {
                world.playLocalSound(d, e, f, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = (Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Direction.Axis axis = direction.getAxis();
            double g = 0.52D;
            double h = random.nextDouble() * 0.6D - 0.3D;
            double i = axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : h;
            double j = random.nextDouble() * 6.0D / 16.0D;
            double k = axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : h;
            world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
        }
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, Reference.NETHERITE_FURNACE_TILE);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockNetheriteFurnaceTile(pos, state);
    }
}
