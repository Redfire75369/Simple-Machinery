package mods.redfire.simplemachinery.tileentities.machine;

import com.google.common.primitives.Ints;
import mods.redfire.simplemachinery.util.inventory.MachineInventory;
import mods.redfire.simplemachinery.util.inventory.MachineItemSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class MachineRecipe implements IRecipe<MachineInventory> {
	protected final ResourceLocation id;
	protected final List<Ingredient> inputItems = new ArrayList<>();
	protected final List<ItemStack> outputItems = new ArrayList<>();
	protected final List<Float> outputItemChances = new ArrayList<>();

	protected int energy;
	protected int time;

	protected MachineRecipe(ResourceLocation id, int energy, int time, List<Ingredient> inputItems, List<ItemStack> outputItems, List<Float> outputItemChances) {
		this.id = id;

		this.energy = energy;
		this.time = time;

		if (inputItems != null) {
			this.inputItems.addAll(inputItems);
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

		trim();
	}

	private void trim() {
		((ArrayList<Ingredient>) this.inputItems).trimToSize();
		((ArrayList<ItemStack>) this.outputItems).trimToSize();
		((ArrayList<Float>) this.outputItemChances).trimToSize();
	}

	public List<Ingredient> getInputItems() {
		return inputItems;
	}

	public List<Integer> getInputItemCounts(List<MachineItemSlot> inputSlots) {
		int[] counts = new int[inputSlots.size()];
		boolean[] used = new boolean[inputSlots.size()];
		for (Ingredient input : inputItems) {
			boolean matched = false;
			for (int i = 0; i < inputSlots.size(); i++) {
				if (!used[i] && !matched) {
					MachineItemSlot slot = inputSlots.get(i);
					ItemStack item = slot.getItemStack();

					if (input.test(item)) {
						counts[i] = input.getItems()[0].getCount();
						used[i] = true;
						matched = true;
					}
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

	public int getEnergy() {
		return energy;
	}

	public int getTime() {
		return time;
	}

	public int getEnergyRate() {
		return getEnergy() / getTime();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public boolean matches(@Nonnull MachineInventory inv, @Nonnull World worldIn) {
		boolean[] used = new boolean[inv.getContainerSize()];
		for (Ingredient input : inputItems) {
			boolean matched = false;
			for (int i = 0; i < inv.getContainerSize(); i++) {
				if (!used[i] && !matched) {
					if (!input.test(inv.get(i))) {
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
	public ItemStack assemble(@Nonnull MachineInventory inv) {
		return getResultItem();
	}

	@Nonnull
	@Override
	public abstract IRecipeSerializer<?> getSerializer();

	@Nonnull
	@Override
	public abstract IRecipeType<?> getType();
}
