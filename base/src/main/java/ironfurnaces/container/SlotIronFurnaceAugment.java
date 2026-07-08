package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class SlotIronFurnaceAugment extends Slot {

	public SlotIronFurnaceAugment(Container container, int index, int x, int y) {
		super(container, index, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(Reference.AUGMENT_TAG);
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return 1;
	}
}
