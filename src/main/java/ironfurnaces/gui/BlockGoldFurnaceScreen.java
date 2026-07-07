package ironfurnaces.gui;

import ironfurnaces.container.BlockGoldFurnaceScreenHandler;
import ironfurnaces.container.BlockIronFurnaceScreenHandler;
import ironfurnaces.init.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BlockGoldFurnaceScreen extends BlockIronFurnaceScreenBase<BlockGoldFurnaceScreenHandler> {

    public BlockGoldFurnaceScreen(BlockGoldFurnaceScreenHandler container, net.minecraft.world.entity.player.Inventory inv, Component name) {
        super(container, inv, name, Identifier.fromNamespaceAndPath(Reference.MOD_ID,"textures/gui/furnace.png"));
    }
}
