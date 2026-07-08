package ironfurnaces.gui;

import com.google.common.collect.Lists;
import ironfurnaces.IronFurnacesClient;
import ironfurnaces.container.BlockIronFurnaceScreenHandlerBase;
import ironfurnaces.init.Reference;
import ironfurnaces.tileentity.BlockIronFurnaceTileBase;
import ironfurnaces.util.StringHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.network.chat.Component;

import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public abstract class BlockIronFurnaceScreenBase<T extends BlockIronFurnaceScreenHandlerBase>
		extends
			AbstractContainerScreen<T> {

	public static Identifier GUI;
	public static final Identifier WIDGETS = net.minecraft.resources.Identifier.fromNamespaceAndPath(Reference.MOD_ID,
			"textures/gui/widgets.png");
	net.minecraft.world.entity.player.Inventory playerInv;
	Component name;
	/** The X size of the inventory window in pixels. */
	protected int xSize = 176;
	/** The Y size of the inventory window in pixels. */
	protected int ySize = 166;

	public boolean add_button;
	public boolean sub_button;
	public final T handler;

	public BlockIronFurnaceScreenBase(T handler, net.minecraft.world.entity.player.Inventory inv, Component name,
			Identifier gui) {
		super(handler, inv, name);
		this.handler = handler;
		playerInv = inv;
		this.name = name;
		if (!net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("ironfurnaces_augments")) {
			BlockIronFurnaceScreenBase.GUI = Identifier.fromNamespaceAndPath(gui.getNamespace(),
					gui.getPath().replace(".png", "_no_augment.png"));
		} else {
			BlockIronFurnaceScreenBase.GUI = gui;
		}
	}

	@Override
	public void extractRenderState(net.minecraft.client.gui.GuiGraphicsExtractor extractor, int mouseX, int mouseY,
			float partialTick) {
		this.extractBackground(extractor, mouseX, mouseY, partialTick);
		super.extractRenderState(extractor, mouseX, mouseY, partialTick);
		this.extractTooltip(extractor, mouseX, mouseY);
	}

	@Override
	protected void extractLabels(net.minecraft.client.gui.GuiGraphicsExtractor extractor, int mouseX, int mouseY) {
		// drawString(matrices, this.font, "Energy: " + container.getEnergy(), 10, 10,
		// 0xffffff);
		extractor.text(this.font, this.playerInv.getDisplayName(), 7, this.imageHeight - 93, 4210752);
		extractor.text(this.font, name, 7 + this.imageWidth / 2 - this.font.width(name) / 2, 6, 4210752);

		if (showInventoryButtons(handler).get() && getRedstoneMode(handler).get() == 4) {
			int comSub = getComSub(handler).get();
			int i = comSub > 9 ? 28 : 31;
			extractor.text(this.font, comSub + "", i - 42, 90, 4210752);
		}
		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

		this.addTooltips(extractor, actualMouseX, actualMouseY);

	}

	private static Optional<Boolean> getAutoInput(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getAutoInput() == 1)
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Boolean> getAutoOutput(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getAutoOutput() == 1)
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Boolean> showInventoryButtons(AbstractContainerMenu handler) {
		if (!net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("ironfurnaces_automation")) {
			return Optional.of(false);
		}
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getShowButtons() == 1)
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getRedstoneMode(AbstractContainerMenu handler) {
		if (!net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("ironfurnaces_automation")) {
			return Optional.of(0);
		}
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getRedstoneSetting())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getComSub(AbstractContainerMenu handler) {
		if (!net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("ironfurnaces_automation")) {
			return Optional.of(0);
		}
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getRedstoneComSub())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	public static Component getTooltip(BlockIronFurnaceTileBase te, int index) {
		switch (te.furnaceSettings.get(index)) {
			case 1 :
				return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_input");
			case 2 :
				return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_output");
			case 3 :
				return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_input_output");
			case 4 :
				return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_fuel");
			default :
				return Component.translatable("tooltip." + Reference.MOD_ID + ".gui_none");
		}
	}

	private static Optional<Component> getTooltip(AbstractContainerMenu handler, int index) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(getTooltip((BlockIronFurnaceTileBase) world.getBlockEntity(pos), index))
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getSettingTop(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingTop())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getSettingBottom(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingBottom())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getSettingFront(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingFront())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getSettingBack(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingBack())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getSettingLeft(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingLeft())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getSettingRight(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getSettingRight())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getIndexFront(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexFront())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getIndexBack(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexBack())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getIndexLeft(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexLeft())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<Integer> getIndexRight(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getIndexRight())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private static Optional<BlockPos> getBlockPos(AbstractContainerMenu handler) {
		if (handler instanceof BlockIronFurnaceScreenHandlerBase) {
			BlockPos pos = ((BlockIronFurnaceScreenHandlerBase) handler).getPos();
			Level world = ((BlockIronFurnaceScreenHandlerBase) handler).getLevel();
			return pos != null
					? Optional.of(((BlockIronFurnaceTileBase) world.getBlockEntity(pos)).getBlockPos())
					: Optional.empty();
		} else {
			return Optional.empty();
		}
	}

	private void addTooltips(GuiGraphicsExtractor extractor, int mouseX, int mouseY) {

		if (!showInventoryButtons(handler).get()) {
			if (mouseX >= -20 && mouseX <= 0 && mouseY >= 4 && mouseY <= 26) {
				extractor.setTooltipForNextFrame(this.font,
						Component.translatable("tooltip." + Reference.MOD_ID + ".gui_open"), mouseX, mouseY);
			}
		} else {
			if (mouseX >= -13 && mouseX <= 0 && mouseY >= 4 && mouseY <= 26) {
				extractor.setComponentTooltipForNextFrame(this.font, StringHelper.getShiftInfoGui(), mouseX, mouseY);
			} else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 12 && mouseY <= 25) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_auto_input"));
				list.add(Component.literal("" + getAutoInput(handler).get()));
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -29 && mouseX <= -16 && mouseY >= 12 && mouseY <= 25) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_auto_output"));
				list.add(Component.literal("" + getAutoOutput(handler).get()));
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 31 && mouseY <= 40) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_top"));
				list.add(getTooltip(handler, 1).get());
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 55 && mouseY <= 64) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_bottom"));
				list.add(getTooltip(handler, 0).get());
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -32 && mouseX <= -23 && mouseY >= 43 && mouseY <= 52) {
				List<Component> list = Lists.newArrayList();
				if (IronFurnacesClient.isShiftKeyDown()) {
					list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_reset"));
				} else {
					list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_front"));
					list.add(getTooltip(handler, getIndexFront(handler).get()).get());
				}
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -44 && mouseX <= -35 && mouseY >= 43 && mouseY <= 52) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_left"));
				list.add(getTooltip(handler, getIndexLeft(handler).get()).get());
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 43 && mouseY <= 52) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_right"));
				list.add(getTooltip(handler, getIndexRight(handler).get()).get());
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -20 && mouseX <= -11 && mouseY >= 55 && mouseY <= 64) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_back"));
				list.add(getTooltip(handler, getIndexBack(handler).get()).get());
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 70 && mouseY <= 83) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_ignored"));
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -31 && mouseX <= -18 && mouseY >= 70 && mouseY <= 83) {
				List<Component> list = Lists.newArrayList();
				if (IronFurnacesClient.isShiftKeyDown()) {
					list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_low"));
				} else {
					list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_high"));
				}
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -15 && mouseX <= -2 && mouseY >= 70 && mouseY <= 83) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_comparator"));
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			} else if (mouseX >= -47 && mouseX <= -34 && mouseY >= 86 && mouseY <= 99) {
				List<Component> list = Lists.newArrayList();
				list.add(Component.translatable("tooltip." + Reference.MOD_ID + ".gui_redstone_comparator_sub"));
				extractor.setComponentTooltipForNextFrame(this.font, list, mouseX, mouseY);
			}

		}
	}

	private void blit(net.minecraft.client.gui.GuiGraphicsExtractor extractor,
			net.minecraft.resources.Identifier texture, int x, int y, int width, int height, float u, float v,
			float uWidth, float vHeight) {
		extractor.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height,
				256, 256);
	}

	@Override
	public void extractContents(net.minecraft.client.gui.GuiGraphicsExtractor extractor, int mouseX, int mouseY,
			float partialTick) {

		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(extractor, BlockIronFurnaceScreenBase.GUI, i, j, this.imageWidth, this.imageHeight, (float) (0),
				(float) (0), (float) (this.imageWidth), (float) (this.imageHeight));

		int k;
		if (((BlockIronFurnaceScreenHandlerBase) this.handler).isBurning()) {
			k = ((BlockIronFurnaceScreenHandlerBase) this.handler).getFuelProgress();
			this.blit(extractor, BlockIronFurnaceScreenBase.GUI, i + 56, j + 36 + 12 - k, 14, k + 1, (float) (176),
					(float) (12 - k), (float) (14), (float) (k + 1));
		}

		k = ((BlockIronFurnaceScreenHandlerBase) this.handler).getCookProgress();
		this.blit(extractor, BlockIronFurnaceScreenBase.GUI, i + 79, j + 34, k + 1, 16, (float) (176), (float) (14),
				(float) (k + 1), (float) (16));

		int actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		int actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);

		this.addInventoryButtons(extractor, ((BlockIronFurnaceScreenHandlerBase) this.handler), actualMouseX,
				actualMouseY);
		this.addRedstoneButtons(extractor, ((BlockIronFurnaceScreenHandlerBase) this.handler), actualMouseX,
				actualMouseY);

		super.extractContents(extractor, mouseX, mouseY, partialTick);
	}

	private void addRedstoneButtons(GuiGraphicsExtractor extractor, BlockIronFurnaceScreenHandlerBase handler,
			int mouseX, int mouseY) {
		int guiLeft = (this.width - this.imageWidth) / 2;
		int guiTop = (this.height - this.imageHeight) / 2;
		if (showInventoryButtons(handler).get()) {
			this.blitRedstone(extractor);
			if (getRedstoneMode(handler).get() == 4) {
				int comSub = getComSub(handler).get();
				boolean flag = IronFurnacesClient.isShiftKeyDown();
				if (flag) {
					if (comSub > 0) {
						this.sub_button = true;
						if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 99) {
							this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 86, 14, 14,
									(float) (14), (float) (0), (float) (14), (float) (14));
						} else {
							this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 86, 14, 14,
									(float) (0), (float) (0), (float) (14), (float) (14));
						}
					} else {
						this.sub_button = false;
						this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 86, 14, 14,
								(float) (28), (float) (0), (float) (14), (float) (14));
					}

				} else {
					if (comSub < 15) {
						this.add_button = true;
						if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 99) {
							this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 86, 14, 14,
									(float) (14), (float) (14), (float) (14), (float) (14));
						} else {
							this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 86, 14, 14,
									(float) (0), (float) (14), (float) (14), (float) (14));
						}
					} else {
						this.add_button = false;
						this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 86, 14, 14,
								(float) (28), (float) (14), (float) (14), (float) (14));

					}
				}
			}
		}
	}

	private void addInventoryButtons(GuiGraphicsExtractor extractor, BlockIronFurnaceScreenHandlerBase container,
			int mouseX, int mouseY) {
		int guiLeft = (this.width - this.imageWidth) / 2;
		int guiTop = (this.height - this.imageHeight) / 2;
		if (!showInventoryButtons(container).get()) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 4, 23, 26, (float) (0),
					(float) (28), (float) (23), (float) (26));
		} else if (showInventoryButtons(container).get()) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 56, guiTop + 4, 59, 107, (float) (0),
					(float) (54), (float) (59), (float) (107));
			if (mouseX >= -47 && mouseX <= -34 && mouseY >= 12 && mouseY <= 25 || getAutoInput(container).get()) {
				this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 47, guiTop + 12, 14, 14, (float) (0),
						(float) (189), (float) (14), (float) (14));
			}
			if (mouseX >= -29 && mouseX <= -16 && mouseY >= 12 && mouseY <= 25 || getAutoOutput(container).get()) {
				this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 29, guiTop + 12, 14, 14,
						(float) (14), (float) (189), (float) (14), (float) (14));
			}
			this.blitIO(extractor);
		}

	}

	private void blitRedstone(GuiGraphicsExtractor extractor) {
		int guiLeft = (this.width - this.imageWidth) / 2;
		int guiTop = (this.height - this.imageHeight) / 2;
		boolean flag = IronFurnacesClient.isShiftKeyDown();
		if (flag) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 70, 14, 14, (float) (84),
					(float) (189), (float) (14), (float) (14));
		}
		int setting = getRedstoneMode(handler).get();
		if (setting == 0) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 47, guiTop + 70, 14, 14, (float) (28),
					(float) (189), (float) (14), (float) (14));
		} else if (setting == 1 && !flag) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 70, 14, 14, (float) (42),
					(float) (189), (float) (14), (float) (14));
		} else if (setting == 2) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 31, guiTop + 70, 14, 14, (float) (98),
					(float) (189), (float) (14), (float) (14));
		} else if (setting == 3) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 15, guiTop + 70, 14, 14, (float) (56),
					(float) (189), (float) (14), (float) (14));
		} else if (setting == 4) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 47, guiTop + 86, 14, 14, (float) (70),
					(float) (189), (float) (14), (float) (14));
		}

	}

	private void blitIO(GuiGraphicsExtractor extractor) {
		int guiLeft = (this.width - this.imageWidth) / 2;
		int guiTop = (this.height - this.imageHeight) / 2;
		int[] settings = new int[]{0, 0, 0, 0, 0, 0};
		int setting = getSettingTop(handler).get();
		if (setting == 1) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 31, 10, 10, (float) (0),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 2) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 31, 10, 10, (float) (10),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 3) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 31, 10, 10, (float) (20),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 4) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 31, 10, 10, (float) (30),
					(float) (161), (float) (10), (float) (10));
		}
		settings[1] = setting;

		setting = getSettingBottom(handler).get();
		if (setting == 1) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 55, 10, 10, (float) (0),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 2) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 55, 10, 10, (float) (10),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 3) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 55, 10, 10, (float) (20),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 4) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 55, 10, 10, (float) (30),
					(float) (161), (float) (10), (float) (10));
		}
		settings[0] = setting;
		setting = getSettingFront(handler).get();
		if (setting == 1) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 43, 10, 10, (float) (0),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 2) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 43, 10, 10, (float) (10),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 3) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 43, 10, 10, (float) (20),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 4) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 32, guiTop + 43, 10, 10, (float) (30),
					(float) (161), (float) (10), (float) (10));
		}
		settings[getIndexFront(handler).get()] = setting;
		setting = getSettingBack(handler).get();
		if (setting == 1) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 55, 10, 10, (float) (0),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 2) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 55, 10, 10, (float) (10),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 3) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 55, 10, 10, (float) (20),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 4) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 55, 10, 10, (float) (30),
					(float) (161), (float) (10), (float) (10));
		}
		settings[getIndexBack(handler).get()] = setting;
		setting = getSettingLeft(handler).get();
		if (setting == 1) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 44, guiTop + 43, 10, 10, (float) (0),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 2) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 44, guiTop + 43, 10, 10, (float) (10),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 3) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 44, guiTop + 43, 10, 10, (float) (20),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 4) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 44, guiTop + 43, 10, 10, (float) (30),
					(float) (161), (float) (10), (float) (10));
		}
		settings[getIndexLeft(handler).get()] = setting;
		setting = getSettingRight(handler).get();
		if (setting == 1) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 43, 10, 10, (float) (0),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 2) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 43, 10, 10, (float) (10),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 3) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 43, 10, 10, (float) (20),
					(float) (161), (float) (10), (float) (10));
		} else if (setting == 4) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft - 20, guiTop + 43, 10, 10, (float) (30),
					(float) (161), (float) (10), (float) (10));
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
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft + 55, guiTop + 16, 18, 18, (float) (0),
					(float) (171), (float) (18), (float) (18));
		}
		if (output || both) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft + 111, guiTop + 30, 26, 26, (float) (0),
					(float) (203), (float) (26), (float) (26));
		}
		if (fuel) {
			this.blit(extractor, BlockIronFurnaceScreenBase.WIDGETS, guiLeft + 55, guiTop + 52, 18, 18, (float) (18),
					(float) (171), (float) (18), (float) (18));
		}
	}

	@Override
	public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean handled) {
		double mouseX = event.x();
		double mouseY = event.y();
		int button = event.button();
		double actualMouseX = mouseX - ((this.width - this.imageWidth) / 2);
		double actualMouseY = mouseY - ((this.height - this.imageHeight) / 2);
		this.mouseClickedRedstoneButtons(actualMouseX, actualMouseY);
		this.mouseClickedInventoryButtons(button, this.handler, actualMouseX, actualMouseY);
		return super.mouseClicked(event, handled);
	}

	public void sendServer(int index, int value) {
		BlockPos pos = getBlockPos(handler).get();
		if (ironfurnaces.api.AutomationAPI.clientHelper != null) {
			ironfurnaces.api.AutomationAPI.clientHelper.sendSettingsPacket(pos, index, value);
		}
	}

	public void mouseClickedInventoryButtons(int button, BlockIronFurnaceScreenHandlerBase container, double mouseX,
			double mouseY) {
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
					Minecraft.getInstance().getSoundManager()
							.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
				} else {
					sendServer(6, 0);
					Minecraft.getInstance().getSoundManager()
							.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
				}

			} else if (mouseX >= -29 && mouseX <= -16 && mouseY >= 12 && mouseY <= 25) {
				if (!getAutoOutput(container).get()) {
					sendServer(7, 1);
					Minecraft.getInstance().getSoundManager()
							.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
				} else {
					sendServer(7, 0);
					Minecraft.getInstance().getSoundManager()
							.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
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
					Minecraft.getInstance().getSoundManager()
							.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.8F, 0.3F));
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
		Minecraft.getInstance().getSoundManager()
				.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
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
		Minecraft.getInstance().getSoundManager()
				.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.3F, 0.3F));
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
				Minecraft.getInstance().getSoundManager()
						.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.3F, 0.3F));
			}
		}
		if (mouseX >= -31 && mouseX <= -18 && mouseY >= 86 && mouseY <= 99) {
			if (this.add_button && !IronFurnacesClient.isShiftKeyDown()) {
				sendServer(9, getComSub(handler).get() + 1);
				Minecraft.getInstance().getSoundManager()
						.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
			}
		}
		if (mouseX >= -47 && mouseX <= -34 && mouseY >= 70 && mouseY <= 83) {
			if (getRedstoneMode(handler).get() != 0) {
				sendServer(8, 0);
				Minecraft.getInstance().getSoundManager()
						.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
			}
		}

		if (mouseX >= -31 && mouseX <= -18 && mouseY >= 70 && mouseY <= 83) {
			if (getRedstoneMode(handler).get() != 1 && !IronFurnacesClient.isShiftKeyDown()) {
				sendServer(8, 1);
				Minecraft.getInstance().getSoundManager()
						.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
			}
			if (getRedstoneMode(handler).get() != 2 && IronFurnacesClient.isShiftKeyDown()) {
				sendServer(8, 2);
				Minecraft.getInstance().getSoundManager()
						.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
			}
		}

		if (mouseX >= -15 && mouseX <= -2 && mouseY >= 70 && mouseY <= 83) {
			if (getRedstoneMode(handler).get() != 3) {
				sendServer(8, 3);
				Minecraft.getInstance().getSoundManager()
						.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
			}
		}

		if (mouseX >= -47 && mouseX <= -34 && mouseY >= 86 && mouseY <= 99) {
			if (getRedstoneMode(handler).get() != 4) {
				sendServer(8, 4);
				Minecraft.getInstance().getSoundManager()
						.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 0.6F, 0.3F));
			}
		}
	}

}
