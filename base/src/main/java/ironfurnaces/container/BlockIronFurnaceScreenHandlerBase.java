package ironfurnaces.container;

import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class BlockIronFurnaceScreenHandlerBase extends AbstractContainerMenu {

	private final Container inventory;
	private final ContainerData propertyDelegate;
	private final RecipeType<? extends AbstractCookingRecipe> recipeType;
	protected Level world;
	public BlockPos pos;

	protected BlockIronFurnaceScreenHandlerBase(MenuType<?> type, int syncId,
			net.minecraft.world.entity.player.Inventory playerInventory, BlockPos pos) {
		this(type, syncId, playerInventory, new SimpleContainer(4), new SimpleContainerData(4));
		this.pos = pos;
		world = playerInventory.player.level();
	}

	protected BlockIronFurnaceScreenHandlerBase(MenuType<?> type, int syncId,
			net.minecraft.world.entity.player.Inventory playerInventory, Container inventory,
			ContainerData propertyDelegate) {
		super(type, syncId);
		this.recipeType = RecipeType.SMELTING;
		checkContainerSize(inventory, 4);
		checkContainerDataCount(propertyDelegate, 4);
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		this.world = playerInventory.player.level();
		this.addSlot(new Slot(inventory, 0, 56, 17));
		this.addSlot(new SlotIronFurnaceFuel(this, this.inventory, 1, 56, 53));
		this.addSlot(new SlotIronFurnace(playerInventory.player, this.inventory, 2, 116, 35));
		if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("ironfurnaces_augments")) {
			this.addSlot(new SlotIronFurnaceAugment(this.inventory, 3, 26, 35));
		}

		int k;
		for (k = 0; k < 3; ++k) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
			}
		}

		for (k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

		this.addDataSlots(propertyDelegate);

	}

	// addContainerListener removed - no longer overrideable in 1.21.5

	public void populateRecipeFinder(StackedItemContents finder) {
		if (this.inventory instanceof StackedContentsCompatible) {
			((StackedContentsCompatible) this.inventory).fillStackedContents(finder);
		}

	}

	public void clearCraftingSlots() {
		this.getSlot(0).set(ItemStack.EMPTY);
		this.getSlot(2).set(ItemStack.EMPTY);
	}

	public int getCraftingResultSlotIndex() {
		return 2;
	}

	public int getCraftingWidth() {
		return 1;
	}

	public int getCraftingHeight() {
		return 1;
	}

	@Environment(EnvType.CLIENT)
	public int getCraftingSlotCount() {
		return 3;
	}

	public boolean stillValid(Player player) {
		return this.inventory.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		boolean hasAugmentSlot = net.fabricmc.loader.api.FabricLoader.getInstance()
				.isModLoaded("ironfurnaces_augments");
		int playerInvStart = hasAugmentSlot ? 4 : 3;

		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();

			// Result slot (index 2)
			if (index == 2) {
				if (!this.moveItemStackTo(itemStack2, playerInvStart, playerInvStart + 36, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemStack2, itemStack);
			}
			// Move from player inventory -> container
			else if (index >= playerInvStart && index < playerInvStart + 36) {
				if (this.isSmeltable(player.level(), itemStack2)) {
					if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (this.isFuel(itemStack2)) {
					if (!this.moveItemStackTo(itemStack2, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				}
				// Player inventory -> hotbar / hotbar -> player inventory
				else if (index >= playerInvStart && index < playerInvStart + 27) {
					if (!this.moveItemStackTo(itemStack2, playerInvStart + 27, playerInvStart + 36, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= playerInvStart + 27 && index < playerInvStart + 36
						&& !this.moveItemStackTo(itemStack2, playerInvStart, playerInvStart + 27, false)) {
					return ItemStack.EMPTY;
				}
			}
			// Move from container slot (0: input, 1: fuel, 3: augment) -> player inventory
			else if (!this.moveItemStackTo(itemStack2, playerInvStart, playerInvStart + 36, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemStack2);
		}
		return itemStack;
	}

	private static net.minecraft.resources.ResourceKey<RecipePropertySet> getPropertySetKey(RecipeType<?> type) {
		if (type == RecipeType.BLASTING) {
			return RecipePropertySet.BLAST_FURNACE_INPUT;
		} else if (type == RecipeType.SMOKING) {
			return RecipePropertySet.SMOKER_INPUT;
		} else {
			return RecipePropertySet.FURNACE_INPUT;
		}
	}

	protected boolean isSmeltable(Level world, ItemStack itemStack) {
		net.minecraft.resources.ResourceKey<RecipePropertySet> key = getPropertySetKey(this.recipeType);
		return world.recipeAccess().propertySet(key).test(itemStack);
	}

	protected boolean isFuel(ItemStack itemStack) {
		return BlockIronFurnaceTileBase.isItemFuel(this.world, itemStack);
	}

	@Environment(EnvType.CLIENT)
	public int getCookProgress() {
		int i = this.propertyDelegate.get(2);
		int j = this.propertyDelegate.get(3);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	@Environment(EnvType.CLIENT)
	public int getFuelProgress() {
		int i = this.propertyDelegate.get(1);
		if (i == 0) {
			i = 200;
		}

		return this.propertyDelegate.get(0) * 13 / i;
	}

	@Environment(EnvType.CLIENT)
	public boolean isBurning() {
		return this.propertyDelegate.get(0) > 0;
	}

	public boolean canInsertIntoSlot(int index) {
		return index != 1;
	}

	public BlockPos getPos() {
		return pos;
	}

	public Level getLevel() {
		return world;
	}

}
