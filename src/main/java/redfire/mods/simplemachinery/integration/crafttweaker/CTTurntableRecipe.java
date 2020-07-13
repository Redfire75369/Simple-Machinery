package redfire.mods.simplemachinery.integration.crafttweaker;

import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import redfire.mods.simplemachinery.tileentities.turntable.RecipesTurntable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.simplemachinery.turntable")
public class CTTurntableRecipe {
	@ZenMethod("addTurntableRecipe")
	public static void addAutoclaveRecipe(IItemStack output, IItemStack input, int energyUsage) {
		RecipesTurntable.addTurntableRecipe(CraftTweakerMC.getItemStack(output), CraftTweakerMC.getItemStack(input), energyUsage);
	}
}
