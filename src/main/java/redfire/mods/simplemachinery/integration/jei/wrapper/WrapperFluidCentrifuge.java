package redfire.mods.simplemachinery.integration.jei.wrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.RecipeFluidCentrifuge;

public class WrapperFluidCentrifuge extends WrapperMachine<RecipeFluidCentrifuge> {
    public WrapperFluidCentrifuge(IJeiHelpers jeiHelpers, RecipeFluidCentrifuge recipe) {
        super(jeiHelpers, recipe);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, recipe.fluidInputs.get(0));
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.outputs);
        ingredients.setOutputs(VanillaTypes.FLUID, recipe.fluidOutputs);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }
}
