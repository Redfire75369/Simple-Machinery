package redfire.mods.simplemachinery.tileentities.turntable;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import redfire.mods.simplemachinery.SimpleMachinery;

public class GuiTurntable extends GuiContainer {
	public static final int width = 176;
	public static final int height = 166;

	private static final ResourceLocation background = new ResourceLocation(SimpleMachinery.modid, "textures/gui/container/turntable.png");

	public GuiTurntable(TileTurntable tileEntity, ContainerTurntable container) {
		super(container);

		xSize = width;
		ySize = height;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		super.renderHoveredToolTip(mouseX, mouseY);
	}
}
