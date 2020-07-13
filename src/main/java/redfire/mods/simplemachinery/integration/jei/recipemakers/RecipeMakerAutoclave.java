package redfire.mods.simplemachinery.integration.jei.recipemakers;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import redfire.mods.simplemachinery.integration.jei.wrappers.RecipeAutoclave;
import redfire.mods.simplemachinery.tileentities.autoclave.RecipesAutoclave;

import java.util.ArrayList;
import java.util.List;

public class RecipeMakerAutoclave {
	private RecipeMakerAutoclave(){}

	public static List<RecipeAutoclave> getAutoclaveRecipes(IJeiHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
		RecipesAutoclave autoclaveRecipes = RecipesAutoclave.instance();
		List<ItemStack> inputList = autoclaveRecipes.getItemInputs();
		List<ItemStack> outputList = autoclaveRecipes.getItemOutputs();
		List<FluidStack> fluidInputList = autoclaveRecipes.getFluidInputs();

		List<RecipeAutoclave> recipes = new ArrayList<>();

		for (int i = 0, ii = inputList.size(); i < ii; i++) {
			ItemStack input = inputList.get(i);
			ItemStack output = outputList.get(i);
			FluidStack fluidInput = fluidInputList.get(i);

			List<ItemStack> inputs = stackHelper.getSubtypes(input);
			RecipeAutoclave recipe = new RecipeAutoclave(inputs, fluidInput, output);
			recipes.add(recipe);
		}

		return recipes;
	}
}
