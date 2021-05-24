package mods.redfire.simplemachinery.tileentities.turntable;

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
import javax.annotation.Nullable;
import java.util.*;

public class TurntableRecipe extends MachineRecipe {
    protected static final String RECIPE_NAME = "turntable";

    @ObjectHolder("simplemachinery:" + RECIPE_NAME)
    public static IRecipeSerializer<?> SERIALIZER = null;
    public static IRecipeType<TurntableRecipe> RECIPE_TYPE = IRecipeType.register(new ResourceLocation(SimpleMachinery.MODID, RECIPE_NAME).toString());

    public TurntableRecipe(ResourceLocation id, Ingredient input, ItemStack output, int energy, int time) {
        super(id, energy, time, Collections.singletonList(input), Collections.singletonList(output));
    }

    @Nonnull
    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.TURNTABLE.get());
    }

    public static int getTimeFor(World world, MachineInventory ctx) {
        return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, ctx, world).map(TurntableRecipe::getTime).orElse(0);
    }

    public static int getEnergyFor(World world, MachineInventory ctx) {
        return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, ctx, world).map(TurntableRecipe::getEnergy).orElse(1);
    }

    public static int getEnergyRateFor(World world, MachineInventory ctx) {
        return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, ctx, world).map(TurntableRecipe::getEnergyRate).orElse(1);
    }

    public static List<ItemStack> getOutputFor(World world, MachineInventory ctx) {
        return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, ctx, world)
                .map(TurntableRecipe::getOutputItems)
                .orElse(Collections.singletonList(ItemStack.EMPTY));
    }

    public static Optional<TurntableRecipe> getRecipe(World world, MachineInventory ctx) {
        return world.getRecipeManager().getRecipeFor(RECIPE_TYPE, ctx, world);
    }

    public static Collection<TurntableRecipe> getAllRecipes(World world) {
        return world.getRecipeManager().getAllRecipesFor(RECIPE_TYPE);
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
            implements IRecipeSerializer<TurntableRecipe> {
        @Nullable
        @Override
        public TurntableRecipe fromJson(@Nonnull ResourceLocation recipeId, JsonObject json) {
            JsonElement jsonelement = JSONUtils.isArrayNode(json, "input")
                    ? JSONUtils.getAsJsonArray(json, "input")
                    : JSONUtils.getAsJsonObject(json, "input");
            Ingredient input = Ingredient.fromJson(jsonelement);

            String outputString = JSONUtils.getAsString(json, "output");
            ResourceLocation outputLocation = new ResourceLocation(outputString);
            ItemStack output = new ItemStack(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(outputLocation))
                    .orElseThrow(() -> new IllegalStateException("Item: " + outputString + " does not exist")));

            int time = JSONUtils.getAsInt(json, "time", 200);
            int energy = JSONUtils.getAsInt(json, "energy", 4000);

            return new TurntableRecipe(recipeId, input, output, energy, time);
        }

        @Override
        public TurntableRecipe fromNetwork(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readUtf(32767);
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            int energy = buffer.readInt();
            int time = buffer.readInt();

            return new TurntableRecipe(recipeId, input, output, energy, time);
        }


        @Override
        public void toNetwork(@Nonnull PacketBuffer buffer, TurntableRecipe recipe) {
            recipe.inputItems.get(0).toNetwork(buffer);
            buffer.writeItem(recipe.outputItems.get(0));
            buffer.writeInt(recipe.energy);
            buffer.writeInt(recipe.time);
        }
    }
}
