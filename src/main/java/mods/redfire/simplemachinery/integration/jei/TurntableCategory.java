/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mods.redfire.simplemachinery.integration.jei.machine.EnergyMachineCategory;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.registry.Names;
import mods.redfire.simplemachinery.tileentities.turntable.TurntableRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static mods.redfire.simplemachinery.SimpleMachinery.MODID;
import static mods.redfire.simplemachinery.util.CoordinateChecker.withinRectangle;

public class TurntableCategory extends EnergyMachineCategory<TurntableRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, Names.TURNTABLE);
	private static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/screen/jei/turntable.png");
	public static TurntableCategory INSTANCE;

	public TurntableCategory(IGuiHelper helper) {
		super(helper, 96, 52, new ItemStack(Blocks.TURNTABLE_BLOCK.get()), GUI);
		INSTANCE = this;
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends TurntableRecipe> getRecipeClass() {
		return TurntableRecipe.class;
	}

	@Nonnull
	@Override
	public ITextComponent getTitleAsTextComponent() {
		return new TranslationTextComponent("screen.simplemachinery.jei.turntable");
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.get("screen.simplemachinery.jei.turntable");
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull TurntableRecipe recipe, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 12, 8);
		itemStacks.init(1, false, 66, 8);

		itemStacks.set(ingredients);
	}

	@Override
	public void draw(@Nonnull TurntableRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);

		progress.draw(matrixStack, 37, 9);
		energy.draw(matrixStack, 16, 35);
	}

	@Nonnull
	@Override
	public List<ITextComponent> getTooltipStrings(@Nonnull TurntableRecipe recipe, double mouseX, double mouseY) {
		int x = (int) mouseX;
		int y = (int) mouseY;

		if (withinRectangle(x, y, 35, 7, 36 + 24, 8 + 18)) {
			return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.ticks", recipe.getTime()));
		} else if (withinRectangle(x, y, 14, 33, 15 + 66, 34 + 10)) {
			return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.energy", recipe.getEnergy()));
		}

		return Collections.emptyList();
	}
}
