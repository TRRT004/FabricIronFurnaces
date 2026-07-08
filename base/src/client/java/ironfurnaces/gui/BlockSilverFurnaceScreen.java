package ironfurnaces.gui;

import ironfurnaces.container.BlockSilverFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BlockSilverFurnaceScreen extends BlockIronFurnaceScreenBase<BlockSilverFurnaceScreenHandler> {

	public BlockSilverFurnaceScreen(BlockSilverFurnaceScreenHandler container,
			net.minecraft.world.entity.player.Inventory inv, Component name) {
		super(container, inv, name, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/furnace.png"));
	}
}
