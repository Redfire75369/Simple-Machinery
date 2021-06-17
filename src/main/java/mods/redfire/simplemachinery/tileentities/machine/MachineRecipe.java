/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.machine;

import com.google.common.primitives.Ints;
import mods.redfire.simplemachinery.util.MachineCombinedInventory;
import mods.redfire.simplemachinery.util.fluid.MachineFluidInventory;
import mods.redfire.simplemachinery.util.inventory.MachineInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MachineRecipe implements IRecipe<MachineCombinedInventory> {
	protected final ResourceLocation id;
	protected final List<Ingredient> inputItems = new ArrayList<>();
	protected final List<FluidStack> inputFluids = new ArrayList<>();

	protected final List<ItemStack> outputItems = new ArrayList<>();
	protected final List<Float> outputItemChances = new ArrayList<>();
	protected final List<FluidStack> outputFluids = new ArrayList<>();

	protected int resource;
	protected int time;

	protected MachineRecipe(ResourceLocation id, int resource, int time, List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems, List<Float> outputItemChances, List<FluidStack> outputFluids) {
		this.id = id;

		this.resource = resource;
		this.time = time;

		if (inputItems != null) {
			this.inputItems.addAll(inputItems);
		}
		if (inputFluids != null) {
			this.inputFluids.addAll(inputFluids);
		}

		if (outputItems != null) {
			this.outputItems.addAll(outputItems);

			if (outputItemChances != null) {
				this.outputItemChances.addAll(outputItemChances);
			}
			if (this.outputItemChances.size() < this.outputItems.size()) {
				for (int i = this.outputItemChances.size(); i < this.outputItems.size(); ++i) {
					this.outputItemChances.add(1.0F);
				}
			}
		}
		if (outputFluids != null) {
			this.outputFluids.addAll(outputFluids);
		}

		trim();
	}

	private void trim() {
		((ArrayList<Ingredient>) this.inputItems).trimToSize();
		((ArrayList<FluidStack>) this.inputFluids).trimToSize();

		((ArrayList<ItemStack>) this.outputItems).trimToSize();
		((ArrayList<Float>) this.outputItemChances).trimToSize();
		((ArrayList<FluidStack>) this.outputFluids).trimToSize();
	}

	public List<Ingredient> getInputItems() {
		return inputItems;
	}

	public List<Integer> getInputItemCounts(MachineInventory inventory) {
		if (inputItems.isEmpty()) {
			return Collections.emptyList();
		}
		int[] counts = new int[inventory.getInputSlots().size()];
		for (Ingredient input : inputItems) {
			for (int j = 0; j < counts.length; ++j) {
				if (input.test(inventory.getInputSlots().get(j).getItemStack())) {
					counts[j] = input.getItems()[0].getCount();
					break;
				}
			}
		}
		return Ints.asList(counts);
	}

	public List<FluidStack> getInputFluids() {
		return inputFluids;
	}

	public List<Integer> getInputFluidCounts(MachineFluidInventory tankInventory) {
		if (inputFluids.isEmpty()) {
			return Collections.emptyList();
		}
		int[] counts = new int[tankInventory.getInputTanks().size()];
		for (FluidStack input : inputFluids) {
			for (int j = 0; j < counts.length; ++j) {
				if (FluidStack.areFluidStackTagsEqual(input, tankInventory.getInputTanks().get(j).getFluidStack())) {
					counts[j] = input.getAmount();
					break;
				}
			}
		}
		return Ints.asList(counts);
	}

	public List<ItemStack> getOutputItems() {
		return outputItems;
	}

	public List<Float> getOutputItemChances() {
		return outputItemChances;
	}

	public List<Tuple<ItemStack, Float>> getWeightedOutputItems() {
		List<Tuple<ItemStack, Float>> weightedOutputItems = new ArrayList<>();
		for (int i = 0; i < outputItems.size(); i++) {
			weightedOutputItems.add(new Tuple<>(outputItems.get(i), outputItemChances.get(i)));
		}
		return weightedOutputItems;
	}

	public List<FluidStack> getOutputFluids() {
		return outputFluids;
	}

	public int getResource() {
		return resource;
	}

	public int getTime() {
		return time;
	}

	public int getResourceRate() {
		return getResource() / getTime();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public boolean matches(@Nonnull MachineCombinedInventory inv, @Nonnull World worldIn) {
		MachineInventory inventory = inv.inventory;
		boolean[] used = new boolean[inventory.getContainerSize()];
		for (Ingredient input : inputItems) {
			boolean matched = false;
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				if (!used[i] && !matched) {
					if (!input.test(inventory.get(i))) {
						return false;
					}
					used[i] = true;
					matched = true;
				}
			}
		}

		MachineFluidInventory tankInventory = inv.tankInventory;
		used = new boolean[tankInventory.getContainerSize()];
		for (FluidStack fluid : inputFluids) {
			boolean matched = false;
			for (int i = 0; i < tankInventory.getContainerSize(); i++) {
				if (!used[i] && !matched) {
					if (!(fluid.getFluid().isSame(tankInventory.get(i).getFluid()) && fluid.getAmount() <= tankInventory.get(i).getAmount())) {
						return false;
					}
					used[i] = true;
					matched = true;
				}
			}
		}

		return true;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();
		list.addAll(inputItems);
		return list;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem() {
		return outputItems.get(0);
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull MachineCombinedInventory inv) {
		return getResultItem();
	}

	@Nonnull
	@Override
	public abstract IRecipeSerializer<?> getSerializer();

	@Nonnull
	@Override
	public abstract IRecipeType<?> getType();
}
