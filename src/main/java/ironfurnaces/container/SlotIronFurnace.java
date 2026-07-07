package ironfurnaces.container;

import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class SlotIronFurnace extends Slot {

	private final Player player;
	private int amount;

	public SlotIronFurnace(Player player, Container container, int index, int x, int y) {
		super(container, index, x, y);
		this.player = player;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack remove(int amount) {
		if (this.hasItem()) {
			this.amount += Math.min(amount, this.getItem().getCount());
		}

		return super.remove(amount);
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		this.checkTakeAchievements(stack);
		super.onTake(player, stack);
	}

	@Override
	protected void checkTakeAchievements(ItemStack stack) {
		stack.onCraftedBy(this.player, this.amount);
		if (!this.player.level().isClientSide() && this.container instanceof BlockIronFurnaceTileBase) {
			((BlockIronFurnaceTileBase) this.container).grantExperience(this.player.level(), this.player.position());
		}

		this.amount = 0;
	}

}
