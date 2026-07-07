package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockCrystalFurnaceTile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

import net.minecraft.util.RandomSource;

public class BlockCrystalFurnace extends BlockIronFurnaceBase {

	public static final String CRYSTAL_FURNACE = "crystal_furnace";
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public BlockCrystalFurnace() {
		super(BlockBehaviour.Properties.ofFullCopy(Blocks.PRISMARINE).noOcclusion()
				.setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK,
						net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, CRYSTAL_FURNACE))));
		this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT, false)
				.setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return (BlockState) this.defaultBlockState()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, Boolean.valueOf(fluidState.getType() == Fluids.WATER));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY();
		double d2 = (double) pos.getZ() + 0.5D;

		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis direction$axis = direction.getAxis();
		double d4 = random.nextDouble() * 0.6D - 0.3D;
		double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
		double d6 = random.nextDouble() * 6.0D / 16.0D;
		double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
		world.addParticle(ParticleTypes.PORTAL, d0 + d5, d1 + d6 - 0.5D, d2 + d7, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.PORTAL, d0 + d5, d1 + d6 - 0.5D, d2 + d7, 0.0D, 0.0D, 0.0D);

		super.animateTick(state, world, pos, random);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
			BlockEntityType<T> type) {
		return checkType(world, type, Reference.CRYSTAL_FURNACE_TILE);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockCrystalFurnaceTile(pos, state);
	}

	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader levelReader,
			net.minecraft.world.level.ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction,
			BlockPos neighborPos, BlockState neighborState, net.minecraft.util.RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
		}
		return super.updateShape(state, levelReader, scheduledTickAccess, pos, direction, neighborPos, neighborState,
				random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED);
		super.createBlockStateDefinition(builder);
	}

}
