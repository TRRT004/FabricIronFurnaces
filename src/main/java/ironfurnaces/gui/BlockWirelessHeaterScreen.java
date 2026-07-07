package ironfurnaces.gui;

import ironfurnaces.container.BlockWirelessHeaterScreenHandler;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockWirelessHeaterTile;
import ironfurnaces.util.StringHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class BlockWirelessHeaterScreen extends AbstractContainerScreen<BlockWirelessHeaterScreenHandler> {

    public static ResourceLocation GUI = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/heater.png");
    net.minecraft.world.entity.player.Inventory playerInv;
    Component name;
    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;
    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    public BlockWirelessHeaterScreen(BlockWirelessHeaterScreenHandler handler, net.minecraft.world.entity.player.Inventory inv, Component name) {
        super(handler, inv, name);
        playerInv = inv;
        this.name = name;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.playerInv.getDisplayName(), 7, this.imageHeight - 93, 4210752, false);
        guiGraphics.drawString(this.font, name, this.imageWidth / 2 - this.font.width(name) / 2, 6, 4210752, false);

        int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
        int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);
        if(actualMouseX >= 65 && actualMouseX <= 111 && actualMouseY >= 64 && actualMouseY <= 76) {
            double energy = this.getEnergy();
            int capacity = this.getCapacity();
            guiGraphics.renderTooltip(this.font, Component.literal(StringHelper.displayEnergy(energy, capacity).get(0)).withStyle(ChatFormatting.GOLD), actualMouseX, actualMouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.GUI, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int k;
        k = this.getEnergyScaled(46);
        guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.GUI, i + 65, j + 64, 176, 0, k + 1, 12, 256, 256);
    }

    public int getEnergyScaled(int pixels) {
        double i = this.getEnergy();
        int j = this.getCapacity();
        return (int) (j != 0 && i != 0 ? i * pixels / j : 0);
    }

    public double getEnergy()
    {
        double energy = getEnergy(this.handler).get();
        return energy;
    }

    public int getCapacity()
    {
        int capacity = getCapacity(this.handler).get();
        return capacity;
    }

    private static Optional<Double> getEnergy(AbstractContainerMenu handler) {
        if (handler instanceof BlockWirelessHeaterScreenHandler) {
            BlockPos pos = ((BlockWirelessHeaterScreenHandler) handler).getPos();
            Level world = ((BlockWirelessHeaterScreenHandler) handler).getLevel();
            return pos != null ? Optional.of(((BlockWirelessHeaterTile) world.getBlockEntity(pos)).getEnergy()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getCapacity(AbstractContainerMenu handler) {
        if (handler instanceof BlockWirelessHeaterScreenHandler) {
            BlockPos pos = ((BlockWirelessHeaterScreenHandler) handler).getPos();
            Level world = ((BlockWirelessHeaterScreenHandler) handler).getLevel();
            return pos != null ? Optional.of(((BlockWirelessHeaterTile) world.getBlockEntity(pos)).getCapacity()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }


}
