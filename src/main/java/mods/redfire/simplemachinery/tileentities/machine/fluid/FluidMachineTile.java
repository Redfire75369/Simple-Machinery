/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.machine.fluid;

import mods.redfire.simplemachinery.tileentities.machine.MachineTile;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import mods.redfire.simplemachinery.util.inventory.fluid.MachineFluidTank;
import mods.redfire.simplemachinery.util.inventory.item.MachineItemSlot;
import net.minecraft.tileentity.TileEntityType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FluidMachineTile<T extends FluidMachineRecipe> extends MachineTile<T> {
	public FluidMachineTile(TileEntityType<?> type, List<MachineItemSlot> inputSlots, List<MachineItemSlot> outputSlots, List<MachineFluidTank> inputTanks, List<MachineFluidTank> outputTanks, MachineFluidTank fuelTank) {
		super(type, inputSlots, outputSlots, inputTanks, outputTanks, Collections.singletonList(fuelTank), new EnergyCoil(0));
	}

	@Override
	public void tick() {
		if (level == null || level.isClientSide) {
			return;
		}

		MachineFluidTank fuelTank = tankInventory.getTank(0);

		if (currentRecipe.isPresent()) {
			T recipe = currentRecipe.get();
			fuelTank.modify(-recipe.getFuelRate());
			progress--;

			if (canComplete()) {
				complete();
				clear();
			} else if (fuelTank.getStored() < recipe.getFuelRate()) {
				clear();
			}
			// TODO: [FIX] Recipe is not cleared when inputs are removed.
		} else {
			Optional<T> recipe = getRecipe();
			if (recipe.isPresent() && canBegin(recipe.get())) {
				begin(recipe.get());
			}
		}
	}

	@Override
	public void begin(T recipe) {
		currentRecipe = Optional.of(recipe);
		progress = totalProgress = recipe.getTime();

		itemInputCounts = recipe.getInputItemCounts(inventory);
		fluidInputCounts = recipe.getInputFluidCounts(tankInventory);
	}

	@Override
	public boolean canBegin(T recipe) {
		return tankInventory.getTank(0).getStored() >= recipe.getFuel() && super.canBegin(recipe);
	}
}
