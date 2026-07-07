package ironfurnaces.blocks;

import ironfurnaces.items.ItemAugment;
import ironfurnaces.items.ItemFurnaceCopy;
import ironfurnaces.items.ItemSpooky;
import ironfurnaces.items.ItemXmas;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public abstract class BlockIronFurnaceBase extends Block implements EntityBlock {
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 4);
    public static final IntegerProperty JOVIAL = IntegerProperty.create("jovial", 0, 2);

    public BlockIronFurnaceBase(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(createLightLevelFromBlockState(13)));
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)).setValue(BlockStateProperties.LIT, false).setValue(TYPE, 0).setValue(JOVIAL, 0));
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(Level world, BlockEntityType<T> givenType, BlockEntityType<? extends BlockIronFurnaceTileBase> expectedType) {
        return world.isClientSide() ? null : checkType(givenType, expectedType, BlockIronFurnaceTileBase::tick);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    private static ToIntFunction<BlockState> createLightLevelFromBlockState(int litLevel) {
        return (blockState) -> {
            return (Boolean)blockState.getValue(BlockStateProperties.LIT) ? litLevel : 0;
        };
    }

    private int calculateOutput(Level worldIn, BlockPos pos, BlockState state) {
        BlockIronFurnaceTileBase tile = ((BlockIronFurnaceTileBase)worldIn.getBlockEntity(pos));
        int i = this.getAnalogOutputSignal(state, worldIn, pos, null);
        if (tile != null)
        {
            int j = tile.furnaceSettings.get(9);
            return tile.furnaceSettings.get(8) == 4 ? Math.max(i - j, 0) : i;
        }
        return 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter world, BlockPos pos, Direction side) {
        return getSignal(blockState, world, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter world, BlockPos pos, Direction side) {
        BlockIronFurnaceTileBase furnace = ((BlockIronFurnaceTileBase) world.getBlockEntity(pos));
        if (furnace != null)
        {
            int mode = furnace.furnaceSettings.get(8);
            if (mode == 0)
            {
                return 0;
            }
            else if (mode == 1)
            {
                return 0;
            }
            else if (mode == 2)
            {
                return 0;
            }
            else
            {
                return calculateOutput(furnace.getLevel(), pos, blockState);
            }
        }
        return 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState)this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BlockIronFurnaceTileBase) {
                ((BlockIronFurnaceTileBase)blockEntity).setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            if (stack.getItem() instanceof ItemAugment && !player.isSecondaryUseActive()) {
                this.interactAugment(world, pos, player, hand);
                return InteractionResult.CONSUME;
            } else if (stack.getItem() instanceof ItemFurnaceCopy && !player.isSecondaryUseActive()) {
                this.interactCopy(world, pos, player, hand);
                return InteractionResult.CONSUME;
            } else if (stack.getItem() instanceof ItemSpooky && !player.isSecondaryUseActive()) {
                return this.interactJovial(world, pos, player, hand, 1);
            } else if (stack.getItem() instanceof ItemXmas && !player.isSecondaryUseActive()) {
                return this.interactJovial(world, pos, player, hand, 2);
            } else {
                this.openScreen(world, pos, player);
                return InteractionResult.CONSUME;
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            if (player.isSecondaryUseActive()) {
                return this.interactJovial(world, pos, player, player.getUsedItemHand(), 0);
            } else {
                this.openScreen(world, pos, player);
                return InteractionResult.CONSUME;
            }
        }
    }

    private InteractionResult interactJovial(Level world, BlockPos pos, Player player, InteractionHand handIn, int jovial) {
        ItemStack stack = player.getItemInHand(handIn);
        if (!(stack.getItem() instanceof ItemSpooky
                || !(stack.getItem() instanceof ItemXmas)
                || !(stack.isEmpty()))) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof BlockIronFurnaceTileBase)) {
            return InteractionResult.SUCCESS;
        }
        ((BlockIronFurnaceTileBase)te).jovial = jovial;
        return InteractionResult.SUCCESS;
    }

    private InteractionResult interactCopy(Level world, BlockPos pos, Player player, InteractionHand handIn) {
        ItemStack stack = player.getInventory().getItem(player.getInventory().getSelectedSlot());
        if (!(stack.getItem() instanceof ItemFurnaceCopy)) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof BlockIronFurnaceTileBase)) {
            return InteractionResult.SUCCESS;
        }

        int[] settings = new int[((BlockIronFurnaceTileBase) te).furnaceSettings.size()];
        for (int i = 0; i < ((BlockIronFurnaceTileBase) te).furnaceSettings.size(); i++)
        {
            settings[i] = ((BlockIronFurnaceTileBase) te).furnaceSettings.get(i);
        }
        
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        tag.putIntArray("settings", settings);
        stack.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));

        ((BlockIronFurnaceTileBase)te).onUpdateSent();
        player.sendSystemMessage(Component.literal("Settings copied"));
        return InteractionResult.SUCCESS;
    }

    private InteractionResult interactAugment(Level world, BlockPos pos, Player player, InteractionHand handIn) {
        ItemStack stack = player.getItemInHand(handIn);
        if (!(stack.getItem() instanceof ItemAugment)) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof BlockIronFurnaceTileBase)) {
            return InteractionResult.SUCCESS;
        }
        if (!(((Container) te).getItem(3).isEmpty())) {
            if (!player.isCreative()) {
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(3)));
            }
        }
        ((Container) te).setItem(3, new ItemStack(stack.getItem(), 1));

        world.playSound((Player)null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.8F, 1.0F);

        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    protected void openScreen(Level world, BlockPos pos, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BlockIronFurnaceTileBase) {
            ((BlockIronFurnaceTileBase)blockEntity).placeConfig();
            player.openMenu((MenuProvider)blockEntity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
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
            double h = random.nextDouble() * 0.6D - 0.3D;
            double i = axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : h;
            double j = random.nextDouble() * 6.0D / 16.0D;
            double k = axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : h;
            world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, net.minecraft.server.level.ServerLevel world, BlockPos pos, boolean moved) {
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
        world.updateNeighborsAt(pos, this);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate((Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT, TYPE, JOVIAL);
    }
}
