/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.inventory.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.item.ItemStack.EMPTY;

public class MachineItemMultiHandler implements IItemHandlerModifiable {
	List<GroupedMachineItemHandler> handlers = new ArrayList<>();
	List<Integer> startIndexes = new ArrayList<>();

	public MachineItemMultiHandler(GroupedMachineItemHandler... handlers) {
		int index = 0;
		for (GroupedMachineItemHandler handler : handlers) {
			this.handlers.add(handler);
			startIndexes.add(index);
			index += handler.getSlots();
		}
		startIndexes.add(index);
	}

	@Override
	public int getSlots() {
		return startIndexes.get(startIndexes.size() - 1);
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (slot < 0 || slot >= getSlots()) {
			return EMPTY;
		}

		Tuple<Integer, GroupedMachineItemHandler> handler = getHandler(slot);
		return handler.getB().getStackInSlot(slot - startIndexes.get(handler.getA()));
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		if (slot < 0 || slot >= getSlots()) {
			return;
		}

		Tuple<Integer, GroupedMachineItemHandler> handler = getHandler(slot);
		handler.getB().setStackInSlot(slot - startIndexes.get(handler.getA()), stack);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (slot < 0 || slot >= getSlots()) {
			return stack;
		}

		Tuple<Integer, GroupedMachineItemHandler> handler = getHandler(slot);
		return handler.getB().insertItem(slot - startIndexes.get(handler.getA()), stack, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot < 0 || slot >= getSlots()) {
			return EMPTY;
		}

		Tuple<Integer, GroupedMachineItemHandler> handler = getHandler(slot);
		return handler.getB().extractItem(slot - startIndexes.get(handler.getA()), amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		if (slot < 0 || slot > getSlots()) {
			return 0;
		}

		Tuple<Integer, GroupedMachineItemHandler> handler = getHandler(slot);
		return handler.getB().getSlotLimit(slot - startIndexes.get(handler.getA()));
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		if (slot < 0 || slot > getSlots()) {
			return false;
		}

		Tuple<Integer, GroupedMachineItemHandler> handler = getHandler(slot);
		return handler.getB().isItemValid(slot - startIndexes.get(handler.getA()), stack);
	}

	private Tuple<Integer, GroupedMachineItemHandler> getHandler(int slot) {
		int index = -1;
		for (int i = 0; i < startIndexes.size() - 1; i++) {
			if (startIndexes.get(i + 1) > slot) {
				index = i;
				break;
			}
		}

		return new Tuple<>(index, handlers.get(index));
	}
}
