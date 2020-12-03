package redfire.mods.simplemachinery.integration.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import redfire.mods.simplemachinery.ModBlocks;
import redfire.mods.simplemachinery.integration.jei.category.CategoryAutoclave;
import redfire.mods.simplemachinery.integration.jei.category.CategoryTurntable;
import redfire.mods.simplemachinery.integration.jei.recipemakers.RecipeMakerAutoclave;
import redfire.mods.simplemachinery.integration.jei.recipemakers.RecipeMakerTurntable;
import redfire.mods.simplemachinery.integration.jei.wrapper.WrapperAutoclave;
import redfire.mods.simplemachinery.integration.jei.wrapper.WrapperTurntable;
import redfire.mods.simplemachinery.tileentities.autoclave.RecipeAutoclave;
import redfire.mods.simplemachinery.tileentities.turntable.RecipeTurntable;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new CategoryAutoclave(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CategoryTurntable(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.handleRecipes(RecipeAutoclave.class, recipe -> new WrapperAutoclave(registry.getJeiHelpers(), recipe), CategoryAutoclave.uid);
		registry.handleRecipes(RecipeTurntable.class, recipe -> new WrapperTurntable(registry.getJeiHelpers(), recipe), CategoryTurntable.uid);

		registry.addRecipes(RecipeMakerAutoclave.getRecipes(registry.getJeiHelpers()), CategoryAutoclave.uid);
		registry.addRecipes(RecipeMakerTurntable.getRecipes(registry.getJeiHelpers()), CategoryTurntable.uid);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.autoclave), CategoryAutoclave.uid);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.turntable), CategoryTurntable.uid);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
	}
}
