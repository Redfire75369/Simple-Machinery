/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.machine.fluid;

import mods.redfire.simplemachinery.tileentities.machine.MachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class FluidMachineRecipe extends MachineRecipe {
	protected FluidMachineRecipe(ResourceLocation id, int fuel, int time, List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems, List<Float> outputItemChances, List<FluidStack> outputFluids) {
		super(id, fuel, time, inputItems, inputFluids, outputItems, outputItemChances, outputFluids);
	}

	public int getFuel() {
		return getResource();
	}

	public int getFuelRate() {
		return getResourceRate();
	}
}

