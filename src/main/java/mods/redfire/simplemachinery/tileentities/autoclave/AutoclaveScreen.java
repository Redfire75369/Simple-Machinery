/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.autoclave;

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

public class AutoclaveScreen extends MachineScreen<AutoclaveContainer> {
	public AutoclaveScreen(AutoclaveContainer container, PlayerInventory inv, ITextComponent name) {
		super(175, 165, "textures/screen/container/autoclave.png", container, inv, name);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		drawGui(matrixStack);
		drawProgress(matrixStack, 76, 26, 24, 18);

		drawTank(matrixStack, 24, 66, 128, 8, 0, FluidDirection.RIGHT);
		drawTank(matrixStack, 9, 10, 8, 64, 1, FluidDirection.UP_GAS);
	}

	@Override
	protected void renderTooltip(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
		int x = getGuiLeft();
		int y = getGuiTop();

		ITextComponent tooltip = null;
		MachineFluidInventory tankInventory = menu.tile.getFluidInv();

		if (CoordinateChecker.withinRectangle(mouseX, mouseY, x + 24 - 2, y + 66 - 2, x + 24 + 128 + 1, y + 66 + 8 + 1)) {
			tooltip = new TranslationTextComponent("screen.simplemachinery.tooltip.fluid", tankInventory.getTank(0).getAmount(), tankInventory.getTank(0).getCapacity());
		} else if (CoordinateChecker.withinRectangle(mouseX, mouseY, x + 9 - 2, y + 10 - 2, x + 9 + 8 + 1, y + 10 + 64 + 1)) {
			tooltip = new TranslationTextComponent("screen.simplemachinery.tooltip.fluid", tankInventory.getTank(1).getAmount(), tankInventory.getTank(1).getCapacity());
		}

		if (tooltip != null) {
			GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(tooltip), mouseX, mouseY, width, height, -1, font);
		}

		super.renderTooltip(matrixStack, mouseX, mouseY);
	}
}
