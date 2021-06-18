/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util;

import mods.redfire.simplemachinery.util.inventory.fluid.MachineFluidInventory;
import mods.redfire.simplemachinery.util.inventory.item.MachineInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class MachineCombinedInventory implements IInventory {
	public final MachineInventory inventory;
	public final MachineFluidInventory tankInventory;

	public MachineCombinedInventory(MachineInventory inventory, MachineFluidInventory tankInventory) {
		this.inventory = inventory;
		this.tankInventory = tankInventory;
	}

	@Override
	public int getContainerSize() {
		return inventory.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty() && tankInventory.isEmpty();
	}

	@Nonnull
	@Override
	public ItemStack getItem(int slot) {
		return inventory.getItem(slot);
	}

	@Override
	public void setItem(int slot, @Nonnull ItemStack stack) {
		inventory.setItem(slot, stack);
	}

	@Nonnull
	@Override
	public ItemStack removeItem(int slot, int count) {
		return inventory.removeItem(slot, count);
	}

	@Nonnull
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setChanged() {
		inventory.setChanged();
		tankInventory.setChanged();
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return inventory.stillValid(player);
	}

	@Override
	public void clearContent() {
		inventory.clearContent();
		tankInventory.clearContent();
	}
}
