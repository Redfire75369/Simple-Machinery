package redfire.mods.simplemachinery.tileentities.fluidcentrifuge;

import redfire.mods.simplemachinery.tileentities.generic.GuiMachine;
import redfire.mods.simplemachinery.util.gui.GuiFluidRenderer;

public class GuiFluidCentrifuge extends GuiMachine<TileFluidCentrifuge, ContainerFluidCentrifuge> {
    public GuiFluidCentrifuge(TileFluidCentrifuge tileFluidCentrifuge, ContainerFluidCentrifuge container) {
        super(tileFluidCentrifuge, container, "textures/gui/container/fluid_centrifuge.png");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        drawTexturedModalRect(guiLeft + 64, guiTop + 24, 176, 0, getProgressScaled(24) + 1, 16);
        drawTexturedModalRect(guiLeft + 23, guiTop + 66, 0, 166, getEnergyStorageScaled(128), 10);

        GuiFluidRenderer.renderGuiTank(tileEntity.getTanks().get(0), guiLeft + 11, guiTop + 24, 0, getFluidTankScaled(0, 48), 16);
        GuiFluidRenderer.renderGuiTank(tileEntity.getTanks().get(1), guiLeft + 117, guiTop + 11, 0, getFluidTankScaled(1, 48), 16);
        GuiFluidRenderer.renderGuiTank(tileEntity.getTanks().get(2), guiLeft + 117, guiTop + 35, 0, getFluidTankScaled(2, 48), 16);
    }

    private int getProgressScaled(int pixels) {
        int i = tileEntity.getProgress();
        int j = RecipesFluidCentrifuge.instance().getTicks(tileEntity.getCurrentRecipe());
        return j != 0 && (j - i) != 0 ? (j - i) * pixels / j : 0;
    }

    private int getFluidTankScaled(int tank, int pixels) {
        int i = tileEntity.getTanks().get(tank).getFluidAmount();
        int j = tileEntity.getTanks().get(tank).getCapacity();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getEnergyStorageScaled(int pixels) {
        int i = tileEntity.getEnergyStorage().getEnergyStored();
        int j = tileEntity.getEnergyStorage().getMaxEnergyStored();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
}
