/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.jei.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
	public void draw(@Nonnull T recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);
		if (energy == null) {
			energy = guiHelper.drawableBuilder(gui, 1, ySize + 1, recipe.getEnergy() * 96 / 10000, 8)
					.buildAnimated(getRecipeTime(recipe), IDrawableAnimated.StartDirection.RIGHT, true);
		}
	}

	@Override
	public void setIngredients(@Nonnull T recipe, @Nonnull IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputItems());

		ingredients.setInputs(VanillaTypes.FLUID, recipe.getInputFluids());
		ingredients.setOutputs(VanillaTypes.FLUID, recipe.getOutputFluids());
	}
}
