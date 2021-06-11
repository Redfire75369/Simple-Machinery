/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.inventory;

import mods.redfire.simplemachinery.util.IMachineInventoryCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MachineInventory extends MachineItemHandler implements IInventory {
	protected List<MachineItemSlot> inputSlots = new ArrayList<>();
	protected List<MachineItemSlot> outputSlots = new ArrayList<>();

	protected IItemHandler inputHandler;
	protected IItemHandler outputHandler;
	protected IItemHandler combinedHandler;

	public MachineInventory(@Nullable IMachineInventoryCallback tile) {
		this(tile, Collections.emptyList());
	}

	public MachineInventory(@Nullable IMachineInventoryCallback tile, @Nonnull List<MachineItemSlot> slots) {
		super(tile, slots);
	}

	public void addSlot(InventoryGroup group, MachineItemSlot slot) {
		if (combinedHandler != null) {
			return;
		}

		slots.add(slot);
		switch (group) {
			case INPUT:
				inputSlots.add(slot);
				break;
			case OUTPUT:
				outputSlots.add(slot);
				break;
			default:
		}
	}

	public void addSlots(InventoryGroup group, int amount) {
		for (int i = 0; i < amount; ++i) {
			addSlot(group, new MachineItemSlot());
		}
	}

	public void addSlots(InventoryGroup group, List<MachineItemSlot> slots) {
		for (MachineItemSlot slot : slots) {
			addSlot(group, slot);
		}
	}

	protected void optimize() {
		((ArrayList<MachineItemSlot>) slots).trimToSize();
		((ArrayList<MachineItemSlot>) inputSlots).trimToSize();
		((ArrayList<MachineItemSlot>) outputSlots).trimToSize();
	}

	public void initHandlers() {
		optimize();
		inputHandler = new MachineItemHandler(tile, inputSlots);
		outputHandler = new MachineItemHandler(tile, outputSlots);
		combinedHandler = new MachineItemHandler(tile, slots);
	}

	@Nonnull
	public ItemStack get(int slot) {
		return slots.get(slot).getItemStack();
	}

	public void set(int slot, @Nonnull ItemStack stack) {
		slots.get(slot).setItemStack(stack);
	}

	public void clear() {
		for (MachineItemSlot slot : slots) {
			slot.setItemStack(ItemStack.EMPTY);
		}
	}

	public boolean hasInputSlots() {
		return inputSlots.size() > 0;
	}

	public boolean hasOutputSlots() {
		return outputSlots.size() > 0;
	}

	public boolean hasAccessibleSlots() {
		return hasInputSlots() || hasOutputSlots();
	}

	public MachineItemSlot getSlot(int slot) {
		return slots.get(slot);
	}

	public List<MachineItemSlot> getInputSlots() {
		return inputSlots;
	}

	public List<MachineItemSlot> getOutputSlots() {
		return outputSlots;
	}

	public MachineInventory getInputInventory() {
		return new MachineInventory(tile, getInputSlots());
	}

	public MachineInventory getOutputInventory() {
		return new MachineInventory(tile, getOutputSlots());
	}

	public IItemHandler getHandler(InventoryGroup group) {
		if (combinedHandler == null) {
			initHandlers();
		}
		switch (group) {
			case INPUT:
				return inputHandler;
			case OUTPUT:
				return outputHandler;
			case ALL:
				return combinedHandler;
			default:
		}
		return EmptyHandler.INSTANCE;
	}

	public MachineInventory read(CompoundNBT tag) {
		for (MachineItemSlot slot : slots) {
			slot.setItemStack(ItemStack.EMPTY);
		}
		ListNBT list = tag.getList("Inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); ++i) {
			CompoundNBT slotTag = list.getCompound(i);
			int slot = slotTag.getByte("Slot");
			if (slot >= 0 && slot < slots.size()) {
				slots.get(slot).read(slotTag);
			}
		}
		return this;
	}

	public CompoundNBT write(CompoundNBT tag) {
		if (slots.size() <= 0) {
			return tag;
		}
		ListNBT list = new ListNBT();
		for (int i = 0; i < slots.size(); ++i) {
			if (!slots.get(i).isEmpty()) {
				CompoundNBT slotTag = new CompoundNBT();
				slotTag.putByte("Slot", (byte) i);
				slots.get(i).write(slotTag);
				list.add(slotTag);
			}
		}
		if (!list.isEmpty()) {
			tag.put("Inventory", list);
		}
		return tag;
	}

	@Override
	public int getContainerSize() {
		return slots.size();
	}

	@Nonnull
	@Override
	public ItemStack getItem(int slot) {
		return get(slot);
	}

	@Nonnull
	@Override
	public ItemStack removeItem(int slot, int count) {
		return slots.get(slot).extractItem(0, count, false);
	}

	@Nonnull
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		slots.get(slot).setItemStack(ItemStack.EMPTY);
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, @Nonnull ItemStack stack) {
		slots.get(slot).setItemStack(stack);
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return true;
	}

	@Override
	public void clearContent() {
		clear();
	}
}
