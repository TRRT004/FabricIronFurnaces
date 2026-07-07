package ironfurnaces.gui;

import ironfurnaces.container.BlockCrystalFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BlockCrystalFurnaceScreen extends BlockIronFurnaceScreenBase<BlockCrystalFurnaceScreenHandler> {

    public BlockCrystalFurnaceScreen(BlockCrystalFurnaceScreenHandler container, net.minecraft.world.entity.player.Inventory inv, Component name) {
        super(container, inv, name, Identifier.fromNamespaceAndPath(Reference.MOD_ID,"textures/gui/furnace.png"));
    }
}
