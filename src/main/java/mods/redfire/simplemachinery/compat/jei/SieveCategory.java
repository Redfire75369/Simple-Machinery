package mods.redfire.simplemachinery.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mods.redfire.simplemachinery.registry.Blocks;
import mods.redfire.simplemachinery.registry.Names;
import mods.redfire.simplemachinery.tileentities.sieve.SieveRecipe;
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

public class SieveCategory extends MachineCategory<SieveRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(MODID, Names.SIEVE);
	private static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/screen/jei/sieve.png");
	public static SieveCategory INSTANCE;

	public SieveCategory(IGuiHelper helper) {
		super(helper, 131, 69, new ItemStack(Blocks.SIEVE_BLOCK.get()), GUI);
		INSTANCE = this;
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends SieveRecipe> getRecipeClass() {
		return SieveRecipe.class;
	}

	@Nonnull
	@Override
	public ITextComponent getTitleAsTextComponent() {
		return new TranslationTextComponent("screen.simplemachinery.jei.sieve");
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.get("screen.simplemachinery.jei.sieve");
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull SieveRecipe recipe, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		int index = 0;

		itemStacks.init(index++, true, 12, 17);
		initSlotRectangle(itemStacks, false, index, 66, 8, 3, 18, 2, 18);

		itemStacks.set(ingredients);
	}

	@Override
	public void draw(@Nonnull SieveRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
		if (progress == null) {
			progress = guiHelper.drawableBuilder(GUI, xSize + 1, 0, 24, 18)
					.buildAnimated(recipe.getTime() / 5, IDrawableAnimated.StartDirection.LEFT, false);
		}
		if (energy == null) {
			energy = guiHelper.drawableBuilder(GUI, 1, ySize, recipe.getEnergy() * 96 / 10000, 10)
					.buildAnimated(recipe.getTime() / 5, IDrawableAnimated.StartDirection.RIGHT, true);
		}

		progress.draw(matrixStack, 36 + 1, 17);
		energy.draw(matrixStack, 17 + 1, 51);
	}

	@Nonnull
	@Override
	public List<ITextComponent> getTooltipStrings(@Nonnull SieveRecipe recipe, double mouseX, double mouseY) {
		int x = (int) mouseX;
		int y = (int) mouseY;

		if (withinRectangle(x, y, 36, 17, 36 + 26, 17 + 10)) {
			return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.ticks", recipe.getTime()));
		} else if (withinRectangle(x, y, 16, 52, 16 + 98, 52 + 10)) {
			return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.energy", recipe.getEnergy()));
		}

		return Collections.emptyList();
	}
}
