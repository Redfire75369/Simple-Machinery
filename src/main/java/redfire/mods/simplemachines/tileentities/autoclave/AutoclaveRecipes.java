package redfire.mods.simplemachines.tileentities.autoclave;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			addAutoclaveRecipe(new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE_POWDER), 1, i), new ItemStack(Item.getItemFromBlock(Blocks.CONCRETE_POWDER), 1, i), FluidRegistry.getFluidStack("water", 1));
		}
	}

	public void addAutoclaveRecipe(ItemStack output, ItemStack input, FluidStack fluidInput) {
		if (getAutoclaveOutput(input) != ItemStack.EMPTY) {
			FMLLog.log.info("Ignored autoclave recipe with conflicting input: {} = {}", input, output);
			return;
		}
		itemInputs.add(input);
		itemOutputs.add(output);
		fluidInputs.add(fluidInput);
	}

	public ItemStack getAutoclaveOutput(ItemStack input) {
		for (int i = 0, ii = itemInputs.size(); i < ii; i++) {
			if (this.compareItemStacks(input, itemInputs.get(i))) {
				return itemInputs.get(i);
			}
		}

		return ItemStack.EMPTY;
	}

	public FluidStack getAutoclaveFluidInput(ItemStack input) {
		for (int i = 0, ii = itemInputs.size(); i < ii; i++) {
			if (this.compareItemStacks(input, itemInputs.get(i))) {
				return fluidInputs.get(i);
			}
		}

		return FluidRegistry.getFluidStack("water", 1);
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
}
