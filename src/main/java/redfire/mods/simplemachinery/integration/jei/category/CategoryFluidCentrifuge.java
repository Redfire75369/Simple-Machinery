package redfire.mods.simplemachinery.integration.jei.category;

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
import redfire.mods.simplemachinery.integration.jei.wrapper.WrapperFluidCentrifuge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CategoryFluidCentrifuge implements IRecipeCategory<WrapperFluidCentrifuge> {
    private static final ResourceLocation guiLocation = new ResourceLocation(SimpleMachinery.modid, "textures/gui/jei/fluid_centrifuge.png");
    public static final String uid = SimpleMachinery.modid + "_fluid_centrifuge";
    private static final String title = "Fluid Centrifuge";

    private static final int fluidInputSlot = 0;
    private static final int outputSlot1 = 1;
    private static final int outputSlot2 = 2;
    private static final int fluidOutputSlot1 = 3;
    private static final int fluidOutputSlot2 = 4;

    @Nonnull
    private final IDrawable background;

    public CategoryFluidCentrifuge(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(guiLocation, 0, 0, 96, 40, 0, 4, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WrapperFluidCentrifuge recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

        guiFluidStacks.init(fluidInputSlot, true, 4, 12, 16, 16, 16000, false, null);
        guiItemStacks.init(outputSlot1, false, 51, 2);
        guiItemStacks.init(outputSlot2, false, 51, 20);
        guiFluidStacks.init(fluidOutputSlot1, false, 76, 3, 16, 16, 8000, false, null);
        guiFluidStacks.init(fluidOutputSlot2, false, 76, 21, 16, 16, 8000, false, null);

        guiItemStacks.set(ingredients);
        guiFluidStacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {}

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
