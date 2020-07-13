package redfire.mods.simplemachinery.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import redfire.mods.simplemachinery.tileentities.turntable.RecipesTurntable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.simplemachinery.turntable")
public class CTTurntableRecipe {
	@ZenMethod("addTurntableRecipe")
	public static void addTurntableRecipe(IItemStack output, IItemStack input, int energy) {
		RecipesTurntable.instance().addTurntableRecipe(CraftTweakerMC.getItemStack(output), CraftTweakerMC.getItemStack(input), energy);
	}
}
