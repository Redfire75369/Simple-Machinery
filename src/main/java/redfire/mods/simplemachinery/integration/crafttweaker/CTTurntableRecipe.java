package redfire.mods.simplemachinery.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import redfire.mods.simplemachinery.tileentities.turntable.RecipesTurntable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.simplemachinery.turntable")
public class CTTurntableRecipe {
	@ZenMethod("addTurntableRecipe")
	public static void addTurntableRecipe(String recipeName, IIngredient input, IItemStack output, int ticks, int power) {
		RecipesTurntable.instance().addRecipe(recipeName, CraftTweakerMC.getIngredient(input), CraftTweakerMC.getItemStack(output), ticks, power);
	}

	@ZenMethod("removeTurntableRecipe")
	public static void removeTurntableRecipe(String recipeName) {
		RecipesTurntable.instance().recipes.remove(recipeName);
	}
}
