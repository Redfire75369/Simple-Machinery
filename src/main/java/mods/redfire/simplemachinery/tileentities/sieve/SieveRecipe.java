/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.sieve;

import com.google.gson.JsonObject;
import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.tileentities.machine.energy.EnergyMachineRecipe;
import mods.redfire.simplemachinery.util.MachineCombinedInventory;
import mods.redfire.simplemachinery.util.recipe.ItemParser;
import mods.redfire.simplemachinery.util.recipe.NetworkParser;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SieveRecipe extends EnergyMachineRecipe {
	protected static final String RECIPE_NAME = "sieve";

	@ObjectHolder("simplemachinery:" + RECIPE_NAME)
	public static IRecipeSerializer<?> SERIALIZER = null;
	public static IRecipeType<SieveRecipe> RECIPE_TYPE = IRecipeType.register(new ResourceLocation(SimpleMachinery.MODID, RECIPE_NAME).toString());

	public SieveRecipe(ResourceLocation id, Ingredient input, List<ItemStack> outputs, List<Float> outputChances, int energy, int time) {
		super(id, energy, time, Collections.singletonList(input), null, outputs, outputChances, null);
	}

	public static Optional<SieveRecipe> getRecipe(World world, MachineCombinedInventory inv) {
		return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, world);
	}

	public static Collection<SieveRecipe> getAllRecipes(World world) {
		return world.getRecipeManager().getAllRecipesFor(RECIPE_TYPE);
	}

	@Nonnull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(Blocks.SIEVE_BLOCK.get());
	}

	@Nonnull
	@Override
	public IRecipeType<?> getType() {
		return RECIPE_TYPE;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
			implements IRecipeSerializer<SieveRecipe> {
		@Nonnull
		@Override
		public SieveRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			Ingredient input = ItemParser.parseIngredient(json, "input");
			Tuple<List<ItemStack>, List<Float>> weightedOutputs = ItemParser.mapWeightedItems(ItemParser.parseWeightedItems(json, "outputs"));
			List<ItemStack> outputs = weightedOutputs.getA();
			List<Float> outputChances = weightedOutputs.getB();

			int energy = JSONUtils.getAsInt(json, "energy", 4000);
			int time = JSONUtils.getAsInt(json, "time", 200);

			return new SieveRecipe(recipeId, input, outputs, outputChances, energy, time);
		}

		@Override
		public SieveRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			Ingredient input = Ingredient.fromNetwork(buffer);
			Tuple<List<ItemStack>, List<Float>> weightedOutputs = ItemParser.mapWeightedItems(NetworkParser.readWeightedItemsFromNetwork(buffer));
			List<ItemStack> outputs = weightedOutputs.getA();
			List<Float> outputChances = weightedOutputs.getB();

			int energy = buffer.readInt();
			int time = buffer.readInt();

			return new SieveRecipe(recipeId, input, outputs, outputChances, energy, time);
		}


		@Override
		public void toNetwork(@Nonnull PacketBuffer buffer, SieveRecipe recipe) {
			recipe.inputItems.get(0).toNetwork(buffer);
			NetworkParser.writeItemsToNetwork(buffer, recipe.getOutputItems());
			buffer.writeInt(recipe.resource);
			buffer.writeInt(recipe.time);
		}
	}
}
