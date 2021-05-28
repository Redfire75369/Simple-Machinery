package mods.redfire.simplemachinery.tileentities.sieve;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mods.redfire.simplemachinery.SimpleMachinery;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.tileentities.machine.MachineRecipe;
import mods.redfire.simplemachinery.util.inventory.MachineInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.*;

public class SieveRecipe extends MachineRecipe {
	protected static final String RECIPE_NAME = "sieve";

	@ObjectHolder("simplemachinery:" + RECIPE_NAME)
	public static IRecipeSerializer<?> SERIALIZER = null;
	public static IRecipeType<SieveRecipe> RECIPE_TYPE = IRecipeType.register(new ResourceLocation(SimpleMachinery.MODID, RECIPE_NAME).toString());

	public SieveRecipe(ResourceLocation id, Ingredient input, List<ItemStack> outputs, List<Float> outputChances, int energy, int time) {
		super(id, energy, time, Collections.singletonList(input), outputs, outputChances);
	}

	public static Optional<SieveRecipe> getRecipe(World world, MachineInventory ctx) {
		return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, ctx.getInputInventory(), world);
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
			JsonElement inputElement = JSONUtils.isArrayNode(json, "input")
					? JSONUtils.getAsJsonArray(json, "input")
					: JSONUtils.getAsJsonObject(json, "input");
			Ingredient input = Ingredient.fromJson(inputElement);

			List<ItemStack> outputs = new ArrayList<>();
			JsonArray outputsArray = JSONUtils.getAsJsonArray(json, "outputs");
			for (JsonElement outputElement : outputsArray) {
				String outputString = outputElement.getAsString();
				ResourceLocation outputLocation = new ResourceLocation(outputString);
				outputs.add(
						new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(outputLocation))
								.orElseThrow(() -> new IllegalStateException("Item: " + outputString + " does not exist.")))
				);
			}

			boolean outputChancesArrayExists = JSONUtils.isArrayNode(json, "output_chances");
			List<Float> outputChances = outputChancesArrayExists ? new ArrayList<>() : null;
			if (outputChancesArrayExists) {
				JsonArray outputChancesArray = JSONUtils.getAsJsonArray(json, "output_chances");
				for (JsonElement outputChanceElement : outputChancesArray) {
					Float outputChance = outputChanceElement.getAsFloat();
					outputChances.add(outputChance);
				}
			}

			int time = JSONUtils.getAsInt(json, "time", 200);
			int energy = JSONUtils.getAsInt(json, "energy", 4000);

			return new SieveRecipe(recipeId, input, outputs, outputChances, energy, time);
		}

		@Override
		public SieveRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			Ingredient input = Ingredient.fromNetwork(buffer);
			int outputCount = buffer.readInt();
			List<ItemStack> outputs = new ArrayList<>();
			List<Float> outputChances = new ArrayList<>();
			for (int i = 0; i < outputCount; i++) {
				outputs.add(buffer.readItem());
				outputChances.add(buffer.readFloat());
			}
			int energy = buffer.readInt();
			int time = buffer.readInt();

			return new SieveRecipe(recipeId, input, outputs, outputChances, energy, time);
		}


		@Override
		public void toNetwork(@Nonnull PacketBuffer buffer, SieveRecipe recipe) {
			recipe.inputItems.get(0).toNetwork(buffer);
			buffer.writeInt(recipe.outputItems.size());
			for (int i = 0; i < recipe.outputItems.size(); i++) {
				buffer.writeItem(recipe.outputItems.get(i));
				buffer.writeFloat(recipe.outputItemChances.get(i));
			}
			buffer.writeInt(recipe.energy);
			buffer.writeInt(recipe.time);
		}
	}
}
