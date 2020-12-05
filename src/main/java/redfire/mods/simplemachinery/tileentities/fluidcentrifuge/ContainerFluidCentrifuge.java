package redfire.mods.simplemachinery.tileentities.fluidcentrifuge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import redfire.mods.simplemachinery.tileentities.generic.ContainerMachine;

public class ContainerFluidCentrifuge extends ContainerMachine<TileFluidCentrifuge> {
    public ContainerFluidCentrifuge(IInventory playerInventory, TileFluidCentrifuge tileEntity) {
        super(playerInventory, tileEntity);
    }

    @Override
    protected void addPlayerSlots(IInventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 58 + 84;
            addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Override
    protected void addOwnSlots() {
        IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int x = 93;
        int y = 11;

        int slotIndex = 0;
        addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));
        y += 24;
        addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            TileFluidCentrifuge tileEntity = new TileFluidCentrifuge();

            if (index < tileEntity.input_slots) {
                if (!mergeItemStack(itemstack1, tileEntity.input_slots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 0, tileEntity.input_slots, false)) {
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
}
