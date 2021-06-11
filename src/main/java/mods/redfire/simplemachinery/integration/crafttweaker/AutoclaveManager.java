/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.simplemachinery.autoclave")
public class AutoclaveManager implements IRecipeManager {
	@ZenCodeType.Method
	public void addRecipe(String id, IIngredient input, IFluidStack fluidInput, IItemStack output, int steam, int time) {
		ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);

		AutoclaveRecipe recipe = new AutoclaveRecipe(recipeId, input.asVanillaIngredient(), fluidInput.getInternal(), output.getInternal(), steam, time);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@Override
	public void removeRecipe(IItemStack output) {
		CraftTweakerAPI.apply(new ActionRemoveRecipe(this, recipe -> {
			if (recipe instanceof AutoclaveRecipe) {
				return output.matches(new MCItemStackMutable(((AutoclaveRecipe) recipe).getOutputItems().get(0)));
			}
			return false;
		}, action -> "Removing \"" + action.getRecipeTypeName() + "\" recipes with output: " + output + "\""));
	}

	@Override
	public IRecipeType<AutoclaveRecipe> getRecipeType() {
		return AutoclaveRecipe.RECIPE_TYPE;
	}
}
