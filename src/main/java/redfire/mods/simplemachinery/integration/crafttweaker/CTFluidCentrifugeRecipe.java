package redfire.mods.simplemachinery.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import redfire.mods.simplemachinery.tileentities.fluidcentrifuge.RecipesFluidCentrifuge;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenRegister
@ZenClass("mods.simplemachinery.FluidCentrifuge")
public class CTFluidCentrifugeRecipe {
    @ZenMethod("addRecipe")
    public static void addRecipe(String recipeName, ILiquidStack fluidInput, IItemStack[] outputs, ILiquidStack[] fluidOutputs, int ticks, int power) {
        RecipesFluidCentrifuge.instance().addRecipe("crafttweaker:" + recipeName, Arrays.asList(CraftTweakerMC.getItemStacks(outputs)), CraftTweakerMC.getLiquidStack(fluidInput), Arrays.asList(CraftTweakerMC.getLiquidStacks(fluidOutputs)), ticks, power);
    }

    @ZenMethod("removeRecipe")
    public static void removeRecipe(String recipeName) {
        RecipesFluidCentrifuge.instance().recipes.remove(recipeName);
    }
}
