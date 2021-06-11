/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.item.MCWeightedItemStack;
import com.google.common.primitives.Floats;
import mods.redfire.simplemachinery.tileentities.sieve.SieveRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Arrays;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.simplemachinery.sieve")
public class SieveManager implements IRecipeManager {
	@ZenCodeType.Method
	public void addRecipe(String id, IIngredient input, IItemStack[] outputs, int energy, int time) {
		ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);

		SieveRecipe recipe = new SieveRecipe(recipeId, input.asVanillaIngredient(), Arrays.stream(outputs).map(IItemStack::getInternal).collect(Collectors.toList()), null, energy, time);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@ZenCodeType.Method
	public void addRecipe(String id, IIngredient input, IItemStack[] outputs, float[] outputChances, int energy, int time) {
		ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);

		SieveRecipe recipe = new SieveRecipe(recipeId, input.asVanillaIngredient(), Arrays.stream(outputs).map(IItemStack::getInternal).collect(Collectors.toList()), Floats.asList(outputChances), energy, time);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@ZenCodeType.Method
	public void addRecipe(String id, IIngredient input, MCWeightedItemStack[] outputs, int energy, int time) {
		ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);

		SieveRecipe recipe = new SieveRecipe(recipeId, input.asVanillaIngredient(), Arrays.stream(outputs).map(MCWeightedItemStack::getItemStack).map(IItemStack::getInternal).collect(Collectors.toList()), Arrays.stream(outputs).map((output) -> (float) output.getWeight()).collect(Collectors.toList()), energy, time);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@Override
	public void removeRecipe(IItemStack output) {
	}

	@Override
	public IRecipeType<SieveRecipe> getRecipeType() {
		return SieveRecipe.RECIPE_TYPE;
	}
}
