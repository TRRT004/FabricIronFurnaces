package ironfurnaces.tileentity;

import com.google.common.collect.Lists;
import ironfurnaces.blocks.BlockIronFurnaceBase;
import ironfurnaces.config.FurnaceSettings;
import ironfurnaces.config.IronFurnacesConfig;
import ironfurnaces.init.Reference;
import ironfurnaces.items.*;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldlyContainerHolder;




import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;


import java.util.*;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.core.HolderLookup;

public abstract class BlockIronFurnaceTileBase extends TileEntityInventory implements net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider<BlockPos>, StackedContentsCompatible {
    public static final int INPUT = 0;
    public static final int FUEL = 1;
    public static final int OUTPUT = 2;

    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    private int timer;
    private int currentAugment = 0; // 0 == none 1 == Blasting 2 == Smoking 3 == Speed 4 == Fuel
    /**
     * The number of ticks that the furnace will keep burning
     */
    private int furnaceBurnTime;
    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime = this.getCookTime();
    private final Object2IntOpenHashMap<Identifier> recipesUsed;
    protected final ContainerData propertyDelegate;
    public int jovial;
    public Random rand = new Random();

    protected RecipeType<? extends AbstractCookingRecipe> recipeType;

    public FurnaceSettings furnaceSettings;
    public boolean placedConfig = false;


