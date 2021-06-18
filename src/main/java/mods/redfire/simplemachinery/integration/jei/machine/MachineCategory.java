/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.jei.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
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
	protected final ResourceLocation gui;
	protected final IGuiHelper guiHelper;

	protected final IDrawable background;
	protected final IDrawable icon;

	protected IDrawableAnimated progress;
	protected IDrawableAnimated energy;

	public MachineCategory(IGuiHelper helper, int xSize, int ySize, ItemStack icon, ResourceLocation gui) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.gui = gui;
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
	public abstract void setIngredients(@Nonnull T recipe, @Nonnull IIngredients ingredients);

	@Override
	public void draw(@Nonnull T recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
		if (progress == null) {
			progress = guiHelper.drawableBuilder(gui, xSize + 1, 1, 24, 16)
					.buildAnimated(getRecipeTime(recipe), IDrawableAnimated.StartDirection.LEFT, false);
		}
	}

	protected int initSlotRow(IGuiItemStackGroup itemStacks, boolean input, int index, int x, int y, int amount, int dx) {
		for (int i = 0; i < amount; i++) {
			itemStacks.init(index++, input, x, y);
			x += dx;
		}
		return index;
	}

	protected int initSlotRectangle(IGuiItemStackGroup itemStacks, boolean input, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
		for (int j = 0; j < verAmount; j++) {
			index = initSlotRow(itemStacks, input, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	protected int initTankRow(IGuiFluidStackGroup fluidStacks, boolean input, int index, int x, int y, int width, int height, int amount, int dx, int capacity) {
		for (int i = 0; i < amount; i++) {
			fluidStacks.init(index++, input, x, y, width, height, capacity, true, null);
			x += dx;
		}
		return index;
	}

	protected int initTankRectangle(IGuiFluidStackGroup fluidStacks, boolean input, int index, int x, int y, int width, int height, int horAmount, int verAmount, int dx, int dy, int capacity) {
		for (int j = 0; j < verAmount; j++) {
			index = initTankRow(fluidStacks, input, index, x, y, width, height, horAmount, dx, capacity);
			y += dy;
		}
		return index;
	}

	protected int getRecipeTime(@Nonnull T recipe) {
		return Math.min(Math.max(recipe.getTime() / 5, 40), 720);
	}
}
