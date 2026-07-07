package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockEmeraldFurnaceTile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BlockEmeraldFurnace extends BlockIronFurnaceBase {

	public static final String EMERALD_FURNACE = "emerald_furnace";

	public BlockEmeraldFurnace() {
		super(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK)
				.setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK,
						net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, EMERALD_FURNACE))));
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
			BlockEntityType<T> type) {
		return checkType(world, type, Reference.EMERALD_FURNACE_TILE);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockEmeraldFurnaceTile(pos, state);
	}
}
