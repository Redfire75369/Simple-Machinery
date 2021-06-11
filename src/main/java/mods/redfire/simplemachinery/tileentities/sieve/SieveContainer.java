/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.sieve;

import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.registry.Containers;
import mods.redfire.simplemachinery.tileentities.machine.MachineContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static mods.redfire.simplemachinery.tileentities.sieve.SieveTile.ITEM_INPUTS;
import static mods.redfire.simplemachinery.tileentities.sieve.SieveTile.ITEM_OUTPUTS;

public class SieveContainer extends MachineContainer<SieveTile> {
	public static final int INVENTORY_SIZE = ITEM_INPUTS + ITEM_OUTPUTS;

	public SieveContainer(int id, World world, BlockPos pos, PlayerInventory playerInv) {
		super(Containers.SIEVE_CONTAINER.get(), INVENTORY_SIZE, id, world, pos, playerInv);
	}

	public SieveContainer(int id, World world, BlockPos pos, PlayerInventory playerInv, SieveTile tile) {
		super(Containers.SIEVE_CONTAINER.get(), INVENTORY_SIZE, id, world, pos, playerInv, tile);
	}

	@Override
	protected void layoutMachineInventorySlots(IItemHandler inv) {
		addSlot(new SlotItemHandler(inv, 0, 35, 26));
		addSlotRectangle(inv, 1, 89, 17, 3, 18, 2, 18);
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return stillValid(IWorldPosCallable.create(world, pos), player, Blocks.SIEVE_BLOCK.get());
	}
}
