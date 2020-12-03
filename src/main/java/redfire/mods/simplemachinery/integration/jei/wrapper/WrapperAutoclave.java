package redfire.mods.simplemachinery.integration.jei.wrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import redfire.mods.simplemachinery.tileentities.autoclave.RecipeAutoclave;

import java.util.Arrays;

public class WrapperAutoclave extends WrapperMachine<RecipeAutoclave> {
    public WrapperAutoclave(IJeiHelpers helpers, RecipeAutoclave recipe) {
        super(helpers, recipe);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(recipe.inputs.get(0).getMatchingStacks()));
        ingredients.setInput(VanillaTypes.FLUID, recipe.fluidInputs.get(0));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.outputs.get(0));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }
}
