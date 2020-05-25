package redfire.mods.simplemachines.tileentities.autoclave;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAutoclave extends Container {
	private TileAutoclave te;

	public ContainerAutoclave(IInventory playerInventory, TileAutoclave te) {
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 84;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		}

		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = 58 + 84;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int x = 38;
		int y = 35;

		int slotIndex = 0;
		addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));
		x+=18;
		addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));
		x = 109;
		addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex++, x, y));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TileAutoclave.size) {
				if (!this.mergeItemStack(itemstack1, TileAutoclave.size, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, TileAutoclave.size, false)) {
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
		return te.canInteractWith(playerIn);
	}
}
