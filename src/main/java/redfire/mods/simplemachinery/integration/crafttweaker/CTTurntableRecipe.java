package redfire.mods.simplemachinery.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import redfire.mods.simplemachinery.tileentities.turntable.RecipesTurntable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.simplemachinery.Turntable")
public class CTTurntableRecipe {
	@ZenMethod("addRecipe")
	public static void addRecipe(String recipeName, IIngredient input, IItemStack output, int ticks, int power) {
		RecipesTurntable.instance().addRecipe("crafttweaker:" + recipeName, CraftTweakerMC.getIngredient(input), CraftTweakerMC.getItemStack(output), ticks, power);
	}

	@ZenMethod("removeRecipe")
	public static void removeRecipe(String recipeName) {
		RecipesTurntable.instance().recipes.remove(recipeName);
	}
}
