/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package mods.redfire.simplemachinery.tileentities.turntable;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.redfire.simplemachinery.tileentities.machine.MachineScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class TurntableScreen extends MachineScreen<TurntableContainer> {
	public TurntableScreen(TurntableContainer container, PlayerInventory inv, ITextComponent name) {
		super(175, 165, "textures/screen/container/turntable.png", container, inv, name);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		drawGui(matrixStack);
		drawProgress(matrixStack, 76, 26, 24, 18);
		drawEnergy(matrixStack, 24, 66, 128, 8);
	}
}
