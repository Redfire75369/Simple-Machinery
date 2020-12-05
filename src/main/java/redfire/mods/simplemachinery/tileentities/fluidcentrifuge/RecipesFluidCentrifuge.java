package redfire.mods.simplemachinery.tileentities.fluidcentrifuge;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class RecipesFluidCentrifuge {
    public static final RecipesFluidCentrifuge fluid_centrifuge_base = new RecipesFluidCentrifuge();
    public final HashMap<String, RecipeFluidCentrifuge> recipes = new HashMap<>();

    public static RecipesFluidCentrifuge instance() {
        return fluid_centrifuge_base;
    }

    public RecipesFluidCentrifuge() {}

    public void addRecipe(String recipeName, List<ItemStack> outputs, FluidStack fluidInput, List<FluidStack> fluidOutputs, int ticks, int power) {
        if (recipes.containsKey(recipeName)) {
            FMLLog.log.warn("Ignored fluid centrifuge recipe with recipe name: {}", recipeName);
            return;
        }
        if ((!(getOutputs(recipeName).get(0).isEmpty()) && (getFluidOutputs(recipeName).get(0) != null))) {
            FMLLog.log.warn("Ignored fluid centrifuge recipe with conflicting input: {}", fluidInput.getFluid().getName());
            return;
        }

        recipes.put(recipeName, new RecipeFluidCentrifuge(outputs, fluidInput, fluidOutputs, ticks, power));
    }

    public List<ItemStack> getOutputs(String recipeName) {
        if (recipes.get(recipeName) != null) {
            return recipes.get(recipeName).outputs;
        }
        return Collections.singletonList(ItemStack.EMPTY);
    }
    public FluidStack getFluidInput(String recipeName) {
        if (recipes.get(recipeName) != null) {
            return recipes.get(recipeName).fluidInputs.get(0);
        }
        return null;
    }
    public List<FluidStack> getFluidOutputs(String recipeName) {
        if (recipes.get(recipeName) != null) {
            return recipes.get(recipeName).fluidOutputs;
        }
        return Collections.singletonList(null);
    }
    public int getPower(String recipeName) {
        if (recipes.get(recipeName) != null) {
            return recipes.get(recipeName).power;
        }
        return 0;
    }
    public int getTicks(String recipeName) {
        if (recipes.get(recipeName) != null) {
            return recipes.get(recipeName).ticks;
        }
        return 0;
    }
    public int getTotalEnergy(String recipeName) {
        if (recipes.get(recipeName) != null) {
            return recipes.get(recipeName).getTotalEnergy();
        }
        return 0;
    }
    public static String searchRecipes(FluidStack fluidInput) {
        HashMap<String, RecipeFluidCentrifuge> recipes = instance().recipes;
        AtomicReference<String> result = new AtomicReference<>("");
        recipes.forEach((name, recipe) -> {
            if (fluidInput != null && Objects.equals(recipe.fluidInputs.get(0).getFluid().getName(), fluidInput.getFluid().getName())) {
                result.set(name);
            }
        });
        return result.get();
    }
}
