package ironfurnaces.gui;

import ironfurnaces.container.BlockNetheriteFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BlockNetheriteFurnaceScreen extends BlockIronFurnaceScreenBase<BlockNetheriteFurnaceScreenHandler> {

    public BlockNetheriteFurnaceScreen(BlockNetheriteFurnaceScreenHandler container, net.minecraft.world.entity.player.Inventory inv, Component name) {
        super(container, inv, name, Identifier.fromNamespaceAndPath(Reference.MOD_ID,"textures/gui/furnace_netherite.png"));
    }
}
