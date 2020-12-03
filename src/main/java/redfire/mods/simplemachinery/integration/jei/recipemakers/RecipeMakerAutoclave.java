package redfire.mods.simplemachinery.integration.jei.recipemakers;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import redfire.mods.simplemachinery.integration.jei.wrapper.WrapperAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.RecipeAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.RecipesAutoclave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeMakerAutoclave {
    private RecipeMakerAutoclave(){}

    public static List<WrapperAutoclave> getRecipes(IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();
        HashMap<String, RecipeAutoclave> recipes = RecipesAutoclave.instance().recipes;

        List<WrapperAutoclave> wrappedRecipes = new ArrayList<>();

        recipes.forEach((name, recipe) -> {
            wrappedRecipes.add(new WrapperAutoclave(helpers, recipe));
        });

        return wrappedRecipes;
    }
}
