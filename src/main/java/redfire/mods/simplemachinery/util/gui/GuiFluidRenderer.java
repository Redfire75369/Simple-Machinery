/** Credits to tomdodd4598 (Developer of NuclearCraft) and CrazyPants (Developer of EnderIO)
 * for letting me use the following code. **/

package redfire.mods.simplemachinery.util.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiFluidRenderer {
    private static final Minecraft MC = Minecraft.getMinecraft();

    private static @Nonnull TextureAtlasSprite getStillTexture(@Nonnull FluidStack fluidstack) {

        final Fluid fluid = fluidstack.getFluid();
        if (fluid == null) {
            return getMissingSprite();
        }
        return getStillTexture(fluid);
    }

    private static @Nonnull TextureAtlasSprite getStillTexture(@Nonnull Fluid fluid) {
        ResourceLocation iconKey = fluid.getStill();
        if (iconKey == null) {
            return getMissingSprite();
        }
        final TextureAtlasSprite textureEntry = MC.getTextureMapBlocks().getTextureExtry(iconKey.toString());
        return textureEntry != null ? textureEntry : getMissingSprite();
    }

    private static @Nonnull TextureAtlasSprite getMissingSprite() {
        return MC.getTextureMapBlocks().getMissingSprite();
    }

    public static void renderGuiTank(FluidTank tank, double x, double y, double zLevel, double width, double height) {
        renderGuiTank(tank.getFluid(), tank.getCapacity(), tank.getFluidAmount(), x, y, zLevel, width, height, 255);
    }

    /** Alpha is a byte! */
    public static void renderGuiTank(FluidTank tank, double x, double y, double zLevel, double width, double height, int alpha) {
        if (tank == null || tank.getFluid() == null) {
            return;
        }
        renderGuiTank(tank.getFluid(), tank.getCapacity(), tank.getFluidAmount(), x, y, zLevel, width, height, alpha);
    }

    /** Alpha is a byte! */
    public static void renderGuiTank(@Nullable FluidStack fluid, int capacity, int amount, double x, double y, double zLevel, double width, double height, int alpha) {
        if (fluid == null || fluid.getFluid() == null || fluid.amount <= 0) {
            return;
        }
        TextureAtlasSprite icon = getStillTexture(fluid);

        int renderAmount = (int) height;
        int posY = (int) y;

        MC.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        int color = fluid.getFluid().getColor(fluid);
        GL11.glColor4ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF), (byte) alpha);

        GlStateManager.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                double minU = icon.getMinU();
                double maxU = icon.getMaxU();
                double minV = icon.getMinV();
                double maxV = icon.getMaxV();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                final double v = minV + (maxV - minV) * drawHeight / 16F;
                buffer.pos(drawX, drawY + drawHeight, 0).tex(minU, v).endVertex();
                double u = minU + (maxU - minU) * drawWidth / 16F;
                buffer.pos(drawX + drawWidth, drawY + drawHeight, 0).tex(u, v).endVertex();
                buffer.pos(drawX + drawWidth, drawY, 0).tex(u, minV).endVertex();
                buffer.pos(drawX, drawY, 0).tex(minU, minV).endVertex();
                tessellator.draw();
            }
        }
        GlStateManager.disableBlend();
    }
}