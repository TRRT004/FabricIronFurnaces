package ironfurnaces.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockSilverFurnaceTile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BlockSilverFurnace extends BlockIronFurnaceBase {

	public static final String SILVER_FURNACE = "silver_furnace";

	public BlockSilverFurnace() {
		super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
				.setId(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK,
						net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, SILVER_FURNACE))));
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
			BlockEntityType<T> type) {
		return checkType(world, type, Reference.SILVER_FURNACE_TILE);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockSilverFurnaceTile(pos, state);
	}

}
