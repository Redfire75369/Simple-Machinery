package redfire.mods.simplemachinery.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import redfire.mods.simplemachinery.tileentities.autoclave.RecipesAutoclave;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.simplemachinery.Autoclave")
public class CTAutoclaveRecipe {
	@ZenMethod("addRecipe")
	public static void addRecipe(String recipeName, IIngredient input, ILiquidStack fluidInput, IItemStack output, int ticks, int steamPower) {
		RecipesAutoclave.instance().addAutoclaveRecipe("crafttweaker:" + recipeName, CraftTweakerMC.getIngredient(input), CraftTweakerMC.getItemStack(output), CraftTweakerMC.getLiquidStack(fluidInput), ticks, steamPower);
	}

	@ZenMethod("removeRecipe")
	public static void removeRecipe(String recipeName) {
		RecipesAutoclave.instance().recipes.remove(recipeName);
	}
}
