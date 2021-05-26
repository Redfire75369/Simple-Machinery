package mods.redfire.simplemachinery.compat.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionRemoveRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.simplemachinery.turntable")
public class TurntableManager implements IRecipeManager {
    @ZenCodeType.Method
    public void addRecipe(String id, IIngredient input, IItemStack output, int energy, int time) {
        ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);

        TurntableRecipe recipe = new TurntableRecipe(recipeId, input.asVanillaIngredient(), output.getInternal(), energy, time);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
    }

    @Override
    public void removeRecipe(IItemStack output) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe(this, recipe -> {
            if(recipe instanceof TurntableRecipe) {
                return output.matches(new MCItemStackMutable(((TurntableRecipe) recipe).getOutputItems().get(0)));
            }
            return false;
        }, action -> "Removing \"" + action.getRecipeTypeName() + "\" recipes with output: " + output + "\""));
    }

    @Override
    public IRecipeType<TurntableRecipe> getRecipeType() {
        return TurntableRecipe.RECIPE_TYPE;
    }
}
