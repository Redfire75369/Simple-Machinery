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
@ZenClass("mods.simplemachinery.autoclave")
public class CTAutoclaveRecipe {
	@ZenMethod("addAutoclaveRecipe")
	public static void addAutoclaveRecipe(String recipeName, IIngredient input, IItemStack output, ILiquidStack fluidInput, int ticks, int steamPower) {
		RecipesAutoclave.instance().addAutoclaveRecipe(recipeName, CraftTweakerMC.getIngredient(input), CraftTweakerMC.getItemStack(output), CraftTweakerMC.getLiquidStack(fluidInput), ticks, steamPower);
	}

	@ZenMethod("removeAutoclaveRecipe")
	public static void removeAutoclaveRecipe(String recipeName) {
		RecipesAutoclave.instance().recipes.remove(recipeName);
	}
}
