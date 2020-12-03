package redfire.mods.simplemachinery.tileentities.autoclave;

import redfire.mods.simplemachinery.tileentities.generic.GuiMachine;

public class GuiAutoclave extends GuiMachine<TileAutoclave, ContainerAutoclave> {
	public GuiAutoclave(TileAutoclave tileAutoclave, ContainerAutoclave container) {
		super(tileAutoclave, container, "textures/gui/container/autoclave.png");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		drawTexturedModalRect(guiLeft + 85, guiTop + 34, 176, 0, getProgressScaled(24) + 1, 16);
	}

	private int getProgressScaled(int pixels) {
		int i = tileEntity.getProgress();
		int j = RecipesAutoclave.instance().getTicks(tileEntity.getCurrentRecipe());;
		return j != 0 && (j - i) != 0 ? (j - i) * pixels / j : 0;
	}
}
