package mods.redfire.simplemachinery.tileentities.machine;

import mods.redfire.simplemachinery.registry.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class MachineContainer<T extends MachineTile<?>> extends Container {
    protected final World world;
    protected final BlockPos pos;
    protected final IIntArray guiData;

    public MachineContainer(ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInv) {
        this(type, id, world, pos, playerInv, new IntArray(6));
    }

    public MachineContainer(ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInv, final MachineTile<?> tile) {
        this(type, id, world, pos, playerInv, tile.getGuiData());
    }

    public MachineContainer(ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInv, final IIntArray guiData) {
        super(type, id);

        this.world = world;
        this.pos = pos;
        this.guiData = guiData;
        TileEntity tile = world.getBlockEntity(pos);

        addDataSlots(guiData);

        layoutPlayerInventorySlots(new InvWrapper(playerInv), 8, 84);
        layoutMachineInventorySlots(tile != null && tile instanceof MachineTile ? ((MachineTile<?>) tile).inventory : new ItemStackHandler(1 + 1));
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

    public IIntArray getGuiData() {
        return guiData;
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
