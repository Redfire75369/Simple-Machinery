/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.inventory.item;

import mods.redfire.simplemachinery.util.IMachineInventoryCallback;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.item.ItemStack.EMPTY;

public class GroupedMachineItemHandler extends MachineItemHandler {
	@Nonnull
	private final SlotGroup group;

	public GroupedMachineItemHandler(@Nonnull SlotGroup group) {
		super();
		this.group = group;
	}

	public GroupedMachineItemHandler(@Nonnull SlotGroup group, @Nullable IMachineInventoryCallback tile) {
		super(tile);
		this.group = group;
	}

	public GroupedMachineItemHandler(@Nonnull SlotGroup group, @Nullable IMachineInventoryCallback tile, @Nonnull List<MachineItemSlot> slots) {
		super(tile, slots);
		this.group = group;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (group == SlotGroup.OUTPUT) {
			return stack;
		}
		return super.insertItem(slot, stack, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (group == SlotGroup.INPUT) {
			return EMPTY;
		}
		return super.extractItem(slot, amount, simulate);
	}
}
