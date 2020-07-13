package redfire.mods.simplemachinery.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
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
	public static void addAutoclaveRecipe(IItemStack output, IItemStack input, ILiquidStack fluidInput, int steamUsage, int ticks) {
		RecipesAutoclave.instance().addAutoclaveRecipe(CraftTweakerMC.getItemStack(output), CraftTweakerMC.getItemStack(input), CraftTweakerMC.getLiquidStack(fluidInput), steamUsage, ticks);
	}
}
