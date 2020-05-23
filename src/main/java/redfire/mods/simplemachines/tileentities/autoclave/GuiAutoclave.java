package redfire.mods.simplemachines.tileentities.autoclave;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import redfire.mods.simplemachines.SimpleMachines;

public class GuiAutoclave extends GuiContainer {
	public static final int width = 180;
	public static final int height = 152;

	private static final ResourceLocation background = new ResourceLocation(SimpleMachines.modid, "textures/gui/container/autoclave.png");

	public GuiAutoclave(TileAutoclave tileEntity, ContainerAutoclave container) {
		super(container);

		xSize = width;
		ySize = height;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
