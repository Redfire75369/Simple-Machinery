package redfire.mods.simplemachinery.tileentities.turntable;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;

import java.util.ArrayList;
import java.util.List;

public class RecipesTurntable {
	public static final RecipesTurntable turntable_base = new RecipesTurntable();
	private List<ItemStack> itemInputs = new ArrayList();
	private List<ItemStack> itemOutputs = new ArrayList();
	private IntList energyInputs = new IntArrayList();

	public static RecipesTurntable instance() {
		return turntable_base;
	}

	public RecipesTurntable() {}

	public void addTurntableRecipe(ItemStack output, ItemStack input, int energy) {
		if (getTurntableOutput(input) != ItemStack.EMPTY) {
			FMLLog.log.info("Ignored autoclave recipe with conflicting input: {} = {}", input, output);
			return;
		}
		itemInputs.add(input);
		itemOutputs.add(output);
		energyInputs.add(energy);
	}

	public ItemStack getTurntableOutput(ItemStack input) {
		for (int i = 0, ii = itemInputs.size(); i < ii; i++) {
			if (compareItemStacks(input, itemInputs.get(i))) {
				return itemOutputs.get(i);
			}
		}

		return ItemStack.EMPTY;
	}
	public int getTurntableEnergyRequired(ItemStack input) {
		for (int i = 0, ii = itemInputs.size(); i < ii; i++) {
			if (compareItemStacks(input, itemInputs.get(i))) {
				return energyInputs.get(i);
			}
		}

		return 0;
	}

	public List<ItemStack> getItemInputs() {
		return itemInputs;
	}
	public List<ItemStack> getItemOutputs() {
		return itemOutputs;
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
}
