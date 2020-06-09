package redfire.mods.simplemachinery.tileentities.autoclave;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class AutoclaveRecipes {
	public static final AutoclaveRecipes autoclave_base = new AutoclaveRecipes();
	private List<ItemStack> itemInputs = new ArrayList();
	private List<ItemStack> itemOutputs = new ArrayList();
	private List<FluidStack> fluidInputs = new ArrayList();

	public static AutoclaveRecipes instance() {
		return autoclave_base;
	}

	public AutoclaveRecipes() {
		for (int i = 0; i < 16; i++) {
			addAutoclaveRecipe(new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE), 1, i), new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE_POWDER), 1, i), FluidRegistry.getFluidStack("water", 125));
		}
	}

	public void addAutoclaveRecipe(ItemStack output, ItemStack input, FluidStack fluidInput) {
		if (getAutoclaveOutput(input, fluidInput) != ItemStack.EMPTY) {
			FMLLog.log.info("Ignored autoclave recipe with conflicting input: {} = {}", input, output);
			return;
		}
		itemInputs.add(input);
		itemOutputs.add(output);
		fluidInputs.add(fluidInput);
	}

	public ItemStack getAutoclaveOutput(ItemStack input, FluidStack fluidInput) {
		for (int i = 0, ii = itemInputs.size(); i < ii; i++) {
			if (this.compareItemStacks(input, itemInputs.get(i)) && this.compareFluids(fluidInput.getFluid(), fluidInputs.get(i).getFluid())) {
				return itemOutputs.get(i);
			}
		}

		return ItemStack.EMPTY;
	}

	public FluidStack getAutoclaveFluidInput(ItemStack input , FluidStack fluidInput) {
		for (int i = 0, ii = itemInputs.size(); i < ii; i++) {
			if (this.compareItemStacks(input, itemInputs.get(i)) && this.compareFluids(fluidInput.getFluid(), fluidInputs.get(i).getFluid())) {
				return fluidInputs.get(i);
			}
		}

		return null;
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
	private boolean compareFluidStacks(FluidStack stack1, FluidStack stack2) {
		if (isNull(stack1) || isNull(stack2)) {
			return false;
		}
		return stack1.getFluid() == stack2.getFluid() && stack1.amount == stack2.amount;
	}
	private boolean compareFluids(Fluid fluid1, Fluid fluid2) {
		return compareFluidStacks(new FluidStack(fluid1, 1000), new FluidStack(fluid2, 1000));
	}
}
