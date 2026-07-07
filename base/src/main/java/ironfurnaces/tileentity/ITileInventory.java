package ironfurnaces.tileentity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.Direction;

public interface ITileInventory {

    public int[] getSlotsForFace(Direction side);

    public boolean canExtractItem(int index, ItemStack stack, Direction direction);

    public String getContainerName();

    public boolean isItemValidForSlot(int index, ItemStack stack);

    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity);

}
