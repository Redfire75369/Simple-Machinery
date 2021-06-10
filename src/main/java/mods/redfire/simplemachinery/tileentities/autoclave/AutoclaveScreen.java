package mods.redfire.simplemachinery.tileentities.autoclave;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.redfire.simplemachinery.tileentities.machine.MachineScreen;
import mods.redfire.simplemachinery.util.CoordinateChecker;
import mods.redfire.simplemachinery.util.render.FluidDirection;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.Collections;

public class AutoclaveScreen extends MachineScreen<AutoclaveContainer> {

	public AutoclaveScreen(AutoclaveContainer container, PlayerInventory inv, ITextComponent name) {
		super(175, 165, "textures/screen/container/autoclave.png", container, inv, name);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		drawGui(matrixStack);
		drawProgress(matrixStack, 76, 25, 24, 18);

		drawTank(matrixStack, 24, 66, 128, 8, 0, FluidDirection.RIGHT);
		drawTank(matrixStack, 9, 10, 8, 64, 1, FluidDirection.UP_GAS);
	}

	@Override
	protected void renderTooltip(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
		int x = getGuiLeft();
		int y = getGuiTop();

		if (CoordinateChecker.withinRectangle(mouseX, mouseY, x + 7, y + 9, x + 18, y + 74)) {
			GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(new TranslationTextComponent("screen.simplemachinery.tooltip.fluid", menu.getFluidStored(0), 8000)), mouseX, mouseY, width, height, -1, font);
		}
		super.renderTooltip(matrixStack, mouseX, mouseY);
	}

	public int getFluidScaled(int tank, int pixels) {
		int i = menu.getFluidStored(tank);
		return i * pixels / 8000;
	}
}
