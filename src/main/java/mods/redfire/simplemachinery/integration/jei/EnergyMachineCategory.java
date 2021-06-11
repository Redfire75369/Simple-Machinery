/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mods.redfire.simplemachinery.tileentities.machine.energy.EnergyMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class EnergyMachineCategory<T extends EnergyMachineRecipe> extends MachineCategory<T> {
	public EnergyMachineCategory(IGuiHelper helper, int xSize, int ySize, ItemStack icon, ResourceLocation gui) {
		super(helper, xSize, ySize, icon, gui);
	}

	@Override
	public void setIngredients(@Nonnull T recipe, @Nonnull IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputItems());

		ingredients.setInputs(VanillaTypes.FLUID, recipe.getInputFluids());
		ingredients.setOutputs(VanillaTypes.FLUID, recipe.getOutputFluids());
	}
}
