package mods.redfire.simplemachinery.tileentities.machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import mods.redfire.simplemachinery.util.IntArrayWrapper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public abstract class MachineScreen<T extends MachineContainer<?>> extends ContainerScreen<T> {
	protected final int xSize;
	protected final int ySize;

	public MachineScreen(int xSize, int ySize, T container, PlayerInventory inv, ITextComponent name) {
		super(container, inv, name);
		this.xSize = xSize;
		this.ySize = ySize;
	}

	@Override
	public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {}

	protected int getTimeScaled(int pixels) {
		IntArrayWrapper data = menu.getData();

		int i = data.getInternal(0);
		int j = data.getInternal(1);
		return j != 0 && i != j ? (j - i) * pixels / j : 0;
	}

	protected int getEnergyStorageScaled(int pixels) {
		int i = menu.getEnergy();
		return i * pixels / 10000;
	}
}