    public BlockIronFurnaceTileBase(BlockEntityType<?> tileentitytypeIn, BlockPos pos, BlockState state) {
        super(tileentitytypeIn, pos, state, 4);
        furnaceSettings = new FurnaceSettings() {
            @Override
            public void onChanged() {
                setChanged();
            }
        };
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return BlockIronFurnaceTileBase.this.furnaceBurnTime;
                    case 1:
                        return BlockIronFurnaceTileBase.this.currentItemBurnTime;
                    case 2:
                        return BlockIronFurnaceTileBase.this.cookTime;
                    case 3:
                        return BlockIronFurnaceTileBase.this.totalCookTime;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        BlockIronFurnaceTileBase.this.furnaceBurnTime = value;
                        break;
                    case 1:
                        BlockIronFurnaceTileBase.this.currentItemBurnTime = value;
                        break;
                    case 2:
                        BlockIronFurnaceTileBase.this.cookTime = value;
                        break;
                    case 3:
                        BlockIronFurnaceTileBase.this.totalCookTime = value;
                }

            }

            public int getCount() {
                return 4;
            }
        };
        this.recipesUsed = new Object2IntOpenHashMap();
        this.recipeType = RecipeType.SMELTING;
    }

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockStateProperties.LIT) != furnaceBurnTime > 0) {
            level.setBlockAndUpdate(worldPosition, state.setValue(BlockStateProperties.LIT, furnaceBurnTime > 0));
        }
        if (state.getValue(BlockIronFurnaceBase.TYPE) != getStateType()) {
            level.setBlockAndUpdate(worldPosition, state.setValue(BlockIronFurnaceBase.TYPE, getStateType()));
        }
        if (state.getValue(BlockIronFurnaceBase.JOVIAL) != jovial) {
            level.setBlockAndUpdate(worldPosition, state.setValue(BlockIronFurnaceBase.JOVIAL, jovial));
        }
    }

    protected int getCookTime() {
        ItemStack stack = this.getItem(3);
        if (!stack.isEmpty()) {
            if (stack.is(Reference.SPEED_AUGMENT_TAG) || stack.is(Reference.BLASTING_AUGMENT_TAG) || stack.is(Reference.SMOKING_AUGMENT_TAG)) {
                return getCookTimeConfig() / 2;
            }
            if (stack.is(Reference.FUEL_AUGMENT_TAG)) {
                return (int) (getCookTimeConfig() * 1.25);
            }
        }
        return getCookTimeConfig();
    }

    protected int getCookTimeConfig() {
        return 200;
    }

    private int getAugment(ItemStack stack) {
        if (stack.is(Reference.BLASTING_AUGMENT_TAG)) {
            return 1;
        } else if (stack.is(Reference.SMOKING_AUGMENT_TAG)) {
            return 2;
        } else if (stack.is(Reference.SPEED_AUGMENT_TAG)) {
            return 3;
        } else if (stack.is(Reference.FUEL_AUGMENT_TAG)) {
            return 4;
        }
        return 0;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, BlockIronFurnaceTileBase e) {
        if (e.furnaceSettings.size() <= 0) {
            e.furnaceSettings = new FurnaceSettings() {
                @Override
                public void onChanged() {
                    e.setChanged();
                }
            };
        }
        boolean flag1 = false;
        if (e.currentAugment != e.getAugment(e.getItem(3))) {
            e.currentAugment = e.getAugment(e.getItem(3));
            e.furnaceBurnTime = 0;
        }
        if (e.isBurning()) {
            --e.furnaceBurnTime;
        }

        if (!e.level.isClientSide()) {
            e.timer++;
            if (e.cookTime <= 0) {
                e.autoIO();
                flag1 = true;
            }
            if (e.totalCookTime != e.getCookTime()) {
                e.totalCookTime = e.getCookTime();
            }
            int mode = e.getRedstoneSetting();
            if (mode != 0) {
                if (mode == 2) {
                    int i = 0;
                    for (Direction side : Direction.values()) {
                        if (world.getSignal(pos.relative(side), side) > 0) {
                            i++;
                        }
                    }
                    if (i != 0) {
                        e.cookTime = 0;
                        e.furnaceBurnTime = 0;
                        e.forceUpdateAllStates();
                        return;
                    }
                }
                if (mode == 1) {
                    boolean flag = false;
                    for (Direction side : Direction.values()) {

                        if (world.getSignal(pos.relative(side), side) > 0) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        e.cookTime = 0;
                        e.furnaceBurnTime = 0;
                        e.forceUpdateAllStates();
                        return;
                    }
                }
                for (int i = 0; i < Direction.values().length; i++)
                    e.provides[i] = e.getBlockState().getDirectSignal(world, pos, Direction.from3DDataValue(i));

            } else {
                for (int i = 0; i < Direction.values().length; i++)
                    e.provides[i] = 0;
            }
            if (e.doesNeedUpdateSend()) {
                e.onUpdateSent();
            }
            if (!e.getItem(3).isEmpty()) {
                if (e.getItem(3).is(Reference.BLASTING_AUGMENT_TAG)) {
                    if (e.recipeType != RecipeType.BLASTING) {
                        e.recipeType = RecipeType.BLASTING;
                    }
                } else if (e.getItem(3).is(Reference.SMOKING_AUGMENT_TAG)) {
                    if (e.recipeType != RecipeType.SMOKING) {
                        e.recipeType = RecipeType.SMOKING;
                    }
                }
            } else {
                if (e.recipeType != RecipeType.SMELTING) {
                    e.recipeType = RecipeType.SMELTING;
                }
            }
            ItemStack itemstack = e.inventory.get(1);
            if (e.isBurning() || !itemstack.isEmpty() && !e.inventory.get(0).isEmpty()) {
                RecipeHolder<?> irecipe = e.level.getServer().getRecipeManager().getRecipeFor(e.recipeType, new net.minecraft.world.item.crafting.SingleRecipeInput(e.getItem(0)), e.level).orElse(null);
                if (!e.isBurning() && e.canSmelt(irecipe)) {
                    if (itemstack.getItem() instanceof ironfurnaces.api.IHeaterItem) {
                        CompoundTag tag = itemstack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
                        Optional<Integer> xOpt = tag.getInt("X");
                        Optional<Integer> yOpt = tag.getInt("Y");
                        Optional<Integer> zOpt = tag.getInt("Z");
                        if (xOpt.isPresent() && yOpt.isPresent() && zOpt.isPresent()) {
                            int x = xOpt.get();
                            int y = yOpt.get();
                            int z = zOpt.get();
                            BlockEntity te = world.getBlockEntity(new BlockPos(x, y, z));
                            if (te != null && te instanceof ironfurnaces.api.IHeaterSource) {
                                double energy = ((ironfurnaces.api.IHeaterSource)te).getEnergy();
                                if (energy >= IronFurnacesConfig.energy_usage) {
                                    ((ironfurnaces.api.IHeaterSource)te).extractEnergy(IronFurnacesConfig.energy_usage);
                                    int fuel = (getFuelTime(world, new ItemStack(Items.COAL)) / 8);
                                    if (!e.getItem(3).isEmpty() && e.getItem(3).is(Reference.FUEL_AUGMENT_TAG)) {
                                        e.furnaceBurnTime = (fuel * 2) * e.getCookTime() / 200;
                                    } else if (!e.getItem(3).isEmpty() && e.getItem(3).is(Reference.SPEED_AUGMENT_TAG)) {
                                        e.furnaceBurnTime = (fuel / 2) * e.getCookTime() / 200;
                                    } else {
                                        e.furnaceBurnTime = fuel * e.getCookTime() / 200;
                                    }
                                    e.currentItemBurnTime = e.furnaceBurnTime;
                                }
                            }
                        }
                    } else {
                        if (!e.getItem(3).isEmpty() && e.getItem(3).is(Reference.FUEL_AUGMENT_TAG)) {
                            e.furnaceBurnTime = 2 * (getFuelTime(world, itemstack)) * e.getCookTime() / 200;
                        } else if (!e.getItem(3).isEmpty() && e.getItem(3).is(Reference.SPEED_AUGMENT_TAG)) {
                            e.furnaceBurnTime = (getFuelTime(world, itemstack) / 2) * e.getCookTime() / 200;
                        } else {
                            e.furnaceBurnTime = getFuelTime(world, itemstack) * e.getCookTime() / 200;
                        }
                    }
                    e.currentItemBurnTime = e.furnaceBurnTime;
                    if (e.isBurning()) {
                        flag1 = true;
                        if (!(itemstack.getItem() instanceof ironfurnaces.api.IHeaterItem)) {
                            if (!itemstack.isEmpty()) {
                                Item item = itemstack.getItem();
                                itemstack.shrink(1);
                                if (itemstack.isEmpty()) {
                                    net.minecraft.world.item.ItemStackTemplate remainder = item.getCraftingRemainder();
                                    e.inventory.set(1, remainder.count() == 0 ? ItemStack.EMPTY : remainder.create());
                                }
                            }
                        }
                    }
                }

                if (e.isBurning() && e.canSmelt(irecipe)) {
                    ++e.cookTime;
                    if (e.cookTime >= e.totalCookTime) {
                        e.cookTime = 0;
                        e.totalCookTime = e.getCookTime();
                        e.smeltItem(irecipe);
                        flag1 = true;
                    }
                } else {
                    e.cookTime = 0;
                }
            } else if (!e.isBurning() && e.cookTime > 0) {
                e.cookTime = Mth.clamp(e.cookTime - 2, 0, e.totalCookTime);
            }
            if (e.timer % 24 == 0) {
                BlockState state2 = world.getBlockState(pos);
                if (state2.getValue(BlockStateProperties.LIT) != e.furnaceBurnTime > 0) {
                    world.setBlock(pos, state2.setValue(BlockStateProperties.LIT, e.furnaceBurnTime > 0), 3);
                }
                if (state2.getValue(BlockIronFurnaceBase.TYPE) != e.getStateType()) {
                    world.setBlock(pos, state2.setValue(BlockIronFurnaceBase.TYPE, e.getStateType()), 3);
                }
                if (state2.getValue(BlockIronFurnaceBase.JOVIAL) != e.jovial) {
                    world.setBlock(pos, state2.setValue(BlockIronFurnaceBase.JOVIAL, e.jovial), 3);
                }
            }

        }

        if (flag1) {
            e.setChanged();
        }
    }

    private int getStateType() {
        if (this.getItem(3).is(Reference.SMOKING_AUGMENT_TAG)) {
            return 1;
        } else if (this.getItem(3).is(Reference.BLASTING_AUGMENT_TAG)) {
            return 2;
        } else {
            return 0;
        }
    }

    public void autoIO() {
        Level world = this.level;
        for (Direction dir : Direction.values()) {
            BlockEntity tile = world.getBlockEntity(this.worldPosition.relative(dir));
            if (tile == null) {
                continue;
            }
            if (this.furnaceSettings.get(dir.get3DDataValue()) == 1 || this.furnaceSettings.get(dir.get3DDataValue()) == 2 || this.furnaceSettings.get(dir.get3DDataValue()) == 3 || this.furnaceSettings.get(dir.get3DDataValue()) == 4) {
                if (tile != null) {


                    if (this.getAutoInput() != 0 || this.getAutoOutput() != 0) {
                        if (this.getAutoInput() == 1) {
                            if (this.furnaceSettings.get(dir.get3DDataValue()) == 1 || this.furnaceSettings.get(dir.get3DDataValue()) == 3) {
                                if (this.getItem(INPUT).getCount() >= this.getMaxStackSize()) {
                                    continue;
                                }
                                Container inv = getInventoryAt(world, this.worldPosition.relative(dir));
                                if (inv != null) {

                                    for (int i = 0; i < inv.getContainerSize(); i++) {
                                        if (inv.getItem(i).isEmpty()) {
                                            continue;
                                        }
                                        if (canExtract(inv, inv.getItem(i), i, dir.getOpposite()))
                                        {
                                            ItemStack stack = transfer(inv, i, this, inv.getItem(i), INPUT, dir);
                                            if (isItemFuel(world, stack) && this.getItem(INPUT).isEmpty() || canMergeItems(this.getItem(INPUT), stack)) {
                                                insertItemInternal(INPUT, stack, false);
                                            }
                                        }

                                    }

                                }
                            }
                            if (this.furnaceSettings.get(dir.get3DDataValue()) == 4) {
                                if (this.getItem(FUEL).getCount() >= this.getMaxStackSize()) {
                                    continue;
                                }
                                Container inv = getInventoryAt(world, this.worldPosition.relative(dir));
                                if (inv != null) {
                                    for (int i = 0; i < inv.getContainerSize(); i++) {
                                        if (inv.getItem(i).isEmpty()) {
                                            continue;
                                        }
                                        if (canExtract(inv, inv.getItem(i), i, dir.getOpposite()))
                                        {
                                            ItemStack stack = transfer(inv, i, this, inv.getItem(i), FUEL, dir);
                                            if (isItemFuel(world, stack) && this.getItem(FUEL).isEmpty() || canMergeItems(this.getItem(FUEL), stack)) {
                                                insertItemInternal(FUEL, stack, false);
                                            }
                                        }

                                    }

                                }
                            }
                        }
                        if (this.getAutoOutput() == 1) {

                            if (this.furnaceSettings.get(dir.get3DDataValue()) == 4) {
                                if (this.getItem(FUEL).isEmpty()) {
                                    continue;
                                }
                                ItemStack stack = extractItemInternal(FUEL, 1, true);
                                if (stack.getItem() != Items.BUCKET) {
                                    continue;
                                }
                                Container inv = getInventoryAt(world, this.worldPosition.relative(dir));
                                if (inv != null) {
                                    for (int i = 0; i < inv.getContainerSize(); i++) {
                                        if (inv.canPlaceItem(i, stack) && (inv.getItem(i).isEmpty() || (canMergeItems(inv.getItem(i), stack) && inv.getItem(i).getCount() + stack.getCount() <= inv.getMaxStackSize()))) {
                                            transfer(null, 0, inv, extractItemInternal(FUEL, stack.getCount(), false), i, dir);
                                        }
                                    }

                                }
                            }

                            if (this.furnaceSettings.get(dir.get3DDataValue()) == 2 || this.furnaceSettings.get(dir.get3DDataValue()) == 3) {
                                if (this.getItem(OUTPUT).isEmpty()) {
                                    continue;
                                }
                                Container inv = getInventoryAt(world, this.worldPosition.relative(dir));
                                if (inv != null) {
                                    for (int i = 0; i < inv.getContainerSize(); i++) {
                                        ItemStack stack = extractItemInternal(OUTPUT, 64 - inv.getItem(i).getCount(), true);
                                        if (inv.canPlaceItem(i, stack) && (inv.getItem(i).isEmpty() || (canMergeItems(inv.getItem(i), stack) && inv.getItem(i).getCount() + stack.getCount() <= inv.getMaxStackSize()))) {
                                            transfer(null, 0, inv, extractItemInternal(OUTPUT, 64 - inv.getItem(i).getCount(), false), i, dir);
                                        }
                                    }

                                }


                            }

                        }
                    }
                }
            }
        }
    }


    public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!canPlaceItem(slot, stack))
            return stack;

        ItemStack existing = this.inventory.get(slot);

        int limit = stack.getMaxStackSize();

        if (!existing.isEmpty()) {
            if (!canMergeItems(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.inventory.set(slot, reachedLimit ? new ItemStack(stack.getItem(), limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            this.setChanged();
        }

        return reachedLimit ? new ItemStack(stack.getItem(), stack.getCount() - limit) : ItemStack.EMPTY;
    }

    private ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existing = this.getItem(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.setItem(slot, ItemStack.EMPTY);
                this.setChanged();
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.setItem(slot, new ItemStack(existing.getItem(), existing.getCount() - toExtract));
                this.setChanged();
            }

            return new ItemStack(existing.getItem(), toExtract);
        }
    }

    @Nullable
    public static Container getInventoryAt(Level world, BlockPos pos) {
        return getInventoryAt(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D);
    }

    @Nullable
    private static Container getInventoryAt(Level world, double x, double y, double z) {
        Container inventory = null;
        BlockPos blockPos = BlockPos.containing(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof WorldlyContainerHolder) {
            inventory = ((WorldlyContainerHolder) block).getContainer(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof Container) {
                inventory = (Container) blockEntity;
                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getContainer((ChestBlock) block, blockState, world, blockPos, true);
                }
            }
        }

        if (inventory == null) {
            List<net.minecraft.world.entity.Entity> list = world.getEntities((net.minecraft.world.entity.Entity) null, new net.minecraft.world.phys.AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), net.minecraft.world.entity.EntitySelector.CONTAINER_ENTITY_SELECTOR);
            if (!list.isEmpty()) {
                inventory = (Container) list.get(world.getRandom().nextInt(list.size()));
            }
        }

        return inventory;
    }

    private static ItemStack transfer(@Nullable Container from, int slotFrom, Container to, ItemStack stack, int slot, @Nullable Direction direction) {
        ItemStack itemStack = to.getItem(slot);
        if (canInsert(to, stack, slot, direction)) {
            boolean bl = false;
            if (itemStack.isEmpty()) {
                to.setItem(slot, stack);
                stack = ItemStack.EMPTY;
                if (from != null)
                {
                    from.setItem(slotFrom, ItemStack.EMPTY);
                }
                bl = true;
            } else if (canMergeItems(itemStack, stack)) {
                int i = stack.getMaxStackSize() - itemStack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemStack.grow(j);
                bl = j > 0;
            }

            if (bl) {
                to.setChanged();
            }
        }

        return stack;
    }

    private static boolean canInsert(Container inventory, ItemStack stack, int slot, @Nullable Direction side) {
        if (!inventory.canPlaceItem(slot, stack)) {
            return false;
        } else {
            return !(inventory instanceof WorldlyContainer) || ((WorldlyContainer) inventory).canPlaceItemThroughFace(slot, stack, side);
        }
    }

    private static boolean canExtract(Container inv, ItemStack stack, int slot, Direction facing) {
        return !(inv instanceof WorldlyContainer) || ((WorldlyContainer) inv).canTakeItemThroughFace(slot, stack, facing);
    }

    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        return ItemStack.isSameItemSameComponents(first, second);
    }

    //CLIENT SYNC
    public int getSettingBottom() {
        return this.furnaceSettings.get(0);
    }

    public int getSettingTop() {
        return this.furnaceSettings.get(1);
    }

    public int getSettingFront() {
        int i = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).get3DDataValue();
        return this.furnaceSettings.get(i);
    }

    public int getSettingBack() {
        int i = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().get3DDataValue();
        return this.furnaceSettings.get(i);
    }

    public int getSettingLeft() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (facing == Direction.NORTH) {
            return this.furnaceSettings.get(Direction.EAST.get3DDataValue());
        } else if (facing == Direction.WEST) {
            return this.furnaceSettings.get(Direction.NORTH.get3DDataValue());
        } else if (facing == Direction.SOUTH) {
            return this.furnaceSettings.get(Direction.WEST.get3DDataValue());
        } else {
            return this.furnaceSettings.get(Direction.SOUTH.get3DDataValue());
        }
    }

    public int getSettingRight() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (facing == Direction.NORTH) {
            return this.furnaceSettings.get(Direction.WEST.get3DDataValue());
        } else if (facing == Direction.WEST) {
            return this.furnaceSettings.get(Direction.SOUTH.get3DDataValue());
        } else if (facing == Direction.SOUTH) {
            return this.furnaceSettings.get(Direction.EAST.get3DDataValue());
        } else {
            return this.furnaceSettings.get(Direction.NORTH.get3DDataValue());
        }
    }

    public int getIndexFront() {
        int i = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).get3DDataValue();
        return i;
    }

    public int getIndexBack() {
        int i = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().get3DDataValue();
        return i;
    }

    public int getIndexLeft() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (facing == Direction.NORTH) {
            return Direction.EAST.get3DDataValue();
        } else if (facing == Direction.WEST) {
            return Direction.NORTH.get3DDataValue();
        } else if (facing == Direction.SOUTH) {
            return Direction.WEST.get3DDataValue();
        } else {
            return Direction.SOUTH.get3DDataValue();
        }
    }

    public int getIndexRight() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        if (facing == Direction.NORTH) {
            return Direction.WEST.get3DDataValue();
        } else if (facing == Direction.WEST) {
            return Direction.SOUTH.get3DDataValue();
        } else if (facing == Direction.SOUTH) {
            return Direction.EAST.get3DDataValue();
        } else {
            return Direction.NORTH.get3DDataValue();
        }
    }

    public int getAutoInput() {
        return this.furnaceSettings.get(6);
    }

    public int getAutoOutput() {
        return this.furnaceSettings.get(7);
    }

    public int getRedstoneSetting() {
        return this.furnaceSettings.get(8);
    }

    public int getRedstoneComSub() {
        return this.furnaceSettings.get(9);
    }

    public int getShowButtons() {
        return this.furnaceSettings.get(10);
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    private boolean canSmelt(RecipeHolder<?> recipe) {
        if (!this.inventory.get(0).isEmpty() && recipe != null) {
            ItemStack itemstack = ((net.minecraft.world.item.crafting.Recipe<net.minecraft.world.item.crafting.SingleRecipeInput>) recipe.value()).assemble(new net.minecraft.world.item.crafting.SingleRecipeInput(this.getItem(0)));
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.inventory.get(2);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(itemstack1, itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getMaxStackSize() && itemstack1.getCount() < itemstack1.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private void smeltItem(RecipeHolder<?> recipe) {
        timer = 0;
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.inventory.get(0);
            ItemStack itemstack1 = ((net.minecraft.world.item.crafting.Recipe<net.minecraft.world.item.crafting.SingleRecipeInput>) recipe.value()).assemble(new net.minecraft.world.item.crafting.SingleRecipeInput(this.getItem(0)));
            ItemStack itemstack2 = this.inventory.get(2);
            if (itemstack2.isEmpty()) {
                this.inventory.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.inventory.get(1).isEmpty() && this.inventory.get(1).getItem() == Items.BUCKET) {
                this.inventory.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.furnaceBurnTime = input.getIntOr("BurnTime", 0);
        this.cookTime = input.getIntOr("CookTime", 0);
        this.totalCookTime = input.getIntOr("CookTimeTotal", 0);
        this.timer = 0;
        this.currentAugment = input.getIntOr("Augment", 0);
        this.currentItemBurnTime = input.getIntOr("currentItemBurnTime", 0);
        this.jovial = input.getIntOr("jovial", 0);
        
        Optional<CompoundTag> recipesUsedTag = input.read("RecipesUsed", CompoundTag.CODEC);
        recipesUsedTag.ifPresent(compound -> {
            for (String string : compound.keySet()) {
                this.recipesUsed.put(Identifier.tryParse(string), compound.getInt(string).orElse(0));
            }
        });

        Optional<CompoundTag> settingsTag = input.read("furnace_settings", CompoundTag.CODEC);
        settingsTag.ifPresent(compound -> this.furnaceSettings.readNbt(compound));
        
        this.placedConfig = input.getBooleanOr("placed", false);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("BurnTime", this.furnaceBurnTime);
        output.putInt("CookTime", this.cookTime);
        output.putInt("CookTimeTotal", this.totalCookTime);
        output.putInt("Augment", this.currentAugment);
        output.putInt("currentItemBurnTime", this.currentItemBurnTime);
        output.putInt("jovial", this.jovial);
        
        CompoundTag compoundTag = new CompoundTag();
        this.recipesUsed.forEach((identifier, integer) -> {
            compoundTag.putInt(identifier.toString(), integer);
        });
        output.store("RecipesUsed", CompoundTag.CODEC, compoundTag);

        CompoundTag compoundTag2 = new CompoundTag();
        furnaceSettings.writeNbt(compoundTag2);
        output.store("furnace_settings", CompoundTag.CODEC, compoundTag2);
        
        output.putBoolean("placed", this.placedConfig);
    }

    protected static int getFuelTime(@Nullable Level level, ItemStack fuel) {
        if (fuel.isEmpty() || level == null) {
            return 0;
        } else {
            return level.fuelValues().burnDuration(fuel);
        }
    }

    public static boolean isItemFuel(@Nullable Level level, ItemStack stack) {
        return getFuelTime(level, stack) > 0 || stack.getItem() instanceof ironfurnaces.api.IHeaterItem;
    }


    @Override
    public int[] getSlotsForFace(Direction side) {
        if (this.furnaceSettings.get(side.get3DDataValue()) == 0) {
            return new int[]{};
        } else if (this.furnaceSettings.get(side.get3DDataValue()) == 1) {
            return new int[]{0, 1};
        } else if (this.furnaceSettings.get(side.get3DDataValue()) == 2) {
            return new int[]{2};
        } else if (this.furnaceSettings.get(side.get3DDataValue()) == 3) {
            return new int[]{0, 1, 2};
        } else if (this.furnaceSettings.get(side.get3DDataValue()) == 4) {
            return new int[]{1};
        }
        return new int[]{};
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (this.furnaceSettings.get(direction.get3DDataValue()) == 0) {
            return false;
        } else if (this.furnaceSettings.get(direction.get3DDataValue()) == 1) {
            return false;
        } else if (this.furnaceSettings.get(direction.get3DDataValue()) == 2) {
            return index == 2;
        } else if (this.furnaceSettings.get(direction.get3DDataValue()) == 3) {
            return index == 2;
        } else if (this.furnaceSettings.get(direction.get3DDataValue()) == 4 && stack.getItem() != Items.BUCKET) {
            return false;
        } else if (this.furnaceSettings.get(direction.get3DDataValue()) == 4 && stack.getItem() == Items.BUCKET) {
            return true;
        }
        return false;
    }

    private static net.minecraft.resources.ResourceKey<RecipePropertySet> getPropertySetKey(RecipeType<?> type) {
        if (type == RecipeType.BLASTING) {
            return RecipePropertySet.BLAST_FURNACE_INPUT;
        } else if (type == RecipeType.SMOKING) {
            return RecipePropertySet.SMOKER_INPUT;
        } else {
            return RecipePropertySet.FURNACE_INPUT;
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == OUTPUT || index == 3) {
            return false;
        } else if (index == INPUT) {
            net.minecraft.resources.ResourceKey<RecipePropertySet> key = getPropertySetKey(this.recipeType);
            return this.level.recipeAccess().propertySet(key).test(stack);
        } else if (index == FUEL) {
            ItemStack itemstack = this.inventory.get(FUEL);
            return getFuelTime(this.level, stack) > 0 || (stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET) || stack.getItem() instanceof ironfurnaces.api.IHeaterItem;
        }
        return false;
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            Identifier identifier = recipe.id().identifier();
            boolean flag2 = false;
            if (!this.level.isClientSide()) {
                if (this.recipesUsed.size() > IronFurnacesConfig.furnaceXPDropValue) {
                    this.grantExperience(level, new Vec3(this.worldPosition.getX() + this.level.getRandom().nextInt(2) - 1, this.worldPosition.getY(), this.worldPosition.getZ() + this.level.getRandom().nextInt(2) - 1));
                    this.recipesUsed.clear();
                } else {
                    for (Object2IntMap.Entry<Identifier> entry : this.recipesUsed.object2IntEntrySet()) {
                        if (level.getServer().getRecipeManager().byKey(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, entry.getKey())).isPresent()) {
                            if (entry.getIntValue() > IronFurnacesConfig.furnaceXPDropValue2) {
                                if (!flag2) {
                                    this.grantExperience(level, new Vec3(this.worldPosition.getX() + this.level.getRandom().nextInt(2) - 1, this.worldPosition.getY(), this.worldPosition.getZ() + this.level.getRandom().nextInt(2) - 1));
                                }
                                flag2 = true;
                            }
                        }
                    }
                    if (flag2) {
                        this.recipesUsed.clear();
                    }
                }
            }

            this.recipesUsed.addTo(identifier, 1);
        }

    }

    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(Player player, List<ItemStack> items) {
        List<RecipeHolder<?>> list = this.grantExperience(player.level(), player.position());
        player.awardRecipes((Collection) list);
        this.recipesUsed.clear();
    }

    public List<RecipeHolder<?>> grantExperience(Level world, Vec3 vec3d) {
        List<RecipeHolder<?>> list = Lists.newArrayList();
        ObjectIterator var4 = this.recipesUsed.object2IntEntrySet().iterator();

        while (var4.hasNext()) {
            Object2IntMap.Entry<Identifier> entry = (Object2IntMap.Entry) var4.next();
            world.getServer().getRecipeManager().byKey(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, entry.getKey())).ifPresent((recipe) -> {
                list.add(recipe);
                dropExperience(world, vec3d, entry.getIntValue(), ((AbstractCookingRecipe) recipe.value()).experience());
            });
        }

        return list;
    }

    private static void dropExperience(Level world, Vec3 vec3d, int i, float f) {
        int j = Mth.floor((float) i * f);
        float g = Mth.frac((float) i * f);
        if (g != 0.0F && Math.random() < (double) g) {
            ++j;
        }

        if (world instanceof net.minecraft.server.level.ServerLevel) {
            ExperienceOrb.award((net.minecraft.server.level.ServerLevel) world, vec3d, j);
        }
    }

    @Override
    public void fillStackedContents(net.minecraft.world.entity.player.StackedItemContents contents) {
        for (ItemStack itemstack : this.inventory) {
            contents.accountStack(itemstack);
        }
    }

    protected boolean doesNeedUpdateSend() {
        return !Arrays.equals(this.provides, this.lastProvides);
    }

    public void onUpdateSent() {
        System.arraycopy(this.provides, 0, this.lastProvides, 0, this.provides.length);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    public void placeConfig() {
        if (placedConfig) return;
        if (this.furnaceSettings != null) {
            this.furnaceSettings.set(0, 2);
            this.furnaceSettings.set(1, 1);
            for (Direction dir : Direction.values()) {
                if (dir != Direction.DOWN && dir != Direction.UP) {
                    this.furnaceSettings.set(dir.get3DDataValue(), 4);
                }
            }
            this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(this.worldPosition).getBlock().defaultBlockState(), this.level.getBlockState(this.worldPosition), 3);
            placedConfig = true;
        }

    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null && !this.level.isClientSide()) {
            this.grantExperience(this.level, net.minecraft.world.phys.Vec3.atCenterOf(pos));
        }
    }
}
