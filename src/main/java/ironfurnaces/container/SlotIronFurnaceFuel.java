package ironfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.Slot;

public class SlotIronFurnaceFuel extends Slot {
	private final BlockIronFurnaceScreenHandlerBase handler;

	public SlotIronFurnaceFuel(BlockIronFurnaceScreenHandlerBase handler, Container container, int index, int x,
			int y) {
		super(container, index, x, y);
		this.handler = handler;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return this.handler.isFuel(stack) || isBucket(stack);
	}

	public static boolean isBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}

}
