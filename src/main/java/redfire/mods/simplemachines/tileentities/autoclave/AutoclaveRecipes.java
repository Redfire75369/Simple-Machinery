package redfire.mods.simplemachines.tileentities.autoclave;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import redfire.mods.simplemachines.Blocks;

import java.util.Map;

public class AutoclaveRecipes {
	public static final AutoclaveRecipes autoclave_base = new AutoclaveRecipes();
	private final Map<ItemStack, ItemStack> recipeList = Maps.<ItemStack, ItemStack>newHashMap();
	private final Map<ItemStack, Integer> tickList = Maps.<ItemStack, Integer>newHashMap();


	public static AutoclaveRecipes instance() {
		return autoclave_base;
	}

	public AutoclaveRecipes() {
		this.addAutoclaveRecipe(new ItemStack(Item.getItemFromBlock(Blocks.regolith), 1, 0), new ItemStack(Item.getItemFromBlock(Blocks.autoclave), 1, 32767));
	}

	public void addAutoclaveRecipe(ItemStack output, ItemStack input) {
		if (getAutoclaveOutput(input) != ItemStack.EMPTY) {
			net.minecraftforge.fml.common.FMLLog.log.info("Ignored autoclave recipe with conflicting input: {} = {}", input, output);
			return;
		}
		this.recipeList.put(input, output);
	}

	public ItemStack getAutoclaveOutput(ItemStack input) {
		for (Map.Entry<ItemStack, ItemStack> entry : this.recipeList.entrySet()) {
			if (this.compareItemStacks(input, entry.getKey())) {
				return entry.getValue();
			}
		}

		return ItemStack.EMPTY;
	}

	public void getTicks(ItemStack input) {

	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
}
