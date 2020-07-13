package redfire.mods.simplemachinery.integration.jei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

public class CategoryAutoclave implements IRecipeCategory {
	public static String uid = "autoclave";
	public static String title = "Autoclave";

	@Override
	public String getUid() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public String getModName() {
		return null;
	}

	@Override
	public IDrawable getBackground() {
		return null;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {

	}
}
