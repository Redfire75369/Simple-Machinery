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

public class RecipeTurntable implements IRecipeWrapper {
	private final List<List<ItemStack>> inputs;
	private final ItemStack output;

	public RecipeTurntable(List<ItemStack> inputs, ItemStack output) {
		this.inputs = Collections.singletonList(inputs);
		this.output = output;
	}

	public RecipeTurntable(IJeiHelpers jeiHelpers, RecipeTurntable recipe) {
		this.inputs = recipe.inputs;
		this.output = recipe.output;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}
}