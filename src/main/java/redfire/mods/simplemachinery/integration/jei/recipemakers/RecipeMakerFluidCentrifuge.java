package redfire.mods.simplemachinery.integration.jei.recipemakers;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import redfire.mods.simplemachinery.integration.jei.wrapper.WrapperFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.RecipeFluidCentrifuge;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.RecipesFluidCentrifuge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeMakerFluidCentrifuge {
    private RecipeMakerFluidCentrifuge() {}

    public static List<WrapperFluidCentrifuge> getRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        HashMap<String, RecipeFluidCentrifuge> recipes = RecipesFluidCentrifuge.instance().recipes;

        List<WrapperFluidCentrifuge> wrappedRecipes = new ArrayList<>();

        recipes.forEach((name, recipe) -> {
            wrappedRecipes.add(new WrapperFluidCentrifuge(helpers, recipe));
        });

        return wrappedRecipes;
    }
}
