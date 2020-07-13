package redfire.mods.simplemachinery.integration.jei.wrappers;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class WrapperAutoclave implements IRecipeWrapper {
	private final List<List<ItemStack>> inputs;
	private final List<List<FluidStack>> fluidInputs;
	private final ItemStack output;

	public WrapperAutoclave(List<ItemStack> inputs, List<FluidStack> fluidInputs, ItemStack output) {
		this.inputs = Collections.singletonList(inputs);
		this.fluidInputs = Collections.singletonList(fluidInputs);
		this.output = output;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setInputLists(VanillaTypes.FLUID, fluidInputs);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}
}
