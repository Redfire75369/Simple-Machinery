package redfire.mods.simplemachinery.tileentities.fluidcentrifuge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import redfire.mods.simplemachinery.tileentities.generic.RecipeMachine;

import java.util.Collections;
import java.util.List;

public class RecipeFluidCentrifuge extends RecipeMachine {
    public RecipeFluidCentrifuge(List<ItemStack> outputs, FluidStack fluidInput, List<FluidStack> fluidOutputs, int ticks, int power) {
        super(Collections.singletonList(Ingredient.EMPTY), outputs, Collections.singletonList(fluidInput), fluidOutputs, ticks, power);
    }
}
