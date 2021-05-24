package mods.redfire.simplemachinery.tileentities.turntable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.redfire.simplemachinery.SimpleMachinery;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TurntableScreen extends ContainerScreen<TurntableContainer> {
    private final ResourceLocation GUI = new ResourceLocation(SimpleMachinery.MODID, "textures/screen/container/turntable.png");

    private final int xSize = 175;
    private final int ySize = 165;

    public TurntableScreen(TurntableContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {}

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(GUI);

        int x = getGuiLeft();
        int y = getGuiTop();

        this.blit(matrixStack, x, y, 0, 0, xSize, ySize);

        this.blit(matrixStack, x + 76, y + 34, xSize + 1, 0, getTimeScaled(24), 18);
        this.blit(matrixStack, x + 24, y + 66, 1, ySize + 1, getEnergyStorageScaled(128), 10);
    }

    private int getTimeScaled(int pixels) {
        int i = (menu.getGuiData().get(3) << 16) | (menu.getGuiData().get(2) & 0xffff);
        int j = (menu.getGuiData().get(5) << 16) | (menu.getGuiData().get(4) & 0xffff);
        return i != 0 && j != 0 ? i * pixels / j : 0;
    }

    private int getEnergyStorageScaled(int pixels) {
        int i = (menu.getGuiData().get(1) << 16) | (menu.getGuiData().get(0) & 0xffff);
        return i * pixels / 10000;
    }
}
