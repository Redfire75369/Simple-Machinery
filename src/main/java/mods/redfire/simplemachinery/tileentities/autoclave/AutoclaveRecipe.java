package mods.redfire.simplemachinery.tileentities.autoclave;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.tileentities.machine.MachineRecipe;
import mods.redfire.simplemachinery.tileentities.machine.fluid.FluidMachineRecipe;
import mods.redfire.simplemachinery.util.MachineCombinedInventory;
import mods.redfire.simplemachinery.util.inventory.MachineInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
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

	public AutoclaveRecipe(ResourceLocation id, Ingredient input, FluidStack fluidInput, ItemStack output,  int steam, int time) {
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
			JsonElement jsonelement = JSONUtils.isArrayNode(json, "input")
					? JSONUtils.getAsJsonArray(json, "input")
					: JSONUtils.getAsJsonObject(json, "input");
			Ingredient input = Ingredient.fromJson(jsonelement);

			String fluidInputString = JSONUtils.getAsString(json, "fluid_input");
			ResourceLocation fluidInputLocation = new ResourceLocation(fluidInputString);
			FluidStack fluidInput = new FluidStack(Optional.ofNullable(ForgeRegistries.FLUIDS.getValue(fluidInputLocation))
					.orElseThrow(() -> new IllegalStateException("Item: " + fluidInputString + " does not exist.")), 1000);

			String outputString = JSONUtils.getAsString(json, "output");
			ResourceLocation outputLocation = new ResourceLocation(outputString);
			ItemStack output = new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(outputLocation))
					.orElseThrow(() -> new IllegalStateException("Item: " + outputString + " does not exist.")));

			int time = JSONUtils.getAsInt(json, "time", 200);
			int energy = JSONUtils.getAsInt(json, "energy", 4000);

			return new AutoclaveRecipe(recipeId, input, fluidInput, output, energy, time);
		}

		@Override
		public AutoclaveRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			Ingredient input = Ingredient.fromNetwork(buffer);
			FluidStack fluidInput = FluidStack.readFromPacket(buffer);
			ItemStack output = buffer.readItem();
			int energy = buffer.readInt();
			int time = buffer.readInt();

			return new AutoclaveRecipe(recipeId, input, fluidInput, output, energy, time);
		}


		@Override
		public void toNetwork(@Nonnull PacketBuffer buffer, AutoclaveRecipe recipe) {
			recipe.inputItems.get(0).toNetwork(buffer);
			recipe.inputFluids.get(0).writeToPacket(buffer);
			buffer.writeItem(recipe.outputItems.get(0));
			buffer.writeInt(recipe.resource);
			buffer.writeInt(recipe.time);
		}
	}
}
