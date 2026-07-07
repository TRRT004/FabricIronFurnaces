package ironfurnaces.container;

import ironfurnaces.init.Reference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;


public class BlockWirelessHeaterScreenHandler extends AbstractContainerMenu {

    private final Container inventory;
    protected Level world;
    public BlockPos pos;
    public int capacity = 100000;

    public BlockWirelessHeaterScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, new SimpleContainer(1));
        this.pos = pos;
        world = playerInventory.player.level();
    }

    public BlockWirelessHeaterScreenHandler(int syncId, net.minecraft.world.entity.player.Inventory playerInventory, Container inventory) {
        super(Reference.WIRELESS_HEATER_SCREEN_HANDLER, syncId);
        checkContainerSize(inventory, 1);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);

        this.addSlot(new SlotHeater(inventory, 0, 80, 37));

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
        this.pos = BlockPos.ORIGIN;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Level getLevel() {
        return world;
    }

    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 1) {
                if (!this.moveItemStackTo(itemstack1, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}
