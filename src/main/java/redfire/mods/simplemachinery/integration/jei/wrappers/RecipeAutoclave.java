package redfire.mods.simplemachinery.integration.jei.wrappers;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class RecipeAutoclave implements IRecipeWrapper {
	private final List<List<ItemStack>> inputs;
	private final FluidStack fluidInput;
	private final ItemStack output;

	public RecipeAutoclave(List<ItemStack> inputs, FluidStack fluidInput, ItemStack output) {
		this.inputs = Collections.singletonList(inputs);
		this.fluidInput = fluidInput;
		this.output = output;
	}

	public RecipeAutoclave(IJeiHelpers jeiHelpers, RecipeAutoclave recipe) {
		this.inputs = recipe.inputs;
		this.fluidInput = recipe.fluidInput;
		this.output = recipe.output;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setInput(VanillaTypes.FLUID, fluidInput);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}
}
