/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.inventory;

import mods.redfire.simplemachinery.util.IMachineInventoryCallback;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MachineItemHandler implements IItemHandlerModifiable {
	@Nullable
	protected IMachineInventoryCallback tile;
	protected List<MachineItemSlot> slots;

	public MachineItemHandler() {
		this(null);
	}

	public MachineItemHandler(@Nullable IMachineInventoryCallback tile) {
		this.tile = tile;
		this.slots = new ArrayList<>();
	}

	public MachineItemHandler(@Nullable IMachineInventoryCallback tile, @Nonnull List<MachineItemSlot> slots) {
		this.tile = tile;
		this.slots = new ArrayList<>(slots);
	}

	public boolean hasSlots() {
		return slots.size() > 0;
	}

	public boolean isEmpty() {
		for (MachineItemSlot slot : slots) {
			if (!slot.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void onInventoryChange(int slot) {
		if (tile == null) {
			return;
		}
		tile.onInventoryChange(slot);
	}

	@Override
	public int getSlots() {
		return slots.size();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 0 || slot > getSlots()) {
			return ItemStack.EMPTY;
		}
		return slots.get(slot).getItemStack();
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		if (slot < 0 || slot > getSlots()) {
			return;
		}
		slots.get(slot).setItemStack(stack);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (slot < 0 || slot > getSlots()) {
			return stack;
		}

		ItemStack ret = slots.get(slot).insertItem(slot, stack, simulate);
		if (!simulate) {
			onInventoryChange(slot);
		}
		return ret;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot < 0 || slot > getSlots()) {
			return ItemStack.EMPTY;
		}
		ItemStack ret = slots.get(slot).extractItem(slot, amount, simulate);
		if (!simulate) {
			onInventoryChange(slot);
		}
		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {
		if (slot < 0 || slot > getSlots()) {
			return 0;
		}
		return slots.get(slot).getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		if (slot < 0 || slot > getSlots()) {
			return false;
		}
		return slots.get(slot).isItemValid(stack);
	}
}
