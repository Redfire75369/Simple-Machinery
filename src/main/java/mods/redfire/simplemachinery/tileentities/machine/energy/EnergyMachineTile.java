/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.machine.energy;

import mods.redfire.simplemachinery.tileentities.machine.MachineTile;
import mods.redfire.simplemachinery.util.energy.EnergyCoil;
import mods.redfire.simplemachinery.util.inventory.fluid.MachineFluidTank;
import mods.redfire.simplemachinery.util.inventory.item.MachineItemSlot;
import net.minecraft.tileentity.TileEntityType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EnergyMachineTile<T extends EnergyMachineRecipe> extends MachineTile<T> {
	public EnergyMachineTile(TileEntityType<?> type, int itemInputs, int itemOutputs, int fluidInputs, int fluidInputsCapacity, int fluidOutputs, int fluidOutputsCapacity, EnergyCoil energy) {
		super(type, itemInputs, itemOutputs, fluidInputs, fluidInputsCapacity, fluidOutputs, fluidOutputsCapacity, energy);
	}

	public EnergyMachineTile(TileEntityType<?> type, List<MachineItemSlot> inputSlots, List<MachineItemSlot> outputSlots, List<MachineFluidTank> inputTanks, List<MachineFluidTank> outputTanks, EnergyCoil energy) {
		super(type, inputSlots, outputSlots, inputTanks, outputTanks, Collections.emptyList(), energy);
	}

	@Override
	public void tick() {
		if (level == null || level.isClientSide) {
			return;
		}

		if (currentRecipe.isPresent()) {
			T recipe = currentRecipe.get();
			energy.modify(-recipe.getResourceRate());
			progress--;

			if (canComplete()) {
				complete();
				clear();
			} else if (energy.getEnergyStored() < recipe.getResourceRate() || !recipe.matches(getCombinedInputInv(), level)) {
				clear();
			}
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
		return energy.getEnergyStored() >= recipe.getResource() && super.canBegin(recipe);
	}
}
