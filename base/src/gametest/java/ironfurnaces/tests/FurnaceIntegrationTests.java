package ironfurnaces.tests;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockIronFurnaceTile;

public class FurnaceIntegrationTests {

	@GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
	public void testIronFurnaceSmeltsIron(GameTestHelper helper) {
		BlockPos pos = new BlockPos(0, 1, 0);

		// 1. Place the iron furnace block
		helper.setBlock(pos, Reference.IRON_FURNACE.defaultBlockState());

		// 2. Retrieve BlockEntity and load ingredients
		BlockIronFurnaceTile tile = (BlockIronFurnaceTile) helper.getBlockEntity(pos);
		tile.setItem(0, new ItemStack(Items.RAW_IRON));
		tile.setItem(1, new ItemStack(Items.COAL));

		// 3. Wait for smelting to finish (coal lasts longer, raw iron takes 160 ticks
		// on iron furnace by default, we wait 200 ticks)
		helper.runAtTickTime(200, () -> {
			ItemStack output = tile.getItem(2);
			helper.assertTrue(output.is(Items.IRON_INGOT), "Output should be an Iron Ingot but was: " + output);
			helper.assertTrue(tile.getItem(0).isEmpty(), "Input slot should be empty");
			helper.succeed();
		});
	}
}
