/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mods.redfire.simplemachinery.Config;
import mods.redfire.simplemachinery.integration.jei.machine.EnergyMachineCategory;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.registry.Names;
import mods.redfire.simplemachinery.tileentities.fluidcentrifuge.FluidCentrifugeRecipe;
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

public class FluidCentrifugeCategory extends EnergyMachineCategory<FluidCentrifugeRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, Names.FLUID_CENTRIFUGE);
	private static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/screen/jei/fluid_centrifuge.png");
	public static FluidCentrifugeCategory INSTANCE;

	public FluidCentrifugeCategory(IGuiHelper helper) {
		super(helper, 132, 70, new ItemStack(Blocks.FLUID_CENTRIFUGE_BLOCK.get()), GUI);
		INSTANCE = this;
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends FluidCentrifugeRecipe> getRecipeClass() {
		return FluidCentrifugeRecipe.class;
	}

	@Nonnull
	@Override
	public ITextComponent getTitleAsTextComponent() {
		return new TranslationTextComponent("screen.simplemachinery.jei.fluid_centrifuge");
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.get("screen.simplemachinery.jei.fluid_centrifuge");
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull FluidCentrifugeRecipe recipe, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

		int index = initSlotRow(itemStacks, false, 0, 66, 8, 3, 18);

		fluidStacks.init(index++, true, 13, 10, 16, 32, Config.FLUID_CENTRIFUGE_INPUT_TANK_CAPACITY.get(), true, null);
		initTankRow(fluidStacks, false, index, 67, 27, 16, 16, 3, 18, Config.FLUID_CENTRIFUGE_OUTPUT_TANKS_CAPACITY.get(), true);

		itemStacks.set(ingredients);
		fluidStacks.set(ingredients);
	}

	@Override
	public void draw(@Nonnull FluidCentrifugeRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);

		progress.draw(matrixStack, 36, 18);
		energy.draw(matrixStack, 18, 53);
	}

	@Nonnull
	@Override
	public List<ITextComponent> getTooltipStrings(@Nonnull FluidCentrifugeRecipe recipe, double mouseX, double mouseY) {
		int x = (int) mouseX;
		int y = (int) mouseY;

		if (withinRectangle(x, y, 34, 16, 37 + 24, 18 + 18)) {
			return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.ticks", recipe.getTime()));
		} else if (withinRectangle(x, y, 16, 51, 17 + 98, 52 + 10)) {
			return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.energy", recipe.getEnergy()));
		}

		return Collections.emptyList();
	}
}
