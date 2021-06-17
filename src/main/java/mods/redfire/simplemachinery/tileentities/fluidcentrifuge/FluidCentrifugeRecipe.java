/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.fluidcentrifuge;

import com.google.gson.JsonObject;
import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.tileentities.machine.energy.EnergyMachineRecipe;
import mods.redfire.simplemachinery.util.MachineCombinedInventory;
import mods.redfire.simplemachinery.util.recipe.FluidParser;
import mods.redfire.simplemachinery.util.recipe.ItemParser;
import mods.redfire.simplemachinery.util.recipe.NetworkParser;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FluidCentrifugeRecipe extends EnergyMachineRecipe {
	protected static final String RECIPE_NAME = "fluid_centrifuge";

	@ObjectHolder("simplemachinery:" + RECIPE_NAME)
	public static IRecipeSerializer<?> SERIALIZER = null;
	public static IRecipeType<FluidCentrifugeRecipe> RECIPE_TYPE = IRecipeType.register(new ResourceLocation(SimpleMachinery.MODID, RECIPE_NAME).toString());

	public FluidCentrifugeRecipe(ResourceLocation id, FluidStack fluidInput, List<ItemStack> itemOutputs, List<Float> itemOutputChances, List<FluidStack> fluidOutputs, int energy, int time) {
		super(id, energy, time, null, Collections.singletonList(fluidInput), itemOutputs, itemOutputChances, fluidOutputs);
	}

	public static Optional<FluidCentrifugeRecipe> getRecipe(World world, MachineCombinedInventory inv) {
		return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, world);
	}

	public static Collection<FluidCentrifugeRecipe> getAllRecipes(World world) {
		return world.getRecipeManager().getAllRecipesFor(RECIPE_TYPE);
	}

	@Nonnull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(Blocks.FLUID_CENTRIFUGE_BLOCK.get());
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
			implements IRecipeSerializer<FluidCentrifugeRecipe> {
		@Nonnull
		@Override
		public FluidCentrifugeRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			FluidStack fluidInput = FluidParser.parseFluid(json, "fluid_input");
			Tuple<List<ItemStack>, List<Float>> weightedOutputs = ItemParser.mapWeightedItems(ItemParser.parseWeightedItems(json, "outputs"));
			List<ItemStack> outputs = weightedOutputs.getA();
			List<Float> outputChances = weightedOutputs.getB();
			List<FluidStack> fluidOutputs = FluidParser.parseFluids(json, "fluid_outputs");

			int energy = JSONUtils.getAsInt(json, "energy", 4000);
			int time = JSONUtils.getAsInt(json, "time", 200);

			return new FluidCentrifugeRecipe(recipeId, fluidInput, outputs, outputChances, fluidOutputs, energy, time);
		}

		@Override
		public FluidCentrifugeRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			FluidStack fluidInput = FluidStack.readFromPacket(buffer);
			Tuple<List<ItemStack>, List<Float>> weightedOutputs = ItemParser.mapWeightedItems(NetworkParser.readWeightedItemsFromNetwork(buffer));
			List<ItemStack> outputs = weightedOutputs.getA();
			List<Float> outputChances = weightedOutputs.getB();
			List<FluidStack> fluidOutputs = NetworkParser.readFluidsFromNetwork(buffer);

			int energy = buffer.readInt();
			int time = buffer.readInt();

			return new FluidCentrifugeRecipe(recipeId, fluidInput, outputs, outputChances, fluidOutputs, energy, time);
		}


		@Override
		public void toNetwork(@Nonnull PacketBuffer buffer, FluidCentrifugeRecipe recipe) {
			buffer.writeFluidStack(recipe.inputFluids.get(0));
			NetworkParser.writeWeightedItemsToNetwork(buffer, recipe.getWeightedOutputItems());
			NetworkParser.writeFluidsToNetwork(buffer, recipe.getOutputFluids());
			buffer.writeInt(recipe.resource);
			buffer.writeInt(recipe.time);
		}
	}
}
