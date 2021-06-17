/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.util.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class NetworkParser {
	public static List<Ingredient> readIngredientsFromNetwork(@Nonnull PacketBuffer buffer) {
		int size = buffer.readInt();
		List<Ingredient> ingredients = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			ingredients.add(Ingredient.fromNetwork(buffer));
		}
		return ingredients;
	}

	public static List<ItemStack> readItemsFromNetwork(@Nonnull PacketBuffer buffer) {
		int size = buffer.readInt();
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			items.add(buffer.readItem());
		}
		return items;
	}

	public static List<Tuple<ItemStack, Float>> readWeightedItemsFromNetwork(@Nonnull PacketBuffer buffer) {
		int size = buffer.readInt();
		List<Tuple<ItemStack, Float>> weightedItems = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			weightedItems.add(new Tuple<>(buffer.readItem(), buffer.readFloat()));
		}
		return weightedItems;
	}

	public static List<FluidStack> readFluidsFromNetwork(@Nonnull PacketBuffer buffer) {
		int size = buffer.readInt();
		List<FluidStack> fluids = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			fluids.add(buffer.readFluidStack());
		}
		return fluids;
	}

	public static void writeIngredientsToNetwork(@Nonnull PacketBuffer buffer, List<Ingredient> ingredients) {
		buffer.writeInt(ingredients.size());
		for (Ingredient ingredient : ingredients) {
			ingredient.toNetwork(buffer);
		}
	}

	public static void writeItemsToNetwork(@Nonnull PacketBuffer buffer, List<ItemStack> items) {
		buffer.writeInt(items.size());
		for (ItemStack item : items) {
			buffer.writeItem(item);
		}
	}

	public static void writeWeightedItemsToNetwork(@Nonnull PacketBuffer buffer, List<Tuple<ItemStack, Float>> weightedItems) {
		buffer.writeInt(weightedItems.size());
		for (Tuple<ItemStack, Float> weightedItem : weightedItems) {
			buffer.writeItem(weightedItem.getA());
			buffer.writeFloat(weightedItem.getB());
		}
	}

	public static void writeFluidsToNetwork(@Nonnull PacketBuffer buffer, List<FluidStack> fluids) {
		buffer.writeInt(fluids.size());
		for (FluidStack fluid : fluids) {
			buffer.writeFluidStack(fluid);
		}
	}
}
