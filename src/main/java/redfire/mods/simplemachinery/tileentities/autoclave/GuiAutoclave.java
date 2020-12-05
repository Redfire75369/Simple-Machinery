package redfire.mods.simplemachinery.tileentities.autoclave;

import net.minecraft.util.ResourceLocation;
import redfire.mods.simplemachinery.tileentities.generic.GuiMachine;

public class GuiAutoclave extends GuiMachine<TileAutoclave, ContainerAutoclave> {
	public GuiAutoclave(TileAutoclave tileAutoclave, ContainerAutoclave container) {
		super(tileAutoclave, container, "textures/gui/container/autoclave.png");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

		drawTexturedModalRect(guiLeft + 85, guiTop + 34, 176, 0, getProgressScaled(24) + 1, 16);

		if (tileEntity.getTanks().get(1).getFluid() != null) {
			mc.getTextureManager().bindTexture(tileEntity.getTanks().get(1).getFluid().getFluid().getFlowing());
			int fluidHeight = getFluidTankScaled(1, 64);
			drawTexturedModalRect(guiLeft + 14, guiTop + 10 + 64 - fluidHeight, 0, 0, 16, fluidHeight);
		}

		if (tileEntity.getTanks().get(0).getFluid() != null) {
			ResourceLocation texture = tileEntity.getTanks().get(0).getFluid().getFluid().getFlowing();
			mc.getTextureManager().bindTexture(new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath()));
			int fluidHeight = getFluidTankScaled(0, 64);
			drawTexturedModalRect(guiLeft + 146, guiTop + 10 + 64 - fluidHeight, 0, 0, 16, fluidHeight);
		}
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
