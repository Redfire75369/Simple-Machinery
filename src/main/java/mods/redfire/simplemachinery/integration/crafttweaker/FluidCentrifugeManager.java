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
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.item.MCWeightedItemStack;
import com.google.common.primitives.Floats;
import mods.redfire.simplemachinery.tileentities.fluidcentrifuge.FluidCentrifugeRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.simplemachinery.fluidcentrifuge")
public class FluidCentrifugeManager implements IRecipeManager {
	@ZenCodeType.Method
	public void addRecipe(String id, IFluidStack fluidInput, MCWeightedItemStack[] outputs, IFluidStack[] fluidOutputs, int energy, int time) {
		ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);

		FluidCentrifugeRecipe recipe = new FluidCentrifugeRecipe(recipeId, fluidInput.getInternal(), Arrays.stream(outputs).map(MCWeightedItemStack::getItemStack).map(IItemStack::getInternal).collect(Collectors.toList()), Arrays.stream(outputs).map((output) -> (float) output.getWeight()).collect(Collectors.toList()), Arrays.stream(fluidOutputs).map(IFluidStack::getInternal).collect(Collectors.toList()), energy, time);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@ZenCodeType.Method
	public void addRecipe(String id, IFluidStack fluidInput, IItemStack[] outputs, float[] outputChances, IFluidStack[] fluidOutputs, int energy, int time) {
		ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);

		FluidCentrifugeRecipe recipe = new FluidCentrifugeRecipe(recipeId, fluidInput.getInternal(), Arrays.stream(outputs).map(IItemStack::getInternal).collect(Collectors.toList()), Floats.asList(outputChances), Arrays.stream(fluidOutputs).map(IFluidStack::getInternal).collect(Collectors.toList()), energy, time);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@Override
	public IRecipeType<FluidCentrifugeRecipe> getRecipeType() {
		return FluidCentrifugeRecipe.RECIPE_TYPE;
	}
}
