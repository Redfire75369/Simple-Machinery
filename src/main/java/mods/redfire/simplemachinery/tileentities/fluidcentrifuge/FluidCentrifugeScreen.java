/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.fluidcentrifuge;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.redfire.simplemachinery.tileentities.machine.MachineScreen;
import mods.redfire.simplemachinery.util.CoordinateChecker;
import mods.redfire.simplemachinery.util.inventory.fluid.MachineFluidInventory;
import mods.redfire.simplemachinery.util.render.FluidDirection;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.Collections;

public class FluidCentrifugeScreen extends MachineScreen<FluidCentrifugeContainer> {
	public FluidCentrifugeScreen(FluidCentrifugeContainer container, PlayerInventory inv, ITextComponent name) {
		super(175, 165, "textures/screen/container/fluid_centrifuge.png", container, inv, name);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		drawGui(matrixStack);
		drawProgress(matrixStack, 59, 26, 24, 18);
		drawEnergy(matrixStack, 24, 66, 128, 8);

		drawTank(matrixStack, 35, 18, 16, 32, 0, FluidDirection.UP_GAS);
		drawTank(matrixStack, 89, 35, 16, 16, 1, FluidDirection.UP_GAS);
		drawTank(matrixStack, 89 + 18, 35, 16, 16, 2, FluidDirection.UP_GAS);
		drawTank(matrixStack, 89 + 18 * 2, 35, 16, 16, 3, FluidDirection.UP_GAS);
	}

	@Override
	protected void renderTooltip(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
		int x = getGuiLeft();
		int y = getGuiTop();

		ITextComponent tooltip = null;
		MachineFluidInventory tankInventory = menu.tile.getFluidInv();

		if (CoordinateChecker.withinRectangle(mouseX, mouseY, x + 35 - 2, y + 18 - 2, x + 35 + 16 + 1, y + 18 + 32 + 1)) {
			tooltip = new TranslationTextComponent("screen.simplemachinery.tooltip.fluid", tankInventory.getTank(0).getAmount(), tankInventory.getTank(0).getCapacity());
		} else if (CoordinateChecker.withinRectangle(mouseX, mouseY, x + 89 - 2, y + 35 - 2, x + 89 + 16 + 1, y + 35 + 16 + 1)) {
			tooltip = new TranslationTextComponent("screen.simplemachinery.tooltip.fluid", tankInventory.getTank(1).getAmount(), tankInventory.getTank(1).getCapacity());
		} else if (CoordinateChecker.withinRectangle(mouseX, mouseY, x + 89 + 18 - 2, y + 35 - 2, x + 89 + 18 + 16 + 1, y + 35 + 16 + 1)) {
			tooltip = new TranslationTextComponent("screen.simplemachinery.tooltip.fluid", tankInventory.getTank(2).getAmount(), tankInventory.getTank(2).getCapacity());
		} else if (CoordinateChecker.withinRectangle(mouseX, mouseY, x + 89 + 18 * 2 - 2, y + 35 - 2, x + 89 + 18 * 2 + 16 + 1, y + 35 + 16 + 1)) {
			tooltip = new TranslationTextComponent("screen.simplemachinery.tooltip.fluid", tankInventory.getTank(3).getAmount(), tankInventory.getTank(3).getCapacity());
		}

		if (tooltip != null) {
			GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(tooltip), mouseX, mouseY, width, height, -1, font);
		}

		super.renderTooltip(matrixStack, mouseX, mouseY);
	}
}
