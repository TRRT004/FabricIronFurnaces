package ironfurnaces.container;

import ironfurnaces.items.ItemHeater;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class SlotHeater extends Slot {

    public SlotHeater(Container inv, int slotIndex, int xPosition, int yPosition) {
        super(inv, slotIndex, xPosition, yPosition);
    }


    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ItemHeater;
    }

}
