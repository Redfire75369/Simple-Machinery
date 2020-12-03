package redfire.mods.simplemachinery.tileentities.generic;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

public class RecipeMachine extends IForgeRegistryEntry.Impl<IRecipe> {
    public List<Ingredient> inputs;
    public List<ItemStack> outputs;
    public List<FluidStack> fluidInputs;
    public List<FluidStack> fluidOutputs;
    public int ticks;
    public int power;

    public RecipeMachine(List<Ingredient> inputs, List<ItemStack> outputs, List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs, int ticks, int power) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.fluidInputs = fluidInputs;
        this.fluidOutputs = fluidOutputs;
        this.ticks = ticks;
        this.power = power;
    }
}
