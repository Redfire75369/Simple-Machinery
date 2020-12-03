package redfire.mods.simplemachinery.integration.jei.recipemakers;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import redfire.mods.simplemachinery.integration.jei.wrapper.WrapperTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.RecipeTurntable;
import redfire.mods.simplemachinery.tileentities.turntable.RecipesTurntable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeMakerTurntable {
	private RecipeMakerTurntable(){}

	public static List<WrapperTurntable> getRecipes(IJeiHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
		HashMap<String, RecipeTurntable> recipes = RecipesTurntable.instance().recipes;

		List<WrapperTurntable> wrappedRecipes = new ArrayList<>();

		recipes.forEach((name, recipe) -> {
			wrappedRecipes.add(new WrapperTurntable(helpers, recipe));
		});

		return wrappedRecipes;
	}
}
