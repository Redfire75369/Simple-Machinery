package redfire.mods.simplemachinery.tileentities.generic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import redfire.mods.simplemachinery.tileentities.turntable.TileTurntable;

public class ContainerMachine<TE extends TileMachine> extends Container {
    protected final TE tileEntity;

    public ContainerMachine(IInventory playerInventory, TE tileEntity) {
        this.tileEntity = tileEntity;
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    protected void addPlayerSlots(IInventory playerInventory) {}
    protected void addOwnSlots() {}

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            TileTurntable te = new TileTurntable();

            if (index < te.input_slots) {
                if (!mergeItemStack(itemstack1, te.input_slots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 0, te.input_slots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntity.canInteractWith(playerIn);
    }
}
