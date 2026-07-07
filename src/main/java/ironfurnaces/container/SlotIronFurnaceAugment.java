package ironfurnaces.container;

import ironfurnaces.items.ItemAugment;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class SlotIronFurnaceAugment extends Slot {

    public SlotIronFurnaceAugment(BlockIronFurnaceScreenHandlerBase handler, Container container, int index, int x, int y) {
        super(container, index, x, y);
     }
 
     @Override
     public boolean mayPlace(ItemStack stack) {
         return stack.getItem() instanceof ItemAugment;
     }
 
     @Override
     public int getMaxStackSize(ItemStack stack) {
         return 1;
     }
}
