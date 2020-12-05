package redfire.mods.simplemachinery.integration.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import redfire.mods.simplemachinery.SimpleMachinery;
import redfire.mods.simplemachinery.integration.jei.wrapper.WrapperTurntable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CategoryTurntable implements IRecipeCategory<WrapperTurntable> {
	private static final ResourceLocation guiLocation = new ResourceLocation(SimpleMachinery.modid, "textures/gui/jei/turntable.png");
	public static final String uid = SimpleMachinery.modid + "_turntable";
	private static final String title = "Turntable";

	private static final int inputSlot = 0;
	private static final int outputSlot = 1;

	@Nonnull
	private final IDrawable background;

	public CategoryTurntable(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(guiLocation, 0, 0, 96, 36, 0, 8, 0, 0);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, WrapperTurntable recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(inputSlot, true, 12, 9);
		guiItemStacks.init(outputSlot, false, 66, 9);

		guiItemStacks.set(ingredients);
	}

	@Override
	public void drawExtras(Minecraft minecraft) { }

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
}
