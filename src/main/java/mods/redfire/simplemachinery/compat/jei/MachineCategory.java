package mods.redfire.simplemachinery.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mods.redfire.simplemachinery.tileentities.machine.MachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;


public abstract class MachineCategory<T extends MachineRecipe> implements IRecipeCategory<T> {
	protected final int xSize;
	protected final int ySize;
	protected final IGuiHelper guiHelper;

	protected final IDrawable background;
	protected final IDrawable icon;

	protected IDrawableAnimated progress;
	protected IDrawableAnimated energy;

	public MachineCategory(IGuiHelper helper, int xSize, int ySize, ItemStack icon, ResourceLocation gui) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.guiHelper = helper;

		this.background = guiHelper.createDrawable(gui, 0, 0, xSize, ySize);
		this.icon = guiHelper.createDrawableIngredient(icon);
	}

	@Nonnull
	@Override
	public abstract ResourceLocation getUid();

	@Nonnull
	@Override
	public abstract Class<? extends T> getRecipeClass();

	@Nonnull
	@Override
	public abstract ITextComponent getTitleAsTextComponent();

	@Nonnull
	@Override
	public abstract String getTitle();

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(T recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputItems());
	}

	public int initSlotRow(IGuiItemStackGroup itemStacks, boolean input, int index, int x, int y, int amount, int dx) {
		for (int i = 0; i < amount; i++) {
			itemStacks.init(index++, input, x, y);
			x += dx;
		}
		return index;
	}

	public int initSlotRectangle(IGuiItemStackGroup itemStacks, boolean input, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
		for (int j = 0; j < verAmount; j++) {
			index = initSlotRow(itemStacks, input, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}
}
