package redfire.mods.simplemachinery.integration.jei.recipemakers;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import redfire.mods.simplemachinery.integration.jei.wrappers.RecipeTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.RecipesTurntable;

import java.util.ArrayList;
import java.util.List;

public class RecipeMakerTurntable {
	private RecipeMakerTurntable(){}

	public static List<RecipeTurntable> getTurntableRecipes(IJeiHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
		RecipesTurntable autoclaveRecipes = RecipesTurntable.instance();
		List<ItemStack> inputList = autoclaveRecipes.getItemInputs();
		List<ItemStack> outputList = autoclaveRecipes.getItemOutputs();

		List<RecipeTurntable> recipes = new ArrayList<>();

		for (int i = 0, ii = inputList.size(); i < ii; i++) {
			ItemStack input = inputList.get(i);
			ItemStack output = outputList.get(i);

			List<ItemStack> inputs = stackHelper.getSubtypes(input);
			RecipeTurntable recipe = new RecipeTurntable(inputs, output);
			recipes.add(recipe);
		}

		return recipes;
	}
}
