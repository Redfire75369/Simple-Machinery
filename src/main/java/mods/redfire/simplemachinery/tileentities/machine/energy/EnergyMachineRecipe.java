package mods.redfire.simplemachinery.tileentities.machine.energy;

import mods.redfire.simplemachinery.tileentities.machine.MachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class EnergyMachineRecipe extends MachineRecipe {
	protected EnergyMachineRecipe(ResourceLocation id, int energy, int time, List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems, List<Float> outputItemChances, List<FluidStack> outputFluids) {
		super(id, energy, time, inputItems, inputFluids, outputItems, outputItemChances, outputFluids);
	}

	public int getEnergy() {
		return getResource();
	}

	public int getEnergyRate() {
		return getResourceRate();
	}
}
