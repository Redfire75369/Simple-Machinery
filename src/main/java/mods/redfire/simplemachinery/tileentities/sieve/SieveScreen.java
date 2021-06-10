package mods.redfire.simplemachinery.tileentities.sieve;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.redfire.simplemachinery.tileentities.machine.MachineScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class SieveScreen extends MachineScreen<SieveContainer> {
	public SieveScreen(SieveContainer container, PlayerInventory inv, ITextComponent name) {
		super(175, 165, "textures/screen/container/sieve.png", container, inv, name);
	}

	@Override
	protected void renderBg(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		int x = getGuiLeft();
		int y = getGuiTop();

		drawGui(matrixStack);
		drawProgress(matrixStack, 59, 26, 24, 18);
		drawEnergy(matrixStack, 24, 66, 128, 8);
	}
}
