package ironfurnaces.tileentity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.Optional;

public abstract class TileEntityInventory extends BlockEntity implements ITileInventory, WorldlyContainer, MenuProvider, Nameable {
    protected NonNullList<ItemStack> inventory;
    protected Component name;

    public TileEntityInventory(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, int sizeContainer) {
        super(tileEntityTypeIn, pos, state);
        this.inventory = NonNullList.withSize(sizeContainer, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.isItemValidForSlot(index, stack);
    }

    public void setCustomName(Component name) {
        this.name = name;
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : Component.translatable(getContainerName());
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return canExtractItem(index, stack, direction);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return (ItemStack)this.inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.inventory);
        Optional<Component> nameOpt = input.read("CustomName", net.minecraft.network.chat.ComponentSerialization.CODEC);
        this.name = nameOpt.isPresent() ? nameOpt.get() : null;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.inventory);
        if (this.name != null) {
            output.store("CustomName", net.minecraft.network.chat.ComponentSerialization.CODEC, this.name);
        }
    }

    public void sync() {
        if (this.level != null && !this.level.isClientSide()) {
            ((net.minecraft.server.level.ServerLevel) this.level).getChunkSource().blockChanged(this.worldPosition);
        }
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    @Override
    public Component getCustomName() {
        return this.name;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return createMenu(i, playerInventory, playerEntity);
    }
}
