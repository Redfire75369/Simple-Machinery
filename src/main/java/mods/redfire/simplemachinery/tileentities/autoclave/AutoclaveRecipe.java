/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.autoclave;

import com.google.gson.JsonObject;
import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.tileentities.machine.fluid.FluidMachineRecipe;
import mods.redfire.simplemachinery.util.MachineCombinedInventory;
import mods.redfire.simplemachinery.util.recipe.FluidParser;
import mods.redfire.simplemachinery.util.recipe.ItemParser;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class AutoclaveRecipe extends FluidMachineRecipe {
	protected static final String RECIPE_NAME = "autoclave";

	@ObjectHolder("simplemachinery:" + RECIPE_NAME)
	public static IRecipeSerializer<?> SERIALIZER = null;
	public static IRecipeType<AutoclaveRecipe> RECIPE_TYPE = IRecipeType.register(new ResourceLocation(SimpleMachinery.MODID, RECIPE_NAME).toString());

	public AutoclaveRecipe(ResourceLocation id, Ingredient input, FluidStack fluidInput, ItemStack output, int steam, int time) {
		super(id, steam, time, Collections.singletonList(input), Collections.singletonList(fluidInput), Collections.singletonList(output), null, null);
	}

	public static Optional<AutoclaveRecipe> getRecipe(World world, MachineCombinedInventory inv) {
		return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, world);
	}

	public static Collection<AutoclaveRecipe> getAllRecipes(World world) {
		return world.getRecipeManager().getAllRecipesFor(RECIPE_TYPE);
	}

	@Nonnull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(Blocks.AUTOCLAVE_BLOCK.get());
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
			implements IRecipeSerializer<AutoclaveRecipe> {
		@Nonnull
		@Override
		public AutoclaveRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			Ingredient input = ItemParser.parseIngredient(json, "input");
			ItemStack output = ItemParser.parseItem(json, "output");
			FluidStack fluidInput = FluidParser.parseFluid(json, "fluid_input");

			int steam = JSONUtils.getAsInt(json, "steam", 4000);
			int time = JSONUtils.getAsInt(json, "time", 200);

			return new AutoclaveRecipe(recipeId, input, fluidInput, output, steam, time);
		}

		@Override
		public AutoclaveRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			Ingredient input = Ingredient.fromNetwork(buffer);
			FluidStack fluidInput = FluidStack.readFromPacket(buffer);
			ItemStack output = buffer.readItem();
			int steam = buffer.readInt();
			int time = buffer.readInt();

			return new AutoclaveRecipe(recipeId, input, fluidInput, output, steam, time);
		}

		@Override
		public void toNetwork(@Nonnull PacketBuffer buffer, AutoclaveRecipe recipe) {
			recipe.inputItems.get(0).toNetwork(buffer);
			buffer.writeFluidStack(recipe.inputFluids.get(0));
			buffer.writeItem(recipe.outputItems.get(0));
			buffer.writeInt(recipe.resource);
			buffer.writeInt(recipe.time);
		}
	}
}
