package ironfurnaces.gui;

import ironfurnaces.container.BlockWirelessHeaterScreenHandler;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockWirelessHeaterTile;
import ironfurnaces.util.StringHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class BlockWirelessHeaterScreen extends AbstractContainerScreen<BlockWirelessHeaterScreenHandler> {

    public static Identifier GUI = net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/heater.png");
    net.minecraft.world.entity.player.Inventory playerInv;
    Component name;
    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;
    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    public final BlockWirelessHeaterScreenHandler handler;

    public BlockWirelessHeaterScreen(BlockWirelessHeaterScreenHandler handler, net.minecraft.world.entity.player.Inventory inv, Component name) {
        super(handler, inv, name);
        this.handler = handler;
        playerInv = inv;
        this.name = name;
    }

    private void blit(net.minecraft.client.gui.GuiGraphicsExtractor extractor, net.minecraft.resources.Identifier texture, int x, int y, int width, int height, float u, float v, float uWidth, float vHeight) {
        extractor.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, 256, 256);
    }

    @Override
    public void extractRenderState(net.minecraft.client.gui.GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
        this.extractBackground(extractor, mouseX, mouseY, partialTick);
        super.extractRenderState(extractor, mouseX, mouseY, partialTick);
        this.extractTooltip(extractor, mouseX, mouseY);
    }

    @Override
    protected void extractLabels(net.minecraft.client.gui.GuiGraphicsExtractor extractor, int mouseX, int mouseY) {
        extractor.text(this.font, this.playerInv.getDisplayName(), 7, this.imageHeight - 93, 4210752, false);
        extractor.text(this.font, name, this.imageWidth / 2 - this.font.width(name) / 2, 6, 4210752, false);

        int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
        int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);
        if(actualMouseX >= 65 && actualMouseX <= 111 && actualMouseY >= 64 && actualMouseY <= 76) {
            double energy = this.getEnergy();
            int capacity = this.getCapacity();
            extractor.setTooltipForNextFrame(this.font, Component.literal(StringHelper.displayEnergy(energy, capacity).get(0)).withStyle(ChatFormatting.GOLD), actualMouseX, actualMouseY);
        }
    }

    @Override
    public void extractContents(net.minecraft.client.gui.GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(extractor, BlockWirelessHeaterScreen.GUI, i, j, this.imageWidth, this.imageHeight, 0.0F, 0.0F, (float)this.imageWidth, (float)this.imageHeight);
        int k;
        k = this.getEnergyScaled(46);
        this.blit(extractor, BlockWirelessHeaterScreen.GUI, i + 65, j + 64, k + 1, 12, 176.0F, 0.0F, (float)(k + 1), 12.0F);
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
