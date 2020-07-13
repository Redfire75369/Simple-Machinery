package redfire.mods.simplemachinery.integration.jei.categories;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import redfire.mods.simplemachinery.SimpleMachinery;
import redfire.mods.simplemachinery.integration.jei.wrappers.RecipeAutoclave;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CategoryAutoclave implements IRecipeCategory<RecipeAutoclave> {
	private static final ResourceLocation guiLocation = new ResourceLocation(SimpleMachinery.modid, "textures/gui/jei/jei.png");
	public static final String uid = SimpleMachinery.modid + "_autoclave";
	private static String title = "Autoclave";

	private static final int inputSlot = 0;
	private static final int outputSlot = 1;
	private static final int fluidInputSlot = 2;

	@Nonnull
	private IDrawable background;

	public CategoryAutoclave(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(guiLocation, 0, 0, 96, 36, 0, 8, 0, 0);
	}

	@Nonnull
	@Override
	public String getUid() {
		return uid;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getModName() {
		return SimpleMachinery.modname;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nullable
	@Override
	public IDrawable getIcon() {
		return null;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeAutoclave recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

		guiItemStacks.init(inputSlot, true, 3, 9);
		guiItemStacks.init(outputSlot, false, 75, 9);
		guiFluidStacks.init(fluidInputSlot, true, 22, 10);

		guiItemStacks.set(ingredients);
		guiFluidStacks.set(ingredients);
	}
}
