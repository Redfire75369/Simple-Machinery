package redfire.mods.simplemachinery.integration.jei.wrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeWrapper;
import redfire.mods.simplemachinery.tileentities.generic.RecipeMachine;

public abstract class WrapperMachine<Recipe extends RecipeMachine> implements IRecipeWrapper {
    protected Recipe recipe;
    public WrapperMachine(IJeiHelpers jeiHelpers, Recipe recipe) {
        this.recipe = recipe;
    }
}
