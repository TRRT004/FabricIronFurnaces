package ironfurnaces.tileentity;

import ironfurnaces.container.BlockWirelessHeaterScreenHandler;
import ironfurnaces.items.ItemHeater;
import ironfurnaces.wireless.IronFurnacesWireless;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.item.ItemEntity;

public class BlockWirelessHeaterTile extends TileEntityInventory
		implements
			net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider<BlockPos>,
			ironfurnaces.api.IHeaterSource {

	private double energy;
	private int capacity = 100000;

	public BlockWirelessHeaterTile(BlockPos pos, BlockState state) {
		super(IronFurnacesWireless.WIRELESS_HEATER_TILE, pos, state, 1);
	}

	public double getEnergy() {
		return this.energy;
	}

	public int getCapacity() {
		return this.capacity;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveWithoutMetadata(registries);
	}

	public static void tick(Level world, BlockPos pos, BlockState state, BlockWirelessHeaterTile e) {
		if (!world.isClientSide()) {
			ItemStack stack = e.getItem(0);
			if (!stack.isEmpty()) {
				CompoundTag nbt = new CompoundTag();
				nbt.putInt("X", pos.getX());
				nbt.putInt("Y", pos.getY());
				nbt.putInt("Z", pos.getZ());
				stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
						net.minecraft.world.item.component.CustomData.of(nbt));
			}
		}
	}

	@Override
	protected void loadAdditional(ValueInput compound) {
		super.loadAdditional(compound);
		this.energy = compound.getDoubleOr("energy", 0.0);
		this.capacity = compound.getIntOr("capacity", 100000);
	}

	@Override
	protected void saveAdditional(ValueOutput compound) {
		super.saveAdditional(compound);
		compound.putDouble("energy", this.energy);
		compound.putInt("capacity", this.capacity);
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		return null;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return true;
	}

	@Override
	public String getContainerName() {
		return "container.ironfurnaces.wireless_energy_heater";
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.getItem() instanceof ItemHeater;
	}

	@Override
	public AbstractContainerMenu createMenu(int i, net.minecraft.world.entity.player.Inventory playerInventory,
			Player playerEntity) {
		return new BlockWirelessHeaterScreenHandler(i, playerEntity.getInventory(), this);
	}

	@Override
	public BlockPos getScreenOpeningData(ServerPlayer player) {
		return this.worldPosition;
	}

	public void extractEnergy(double amount) {
		this.energy -= Math.min(amount, this.energy);
		this.setChanged();
	}

	@Deprecated
	public void setStored(double amount) {
		energy = amount;
		if (energy > getMaxStoredPower()) {
			energy = getMaxStoredPower();
		}
		if (energy < 0) {
			energy = 0;
		}
		this.setChanged();
	}

	public double getMaxStoredPower() {
		return capacity;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (hasLevel() && getLevel() instanceof ServerLevel) {
			sync();
		}
	}

	@Override
	public void preRemoveSideEffects(BlockPos pos, BlockState state) {
		super.preRemoveSideEffects(pos, state);
		if (this.level != null && !this.level.isClientSide()) {
			ItemStack stack = new ItemStack(IronFurnacesWireless.WIRELESS_HEATER);
			net.minecraft.world.item.component.CustomData.update(
					net.minecraft.core.component.DataComponents.CUSTOM_DATA, stack,
					tag -> tag.putInt("energy", (int) this.getEnergy()));
			java.util.Random rand = new java.util.Random();
			this.level.addFreshEntity(new ItemEntity(this.level, pos.getX() + rand.nextInt(1),
					pos.getY() + rand.nextInt(1), pos.getZ() + rand.nextInt(1), stack));
		}
	}
}
