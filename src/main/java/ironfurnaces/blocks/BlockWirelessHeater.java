package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockWirelessHeaterTile;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BlockWirelessHeater extends Block implements EntityBlock {

	public static final String HEATER = "block_heater";

	public BlockWirelessHeater() {
		super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
				.setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK,
						net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, HEATER))));
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockWirelessHeaterTile(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
			BlockEntityType<T> type) {
		return checkType(world, type, Reference.WIRELESS_HEATER_TILE);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(Level world, BlockEntityType<T> givenType,
			BlockEntityType<? extends BlockWirelessHeaterTile> expectedType) {
		return world.isClientSide() ? null : checkType(givenType, expectedType, BlockWirelessHeaterTile::tick);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(
			BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
		return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BlockWirelessHeaterTile) {
			if (itemStack.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME)) {
				((BlockWirelessHeaterTile) blockEntity).setCustomName(itemStack.getHoverName());
			}
			net.minecraft.world.item.component.CustomData customData = itemStack
					.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
			if (customData != null) {
				((BlockWirelessHeaterTile) blockEntity).setStored(customData.copyTag().getInt("energy").orElse(0));
			}
		}

	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player,
			BlockHitResult hit) {
		if (world.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return InteractionResult.CONSUME;
		}
	}

	protected void openScreen(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BlockWirelessHeaterTile) {
			player.openMenu((MenuProvider) blockEntity);
		}

	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, net.minecraft.server.level.ServerLevel world,
			BlockPos pos, boolean moved) {
		super.affectNeighborsAfterRemoval(state, world, pos, moved);
		world.updateNeighborsAt(pos, this);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos,
			net.minecraft.core.Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

}
