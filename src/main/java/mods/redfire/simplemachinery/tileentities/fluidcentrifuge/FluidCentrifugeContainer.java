/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.fluidcentrifuge;

import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.registry.Containers;
import mods.redfire.simplemachinery.tileentities.machine.MachineContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

import static mods.redfire.simplemachinery.tileentities.fluidcentrifuge.FluidCentrifugeTile.ITEM_INPUTS;
import static mods.redfire.simplemachinery.tileentities.fluidcentrifuge.FluidCentrifugeTile.ITEM_OUTPUTS;

public class FluidCentrifugeContainer extends MachineContainer<FluidCentrifugeTile> {
	public static final int INVENTORY_SIZE = ITEM_INPUTS + ITEM_OUTPUTS;

	public FluidCentrifugeContainer(int id, World world, BlockPos pos, PlayerInventory playerInv) {
		super(Containers.FLUID_CENTRIFUGE_CONTAINER.get(), INVENTORY_SIZE, id, world, pos, playerInv);
	}

	public FluidCentrifugeContainer(int id, World world, BlockPos pos, PlayerInventory playerInv, FluidCentrifugeTile tile) {
		super(Containers.FLUID_CENTRIFUGE_CONTAINER.get(), INVENTORY_SIZE, id, world, pos, playerInv, tile);
	}

	@Override
	protected void layoutMachineInventorySlots(IItemHandler inv) {
		addSlotRow(inv, 0, 89, 17, 3, 18);
	}

	@Override
	public boolean stillValid(@Nonnull PlayerEntity player) {
		return stillValid(IWorldPosCallable.create(world, pos), player, Blocks.FLUID_CENTRIFUGE_BLOCK.get());
	}
}
