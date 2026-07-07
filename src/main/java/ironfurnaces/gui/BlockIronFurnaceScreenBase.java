package ironfurnaces.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import ironfurnaces.IronFurnaces;
import ironfurnaces.IronFurnacesClient;
import ironfurnaces.container.BlockIronFurnaceScreenHandlerBase;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import ironfurnaces.util.StringHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public abstract class BlockIronFurnaceScreenBase<T extends BlockIronFurnaceScreenHandlerBase> extends AbstractContainerScreen<T> {

    public static ResourceLocation GUI;
    public static final ResourceLocation WIDGETS = net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/gui/widgets.png");
    net.minecraft.world.entity.player.Inventory playerInv;
    Component name;
    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;
    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;

    public boolean add_button;
    public boolean sub_button;

    public BlockIronFurnaceScreenBase(T handler, net.minecraft.world.entity.player.Inventory inv, Component name, ResourceLocation gui) {
        super(handler, inv, name);
        playerInv = inv;
        this.name = name;
        this.GUI = gui;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        //drawString(matrices, this.font, "Energy: " + container.getEnergy(), 10, 10, 0xffffff);
        guiGraphics.drawString(this.font, this.playerInv.getDisplayName(), 7, this.imageHeight - 93, 4210752);
        guiGraphics.drawString(this.font, name, 7 + this.imageWidth / 2 - this.font.width(name) / 2, 6, 4210752);

        if (showInventoryButtons(handler).get() && getRedstoneMode(handler).get() == 4) {
            int comSub = getComSub(handler).get();
            int i = comSub > 9 ? 28 : 31;
            guiGraphics.drawString(this.font, comSub + "", i - 42, 90, 4210752);
        }
        int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
        int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

        this.addTooltips(guiGraphics, actualMouseX, actualMouseY);

    }

    private static Optional<Boolean> getAutoInput(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getAutoInput() == 1) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Boolean> getAutoOutput(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getAutoOutput() == 1) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Boolean> showInventoryButtons(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getShowButtons() == 1) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getRedstoneMode(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getRedstoneSetting()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getComSub(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getRedstoneComSub()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    public static Component getTooltip(BlockIronFurnaceTileBase te, int index)
    {
        switch (te.furnaceSettings.get(index))
        {
            case 1:
                return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_input");
            case 2:
                return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_output");
            case 3:
                return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_input_output");
            case 4:
                return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_fuel");
            default:
                return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_none");
        }
    }

    private static Optional<Component> getTooltip(AbstractContainerMenu handler, int index) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(getTooltip((BlockIronFurnaceTileBase) world.getBlockEntity(pos), index)) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getSettingTop(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingTop()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getSettingBottom(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingBottom()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getSettingFront(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingFront()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getSettingBack(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingBack()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getSettingLeft(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingLeft()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getSettingRight(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingRight()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getIndexFront(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexFront()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getIndexBack(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexBack()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getIndexLeft(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexLeft()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Integer> getIndexRight(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexRight()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private static Optional<BlockPos> getBlockPos(AbstractContainerMenu handler) {
        if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
            BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
            Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
            return pos != null ? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getPos()) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private void addTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {

        if (!showInventoryButtons(handler).get()) {
            if (mouseX >= -20 && mouseX <= 0 && mouseY >= 4 && mouseY <= 26) {
                guiGraphics.renderTooltip(this.font, Component.translatable("tooltip." + Reference.MOD_ID + ".gui_open"), mouseX, mouseY);
            }
        } else {
            if (mouseX >= -13 && mouseX <= 0 && mouseY >= 4 && mouseY <= 26) {
                guiGraphics.renderTooltip(this.font, StringHelper.getShiftInfoGui(), mouseX, mouseY);
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 12 && mouseY <= 25) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_auto_input"));
                list.add(Component.literal("" + getAutoInput(handler).get()));
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -29 && mouseX <= -16 && mouseY >= 12 && mouseY <= 25) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_auto_output"));
                list.add(Component.literal("" + getAutoOutput(handler).get()));
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 31 && mouseY <= 40) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_top"));
                list.add(getTooltip(handler, 1).get());
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 55 && mouseY <= 64) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_bottom"));
                list.add(getTooltip(handler, 0).get());
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 43 && mouseY <= 52) {
                List<Component> list = Lists.newArrayList();
                if (IronFurnacesClient.isShiftKeyDown()) {
                    list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_reset"));
                } else {
                    list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_front"));
                    list.add(getTooltip(handler, getIndexFront(handler).get()).get());
                }
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -44 && mouseX <= -35 && mouseY >= 43 && mouseY <= 52) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_left"));
                list.add(getTooltip(handler, getIndexLeft(handler).get()).get());
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 43 && mouseY <= 52) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_right"));
                list.add(getTooltip(handler, getIndexRight(handler).get()).get());
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 55 && mouseY <= 64) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_back"));
                list.add(getTooltip(handler, getIndexBack(handler).get()).get());
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 70 && mouseY <= 83) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_ignored"));
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 70 && mouseY <= 83) {
                List<Component> list = Lists.newArrayList();
                if (IronFurnacesClient.isShiftKeyDown()) {
                    list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_low"));
                } else {
                    list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_high"));
                }
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -15 && mouseX <= -2 && mouseY >= 70 && mouseY <= 83) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_comparator"));
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 86 && mouseY <= 99) {
                List<Component> list = Lists.newArrayList();
                list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_comparator_sub"));
                guiGraphics.renderComponentTooltip(this.font, list, mouseX, mouseY);
            }

        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.GUI, i, j, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int k;
        if (((BlockIronFurnaceScreenHandlerBase)this.handler).isBurning()) {
            k = ((BlockIronFurnaceScreenHandlerBase)this.handler).getFuelProgress();
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.GUI, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1, 256, 256);
        }

        k = ((BlockIronFurnaceScreenHandlerBase)this.handler).getCookProgress();
        guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.GUI, i + 79, j + 34, 176, 14, k + 1, 16, 256, 256);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
        int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

        this.addInventoryButtons(guiGraphics, ((BlockIronFurnaceScreenHandlerBase) this.handler), actualMouseX, actualMouseY);
        this.addRedstoneButtons(guiGraphics, ((BlockIronFurnaceScreenHandlerBase) this.handler), actualMouseX, actualMouseY);

    }

    private void addRedstoneButtons(GuiGraphics guiGraphics, BlockIronFurnaceScreenHandlerBase handler, int mouseX, int mouseY) {
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        if (showInventoryButtons(handler).get()) {
            this.blitRedstone(guiGraphics);
            if (getRedstoneMode(handler).get() == 4) {
                int comSub = getComSub(handler).get();
                boolean flag = IronFurnacesClient.isShiftKeyDown();
                if (flag) {
                    if (comSub > 0) {
                        this.sub_button = true;
                        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 99) {
                            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 86, 14, 0, 14, 14, 256, 256);
                        } else {
                            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 86, 0, 0, 14, 14, 256, 256);
                        }
                    } else {
                        this.sub_button = false;
                        guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 86, 28, 0, 14, 14, 256, 256);
                    }

                } else {
                    if (comSub < 15) {
                        this.add_button = true;
                        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 99) {
                            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 86, 14, 14, 14, 14, 256, 256);
                        } else {
                            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 86, 0, 14, 14, 14, 256, 256);
                        }
                    } else {
                        this.add_button = false;
                        guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 86, 28, 14, 14, 14, 256, 256);

                    }
                }
            }
        }
    }

    private void addInventoryButtons(GuiGraphics guiGraphics, BlockIronFurnaceScreenHandlerBase container, int mouseX, int mouseY) {
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        if (!showInventoryButtons(container).get()) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 4, 0, 28, 23, 26, 256, 256);
        } else if (showInventoryButtons(container).get()) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 56, guiTop + 4, 0, 54, 59, 107, 256, 256);
            if (mouseX >= -47 && mouseX <= -34 && mouseY >= 12 && mouseY <= 25 || getAutoInput(container).get()) {
                guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 47, guiTop + 12, 0, 189, 14, 14, 256, 256);
            }
            if (mouseX >= -29 && mouseX <= -16 && mouseY >= 12 && mouseY <= 25 || getAutoOutput(container).get()) {
                guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 29, guiTop + 12, 14, 189, 14, 14, 256, 256);
            }
            this.blitIO(guiGraphics);
        }


    }

    private void blitRedstone(GuiGraphics guiGraphics) {
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        boolean flag = IronFurnacesClient.isShiftKeyDown();
        if (flag) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 70, 84, 189, 14, 14, 256, 256);
        }
        int setting = getRedstoneMode(handler).get();
        if (setting == 0) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 47, guiTop + 70, 28, 189, 14, 14, 256, 256);
        } else if (setting == 1 && !flag) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 70, 42, 189, 14, 14, 256, 256);
        } else if (setting == 2) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 31, guiTop + 70, 98, 189, 14, 14, 256, 256);
        } else if (setting == 3) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 15, guiTop + 70, 56, 189, 14, 14, 256, 256);
        } else if (setting == 4) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 47, guiTop + 86, 70, 189, 14, 14, 256, 256);
        }

    }

    private void blitIO(GuiGraphics guiGraphics) {
        int guiLeft = (this.width - this.imageWidth) / 2;
        int guiTop = (this.height - this.imageHeight) / 2;
        int[] settings = new int[]{0, 0, 0, 0, 0, 0};
        int setting = getSettingTop(handler).get();
        if (setting == 1) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 31, 0, 161, 10, 10, 256, 256);
        } else if (setting == 2) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 31, 10, 161, 10, 10, 256, 256);
        } else if (setting == 3) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 31, 20, 161, 10, 10, 256, 256);
        } else if (setting == 4) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 31, 30, 161, 10, 10, 256, 256);
        }
        settings[1] = setting;

        setting = getSettingBottom(handler).get();
        if (setting == 1) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 55, 0, 161, 10, 10, 256, 256);
        } else if (setting == 2) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 55, 10, 161, 10, 10, 256, 256);
        } else if (setting == 3) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 55, 20, 161, 10, 10, 256, 256);
        } else if (setting == 4) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 55, 30, 161, 10, 10, 256, 256);
        }
        settings[0] = setting;
        setting = getSettingFront(handler).get();
        if (setting == 1) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 43, 0, 161, 10, 10, 256, 256);
        } else if (setting == 2) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 43, 10, 161, 10, 10, 256, 256);
        } else if (setting == 3) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 43, 20, 161, 10, 10, 256, 256);
        } else if (setting == 4) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 32, guiTop + 43, 30, 161, 10, 10, 256, 256);
        }
        settings[getIndexFront(handler).get()] = setting;
        setting = getSettingBack(handler).get();
        if (setting == 1) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 55, 0, 161, 10, 10, 256, 256);
        } else if (setting == 2) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 55, 10, 161, 10, 10, 256, 256);
        } else if (setting == 3) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 55, 20, 161, 10, 10, 256, 256);
        } else if (setting == 4) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 55, 30, 161, 10, 10, 256, 256);
        }
        settings[getIndexBack(handler).get()] = setting;
        setting = getSettingLeft(handler).get();
        if (setting == 1) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 44, guiTop + 43, 0, 161, 10, 10, 256, 256);
        } else if (setting == 2) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 44, guiTop + 43, 10, 161, 10, 10, 256, 256);
        } else if (setting == 3) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 44, guiTop + 43, 20, 161, 10, 10, 256, 256);
        } else if (setting == 4) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 44, guiTop + 43, 30, 161, 10, 10, 256, 256);
        }
        settings[getIndexLeft(handler).get()] = setting;
        setting = getSettingRight(handler).get();
        if (setting == 1) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 43, 0, 161, 10, 10, 256, 256);
        } else if (setting == 2) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 43, 10, 161, 10, 10, 256, 256);
        } else if (setting == 3) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 43, 20, 161, 10, 10, 256, 256);
        } else if (setting == 4) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft - 20, guiTop + 43, 30, 161, 10, 10, 256, 256);
        }
        settings[getIndexRight(handler).get()] = setting;
        boolean input = false;
        boolean output = false;
        boolean both = false;
        boolean fuel = false;
        for (int set : settings) {
            if (set == 1) {
                input = true;
            } else if (set == 2) {
                output = true;
            } else if (set == 3) {
                both = true;
            } else if (set == 4) {
                fuel = true;
            }
        }
        if (input || both) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft + 55, guiTop + 16, 0, 171, 18, 18, 256, 256);
        }
        if (output || both) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft + 111, guiTop + 30, 0, 203, 26, 26, 256, 256);
        }
        if (fuel) {
            guiGraphics.blit(net.minecraft.client.gui.screens.Screen::applyBlitOffset, this.WIDGETS, guiLeft + 55, guiTop + 52, 18, 171, 18, 18, 256, 256);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
        double actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);
        this.mouseClickedRedstoneButtons(actualMouseX, actualMouseY);
        this.mouseClickedInventoryButtons(button, this.handler, actualMouseX, actualMouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void sendServer(int index, int value)
    {
        BlockPos pos = getBlockPos(handler).get();
        net.minecraft.network.FriendlyByteBuf buf = new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(index);
        buf.writeInt(value);

        ClientPlayNetworking.send(IronFurnaces.furnace_packet, buf);
    }

    public void mouseClickedInventoryButtons(int button, BlockIronFurnaceScreenHandlerBase container, double mouseX, double mouseY) {
        boolean flag = button == GLFW.GLFW_MOUSE_BUTTON_2;
        if (!showInventoryButtons(container).get()) {
            if (mouseX >= -20 && mouseX <= 0 && mouseY >= 4 && mouseY <= 26) {
                sendServer(10, 1);
            }
        } else {
            if (mouseX >= -13 && mouseX <= 0 && mouseY >= 4 && mouseY <= 26) {
                sendServer(10, 0);
            } else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 12 && mouseY <= 25) {
                if (!getAutoInput(container).get()) {
                    sendServer(6, 1);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                } else {
                    sendServer(6, 0);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }

            } else if (mouseX >= -29 && mouseX <= -16 && mouseY >= 12 && mouseY <= 25) {
                if (!getAutoOutput(container).get()) {
                    sendServer(7, 1);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                } else {
                    sendServer(7, 0);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
                }
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 31 && mouseY <= 40) {
                if (flag) {
                    sendToServerInverted(getSettingTop(container).get(), 1);
                } else {
                    sendToServer(getSettingTop(container).get(), 1);
                }
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 55 && mouseY <= 64) {
                if (flag) {
                    sendToServerInverted(getSettingBottom(container).get(), 0);
                } else {
                    sendToServer(getSettingBottom(container).get(), 0);
                }
            } else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 43 && mouseY <= 52) {
                if (IronFurnacesClient.isShiftKeyDown()) {
                    sendServer(0, 0);
                    sendServer(1, 0);
                    sendServer(2, 0);
                    sendServer(3, 0);
                    sendServer(4, 0);
                    sendServer(5, 0);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.8F, 0.3F));
                } else {
                    if (flag) {
                        sendToServerInverted(getSettingFront(container).get(), getIndexFront(container).get());
                    } else {
                        sendToServer(getSettingFront(container).get(), getIndexFront(container).get());
                    }
                }
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 55 && mouseY <= 64) {
                if (flag) {
                    sendToServerInverted(getSettingBack(container).get(), getIndexBack(container).get());
                } else {
                    sendToServer(getSettingBack(container).get(), getIndexBack(container).get());
                }
            } else if (mouseX >= -44 && mouseX <= -35 && mouseY >= 43 && mouseY <= 52) {
                if (flag) {
                    sendToServerInverted(getSettingLeft(container).get(), getIndexLeft(container).get());
                } else {
                    sendToServer(getSettingLeft(container).get(), getIndexLeft(container).get());
                }
            } else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 43 && mouseY <= 52) {
                if (flag) {
                    sendToServerInverted(getSettingRight(container).get(), getIndexRight(container).get());
                } else {
                    sendToServer(getSettingRight(container).get(), getIndexRight(container).get());
                }
            }
        }
    }

    private void sendToServer(int setting, int index) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
        if (setting <= 0) {
            sendServer(index, 1);
        } else if (setting == 1) {
            sendServer(index, 2);
        } else if (setting == 2) {
            sendServer(index, 3);
        } else if (setting == 3) {
            sendServer(index, 4);
        } else if (setting >= 4) {
            sendServer(index, 0);
        }
    }

    private void sendToServerInverted(int setting, int index) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
        if (setting <= 0) {
            sendServer(index, 4);
        } else if (setting == 1) {
            sendServer(index, 0);
        } else if (setting == 2) {
            sendServer(index, 1);
        } else if (setting == 3) {
            sendServer(index, 2);
        } else if (setting >= 4) {
            sendServer(index, 3);
        }
    }

    public void mouseClickedRedstoneButtons(double mouseX, double mouseY) {
        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 99) {
            if (this.sub_button && IronFurnacesClient.isShiftKeyDown()) {
                sendServer(9, getComSub(handler).get() - 1);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.3F, 0.3F));
            }
        }
        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 99) {
            if (this.add_button && !IronFurnacesClient.isShiftKeyDown()) {
                sendServer(9, getComSub(handler).get() + 1);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
            }
        }
        if (mouseX >= -47 && mouseX <= -34 && mouseY >= 70 && mouseY <= 83) {
            if (getRedstoneMode(handler).get() != 0) {
                sendServer(8, 0);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
            }
        }

        if (mouseX >= -31 && mouseX <= -18 && mouseY >= 70 && mouseY <= 83) {
            if (getRedstoneMode(handler).get() != 1 && !IronFurnacesClient.isShiftKeyDown()) {
                sendServer(8, 1);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
            }
            if (getRedstoneMode(handler).get() != 2 && IronFurnacesClient.isShiftKeyDown()) {
                sendServer(8, 2);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
            }
        }

        if (mouseX >= -15 && mouseX <= -2 && mouseY >= 70 && mouseY <= 83) {
            if (getRedstoneMode(handler).get() != 3) {
                sendServer(8, 3);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
            }
        }

        if (mouseX >= -47 && mouseX <= -34 && mouseY >= 86 && mouseY <= 99) {
            if (getRedstoneMode(handler).get() != 4) {
                sendServer(8, 4);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.6F, 0.3F));
            }
        }
    }

}
