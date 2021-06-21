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
import mods.redfire.simplemachinery.integration.jei.machine.FluidMachineCategory;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.registry.Names;
import mods.redfire.simplemachinery.tileentities.autoclave.AutoclaveRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static mods.redfire.simplemachinery.SimpleMachinery.MODID;
import static mods.redfire.simplemachinery.util.CoordinateChecker.withinRectangle;

public class AutoclaveCategory extends FluidMachineCategory<AutoclaveRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, Names.AUTOCLAVE);
	private static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/screen/jei/autoclave.png");
	public static AutoclaveCategory INSTANCE;

	public AutoclaveCategory(IGuiHelper helper) {
		super(helper, 113, 52, new ItemStack(Blocks.TURNTABLE_BLOCK.get()), GUI, ForgeRegistries.FLUIDS.getValues().stream().filter(f -> f.getRegistryName().getPath().equals("steam")).collect(Collectors.toList()));
		INSTANCE = this;
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends AutoclaveRecipe> getRecipeClass() {
		return AutoclaveRecipe.class;
	}

	@Nonnull
	@Override
	public ITextComponent getTitleAsTextComponent() {
		return new TranslationTextComponent("screen.simplemachinery.jei.autoclave");
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.get("screen.simplemachinery.jei.autoclave");
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull AutoclaveRecipe recipe, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

		itemStacks.init(0, true, 30, 8);
		itemStacks.init(1, false, 83, 8);

		// TODO: Make Steam Tank fill up from left to right instead of bottom to top
		fluidStacks.init(2, true, 33, 35, 64, 8, Config.AUTOCLAVE_STEAM_CAPACITY.get(), false, null);
		fluidStacks.init(3, true, 13, 10, 8, 32, Config.AUTOCLAVE_TANK_CAPACITY.get(), true, null);

		itemStacks.set(ingredients);
		fluidStacks.set(ingredients);
	}

	@Override
	public void draw(@Nonnull AutoclaveRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
		super.draw(recipe, matrixStack, mouseX, mouseY);

		progress.draw(matrixStack, 54, 9);
	}

	@Nonnull
	@Override
	public List<ITextComponent> getTooltipStrings(@Nonnull AutoclaveRecipe recipe, double mouseX, double mouseY) {
		int x = (int) mouseX;
		int y = (int) mouseY;

		if (withinRectangle(x, y, 52, 7, 53 + 26, 8 + 18)) {
			return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.ticks", recipe.getTime()));
		}

		return Collections.emptyList();
	}
}
