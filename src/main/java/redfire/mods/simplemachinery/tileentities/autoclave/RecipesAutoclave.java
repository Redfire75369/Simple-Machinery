package redfire.mods.simplemachinery.tileentities.autoclave;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class RecipesAutoclave {
	public static final RecipesAutoclave autoclave_base = new RecipesAutoclave();
	public final HashMap<String, RecipeAutoclave> recipes = new HashMap<>();

	public static RecipesAutoclave instance() {
		return autoclave_base;
	}

	public RecipesAutoclave() {
		for (int i = 0; i < 16; i++) {
			addAutoclaveRecipe("simplemachinery:concrete_" + i,
					Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE_POWDER), 1, i)),
					new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE), 1, i),
					FluidRegistry.getFluidStack("water", 125),
					30,
					5
			);
		}
	}

	public void addAutoclaveRecipe(String recipeName, Ingredient input,  ItemStack output, FluidStack fluidInput, int ticks, int steamPower) {
		if (recipes.containsKey(recipeName)) {
			FMLLog.log.warn("Ignored autoclave recipe with recipe name: {}", recipeName);
			return;
		}
		if (getOutput(recipeName) != ItemStack.EMPTY) {
			FMLLog.log.warn("Ignored autoclave recipe with conflicting input: {} = {}", input, output);
			return;
		}

		recipes.put(recipeName, new RecipeAutoclave(input,
				output,
				fluidInput,
				ticks,
				steamPower
		));
	}

	public void removeAutoclaveRecipe(String recipeName) {

	}

	public ItemStack getOutput(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).outputs.get(0);
		}
		return ItemStack.EMPTY;
	}
	public FluidStack getFluidInput(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).fluidInputs.get(0);
		}
		return null;
	}
	public int getSteamPower(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).steamPower;
		}
		return 0;
	}
	public int getTicks(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).ticks;
		}
		return 0;
	}
	public int getTotalSteam(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).getTotalSteam();
		}
		return 0;
	}

	public static String searchRecipes(ItemStack input, FluidStack fluidInput) {
		HashMap<String, RecipeAutoclave> recipes = instance().recipes;
		AtomicReference<String> result = new AtomicReference<>("");
		recipes.forEach((name, recipe) -> {
			if (recipe.inputs.get(0).apply(input)
					&& fluidInput != null && Objects.equals(recipe.fluidInputs.get(0).getFluid().getName(), fluidInput.getFluid().getName())) {
				result.set(name);
			}
		});
		return result.get();
	}
}
