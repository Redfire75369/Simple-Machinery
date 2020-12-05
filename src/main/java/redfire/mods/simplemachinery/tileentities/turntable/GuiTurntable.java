package redfire.mods.simplemachinery.tileentities.turntable;

import redfire.mods.simplemachinery.tileentities.generic.GuiMachine;

public class GuiTurntable extends GuiMachine<TileTurntable, ContainerTurntable> {
	public GuiTurntable(TileTurntable tileTurntable, ContainerTurntable container) {
		super(tileTurntable, container,"textures/gui/container/turntable.png");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		drawTexturedModalRect(guiLeft + 76, guiTop + 34, 176, 0, getProgressScaled(24) + 1, 16);
		drawTexturedModalRect(guiLeft + 23, guiTop + 66, 0, 166, getEnergyStorageScaled(128), 10);
	}

	private int getProgressScaled(int pixels) {
		int i = tileEntity.getProgress();
		int j = RecipesTurntable.instance().getTicks(tileEntity.getCurrentRecipe());;
		return j != 0 && (j - i) != 0 ? (j - i) * pixels / j : 0;
	}

	private int getEnergyStorageScaled(int pixels) {
		int i = tileEntity.getEnergyStorage().getEnergyStored();
		int j = tileEntity.getEnergyStorage().getMaxEnergyStored();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
