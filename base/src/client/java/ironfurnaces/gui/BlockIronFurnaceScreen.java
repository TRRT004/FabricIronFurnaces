package ironfurnaces.gui;

import ironfurnaces.container.BlockIronFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BlockIronFurnaceScreen extends BlockIronFurnaceScreenBase<BlockIronFurnaceScreenHandler> {

	public BlockIronFurnaceScreen(BlockIronFurnaceScreenHandler container,
			net.minecraft.world.entity.player.Inventory inv, Component name) {
		super(container, inv, name, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/furnace.png"));
	}
}
