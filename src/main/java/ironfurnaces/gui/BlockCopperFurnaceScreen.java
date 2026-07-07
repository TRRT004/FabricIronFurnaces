package ironfurnaces.gui;

import ironfurnaces.container.BlockCopperFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BlockCopperFurnaceScreen extends BlockIronFurnaceScreenBase<BlockCopperFurnaceScreenHandler> {

    public BlockCopperFurnaceScreen(BlockCopperFurnaceScreenHandler container, net.minecraft.world.entity.player.Inventory inv, Component name) {
        super(container, inv, name, Identifier.fromNamespaceAndPath(Reference.MOD_ID,"textures/gui/furnace.png"));
    }
}
