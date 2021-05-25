package mods.redfire.simplemachinery.tileentities.machine;

import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.util.IntArrayWrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class MachineContainer<T extends MachineTile<?>> extends Container {
    @Nullable
    protected final T tile;

    protected final World world;
    protected final BlockPos pos;
    protected final IntArrayWrapper data;

    public MachineContainer(ContainerType<?> type, int inventorySize, int id, World world, BlockPos pos, PlayerInventory playerInv) {
        this(type, inventorySize, id, world, pos, playerInv, null, new IntArrayWrapper(2));
    }

    public MachineContainer(ContainerType<?> type, int inventorySize, int id, World world, BlockPos pos, PlayerInventory playerInv, final T tile) {
        this(type, inventorySize, id, world, pos, playerInv, tile, tile.getData());
    }

    public MachineContainer(ContainerType<?> type, int inventorySize, int id, World world, BlockPos pos, PlayerInventory playerInv, @Nullable T tile, final IntArrayWrapper data) {
        super(type, id);

        this.tile = tile;
        this.world = world;
        this.pos = pos;
        this.data = data;

        addDataSlots(data);
        trackEnergy();

        layoutPlayerInventorySlots(new InvWrapper(playerInv), 8, 84);
        layoutMachineInventorySlots(tile != null ? ((MachineTile<?>) tile).inventory : new ItemStackHandler(inventorySize));
    }

    protected void trackEnergy() {
        if (tile != null) {
            addDataSlots(new IIntArray() {
                @Override
                public int get(int index) {
                    switch (index) {
                        case 0:
                            return getEnergy() & 0xffff;
                        case 1:
                            return (getEnergy() >> 16) & 0xffff;
                        default:
                            return 0;
                    }
                }

                @Override
                public void set(int index, int value) {
                    int energyStored = getEnergy();
                    switch (index) {
                        case 0:
                            tile.getEnergy().setEnergyStored((energyStored & 0xffff0000) | (value & 0xffff));
                            break;
                        case 1:
                            tile.getEnergy().setEnergyStored((value << 16) | (energyStored & 0xffff));
                            break;
                    }
                }

                @Override
                public int getCount() {
                    return 2;
                }
            });
        } else {
            addDataSlots(new IntArrayWrapper(1));
        }
    }

    protected int addSlotRow(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotRectangle(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRow(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(IItemHandler playerInv, int leftCol, int topRow) {
        addSlotRectangle(playerInv, 9, leftCol, topRow, 9, 18, 3, 18);
        topRow += 58;
        addSlotRow(playerInv, 0, leftCol, topRow, 9, 18);
    }

    protected void layoutMachineInventorySlots(IItemHandler inventory) {}

    public int getEnergy() {
        return tile.getEnergy().getEnergyStored();
    }

    public IntArrayWrapper getData() {
        return data;
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(IWorldPosCallable.create(world, pos), playerIn, Blocks.TURNTABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack item = slot.getItem();
            stack = item.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(item, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(item, stack);
            } else {
                if (index < 28) {
                    if (!this.moveItemStackTo(item, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37 && !this.moveItemStackTo(item, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (item.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, item);
        }

        return stack;
    }
}
