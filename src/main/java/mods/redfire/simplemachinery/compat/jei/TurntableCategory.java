package mods.redfire.simplemachinery.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mods.redfire.simplemachinery.registry.Blocks;
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
import static mods.redfire.simplemachinery.util.CoordinateCheck.withinRectangle;

public class TurntableCategory implements IRecipeCategory<TurntableRecipe> {
    private static final ResourceLocation GUI = new ResourceLocation(MODID, "textures/screen/jei/turntable.png");
    public static final ResourceLocation UID = new ResourceLocation(MODID, "chopping");

    public static TurntableCategory INSTANCE;

    private final IGuiHelper guiHelper;
    private final IDrawable icon;
    private final IDrawable background;

    private final int xSize = 96;
    private final int ySize = 52;

    public TurntableCategory(IGuiHelper helper) {
        INSTANCE = this;
        guiHelper = helper;

        icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.TURNTABLE.get()));
        background = guiHelper.createDrawable(GUI, 0, 0, xSize, ySize);
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
    public void setIngredients(TurntableRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutputItems().get(0));
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
        IDrawableAnimated progress = guiHelper.drawableBuilder(GUI, xSize + 1, 0, 24, 18)
                .buildAnimated(recipe.getTime() / 10, IDrawableAnimated.StartDirection.LEFT, false);
        progress.draw(matrixStack, 36 + 1, 8);

        int energyWidth = recipe.getEnergy() * 64 / 10000;
        guiHelper.drawableBuilder(GUI, 1, ySize, energyWidth, 10)
                .buildAnimated(recipe.getTime() / 10, IDrawableAnimated.StartDirection.LEFT, true)
                .draw(matrixStack, 15 + 1, 34);
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltipStrings(@Nonnull TurntableRecipe recipe, double mouseX, double mouseY) {
        int x = (int) mouseX;
        int y = (int) mouseY;

        if (withinRectangle(x, y, 36, 8, 36 + 26, 8 + 10)) {
            return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.ticks", recipe.getTime()));
        } else if (withinRectangle(x, y, 15, 34, 15 + 66, 34 + 10)) {
            return Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.jei.energy", recipe.getEnergy()));
        }

        return Collections.emptyList();
    }
}
