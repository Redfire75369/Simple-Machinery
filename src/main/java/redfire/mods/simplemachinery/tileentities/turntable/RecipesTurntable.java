package redfire.mods.simplemachinery.tileentities.turntable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.FMLLog;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class RecipesTurntable {
	public static final RecipesTurntable turntable_base = new RecipesTurntable();
	public final HashMap<String, RecipeTurntable> recipes = new HashMap<>();

	public static RecipesTurntable instance() {
		return turntable_base;
	}

	public RecipesTurntable() {}

	public void addRecipe(String recipeName, Ingredient input, ItemStack output, int ticks, int power) {
		if (recipes.containsKey(recipeName)) {
			FMLLog.log.warn("Ignored turntable recipe with recipe name: {}", recipeName);
			return;
		}
		if (getOutput(recipeName) != ItemStack.EMPTY) {
			FMLLog.log.warn("Ignored turntable recipe with conflicting input: {} = {}", input, output);
			return;
		}

		recipes.put(recipeName, new RecipeTurntable(input, output, ticks, power));
	}

	public Ingredient getInput(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).inputs.get(0);
		}
		return Ingredient.EMPTY;
	}
	public ItemStack getOutput(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).outputs.get(0);
		}
		return ItemStack.EMPTY;
	}
	public int getPower(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).power;
		}
		return 0;
	}
	public int getTicks(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).ticks;
		}
		return 0;
	}
	public int getTotalEnergy(String recipeName) {
		if (recipes.get(recipeName) != null) {
			return recipes.get(recipeName).getTotalEnergy();
		}
		return 0;
	}

	public static String searchRecipes(ItemStack input) {
		HashMap<String, RecipeTurntable> recipes = instance().recipes;
		AtomicReference<String> result = new AtomicReference<>("");
		recipes.forEach((name, recipe) -> {
			if (recipe.inputs.get(0).apply(input)) {
				result.set(name);
			}
		});
		return result.get();
	}
}
