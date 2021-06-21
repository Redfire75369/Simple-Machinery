/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.machine;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MachineContainer<T extends MachineTile<?>> extends Container {
	@Nullable
	public final T tile;
	protected final int inventorySize;

	protected final World world;
	protected final BlockPos pos;

	public MachineContainer(ContainerType<?> type, int inventorySize, int id, World world, BlockPos pos, PlayerInventory playerInv) {
		this(type, inventorySize, id, world, pos, playerInv, null);
	}

	public MachineContainer(ContainerType<?> type, int inventorySize, int id, World world, BlockPos pos, PlayerInventory playerInv, @Nullable T tile) {
		super(type, id);

		this.tile = tile;
		this.inventorySize = inventorySize;
		this.world = world;
		this.pos = pos;

		layoutPlayerInventorySlots(new InvWrapper(playerInv), 8, 84);
		layoutMachineInventorySlots(tile != null ? tile.inventory : new ItemStackHandler(inventorySize));
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

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return false;
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		if (tile != null) {
			for (IContainerListener listener : containerListeners) {
				tile.sendGuiNetworkData(this, listener);
			}
		}
	}

	@Nonnull
	@Override
	public ItemStack quickMoveStack(@Nonnull PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {
			ItemStack item = slot.getItem();
			stack = item.copy();

			if (index < 36) {
				if (!moveItemStackTo(item, 36, 36 + inventorySize - 1, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.moveItemStackTo(item, 0, 35, false)) {
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
