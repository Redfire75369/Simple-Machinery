package redfire.mods.simplemachinery.tileentities.autoclave;

import redfire.mods.simplemachinery.tileentities.generic.GuiMachine;
import redfire.mods.simplemachinery.util.gui.GuiFluidRenderer;

public class GuiAutoclave extends GuiMachine<TileAutoclave, ContainerAutoclave> {
	public GuiAutoclave(TileAutoclave tileAutoclave, ContainerAutoclave container) {
		super(tileAutoclave, container, "textures/gui/container/autoclave.png");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		drawTexturedModalRect(guiLeft + 85, guiTop + 34, 176, 0, getProgressScaled(24) + 1, 16);

		int fluidHeight = getFluidTankScaled(1, 64);
		GuiFluidRenderer.renderGuiTank(tileEntity.getTanks().get(1), guiLeft + 14, guiTop + 10 + 64 - fluidHeight, 0, 16, fluidHeight);
		fluidHeight = getFluidTankScaled(0, 64);
		GuiFluidRenderer.renderGuiTank(tileEntity.getTanks().get(0), guiLeft + 146, guiTop + 10 + 64 - fluidHeight, 0, 16, fluidHeight);
	}

	private int getProgressScaled(int pixels) {
		int i = tileEntity.getProgress();
		int j = RecipesAutoclave.instance().getTicks(tileEntity.getCurrentRecipe());
		return j != 0 && (j - i) != 0 ? (j - i) * pixels / j : 0;
	}

	private int getFluidTankScaled(int tank, int pixels) {
		int i = tileEntity.getTanks().get(tank).getFluidAmount();
		int j = tileEntity.getTanks().get(tank).getCapacity();
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}
