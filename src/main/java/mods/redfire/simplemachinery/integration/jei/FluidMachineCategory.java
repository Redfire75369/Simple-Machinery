/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mods.redfire.simplemachinery.tileentities.machine.fluid.FluidMachineRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FluidMachineCategory<T extends FluidMachineRecipe> extends MachineCategory<T> {
	private final List<Fluid> fuels;

	public FluidMachineCategory(IGuiHelper helper, int xSize, int ySize, ItemStack icon, ResourceLocation gui, List<Fluid> fuels) {
		super(helper, xSize, ySize, icon, gui);
		this.fuels = fuels;
	}

	@Override
	public void setIngredients(@Nonnull T recipe, @Nonnull IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputItems());

		List<List<FluidStack>> fluidInputs = new ArrayList<>();
		fluidInputs.add(fuels.stream().map(f -> new FluidStack(f, recipe.getFuel())).collect(Collectors.toList()));
		fluidInputs.addAll(recipe.getInputFluids().stream().map(Collections::singletonList).collect(Collectors.toList()));
		ingredients.setInputLists(VanillaTypes.FLUID, fluidInputs);
		ingredients.setOutputs(VanillaTypes.FLUID, recipe.getOutputFluids());
	}
}
